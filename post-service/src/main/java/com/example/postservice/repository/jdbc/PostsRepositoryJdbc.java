package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.object.HashtagsDTO;
import com.example.commericalcommon.dto.object.UserServicesDTO;
import com.example.commericalcommon.dto.request.GetUserInfoRequest;
import com.example.commericalcommon.dto.request.GetUserServiceRequest;
import com.example.commericalcommon.dto.response.AttachmentResponse;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import com.example.commericalcommon.enums.ObjectType;
import com.example.commericalcommon.service.RedisService;
import com.example.commericalcommon.utils.DateTimeFormatter;
import com.example.commericalcommon.utils.RedisConstant;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;
import com.example.postservice.repository.httpclient.AuthenticationClient;
import com.example.postservice.repository.httpclient.CustomerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;
import static com.example.commericalcommon.utils.Util.isNotNull;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostsRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    CustomerClient customerClient;
    HashtagRepositoryJdbc hashtagRepositoryJdbc;
    AttachmentRepositoryJdbc attachmentRepositoryJdbc;
    DateTimeFormatter dateTimeFormatter;
    AuthenticationClient authenticationClient;
    RedisService redisService;
    ObjectMapper objectMapper;

    private record PostRow(Long id, Long userInfoId, String title, Timestamp createdAt,
                           int totalComments, int totalLikes) {
    }

    public List<GetPostsResponse> getPostsByConditions(GetPostRequest request, int offset) {
        // Phase 1: Build main SQL — counts embedded as correlated subqueries, no JOIN side-effects
        StringBuilder sql = new StringBuilder("""
                select p.id,
                       p.title,
                       p.created_at,
                       p.user_info_id,
                       (select count(*) from post_comments pc where pc.post_id = p.id) as total_comments,
                       (select count(*) from post_likes   pl where pl.post_id = p.id) as total_likes
                from posts p
                where 1 = 1
                and p.is_active = true
                """);
        MapSqlParameterSource params = new MapSqlParameterSource();

        String keyword = request.getKeyword();
        if (StringUtils.hasText(keyword)) {
            List<HashtagsDTO> hashtags = hashtagRepositoryJdbc.getHashtagsByConditions(
                    null, ObjectType.POST.getType(), keyword);
            if (CollectionUtils.isEmpty(hashtags)) {
                sql.append(" and p.title = :keyword ");
            } else {
                sql.append(" and (p.title = :keyword or p.id in (:keyword_post_ids)) ");
                params.addValue("keyword_post_ids",
                        hashtags.stream().map(HashtagsDTO::getObjectId).toList());
            }
            params.addValue("keyword", keyword);
        }

        if (request.getPriceFrom() != null && request.getPriceTo() != null) {
            List<UserServicesDTO> services = customerClient.getUserByConditions(
                    GetUserServiceRequest.builder()
                            .priceFrom(request.getPriceFrom())
                            .priceTo(request.getPriceTo())
                            .build()).getData();
            if (!CollectionUtils.isEmpty(services)) {
                sql.append(" and p.id in (:price_post_ids) ");
                params.addValue("price_post_ids",
                        services.stream().map(UserServicesDTO::getObjectId).toList());
            }
        }

        sql.append(" order by p.created_at desc limit :size offset :offset ");
        params.addValue("size", request.getSize());
        params.addValue("offset", offset);

        // Phase 2: Execute main query — pure data extraction, no side effects
        List<PostRow> rows = namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rowNum) ->
                new PostRow(
                        rs.getLong("id"),
                        rs.getLong("user_info_id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("total_comments"),
                        rs.getInt("total_likes")
                ));

        if (rows.isEmpty()) return List.of();

        List<Long> postIds = rows.stream().map(PostRow::id).toList();
        List<Long> userInfoIds = rows.stream().map(PostRow::userInfoId).distinct().toList();

        // Phase 3: Batch load — 3 operations total instead of N*3
        Map<Long, List<AttachmentResponse>> attachmentsMap =
                attachmentRepositoryJdbc.getAllAttachmentsBatch(postIds, ObjectType.POST.getType());
        Map<Long, List<String>> serviceNamesMap = batchLoadServiceNames(postIds);
        Map<Long, UserInfoResponse> userInfoMap = batchLoadUserInfo(userInfoIds);

        // Phase 4: Assemble responses
        return rows.stream()
                .map(row -> {
                    UserInfoResponse user = userInfoMap.get(row.userInfoId());
                    if (user == null) return null;
                    return GetPostsResponse.builder()
                            .postId(row.id())
                            .userInfoId(row.userInfoId())
                            .userAvatar(user.getAvatar())
                            .userFullName(user.getFullName())
                            .postTitle(row.title())
                            .serviceName(serviceNamesMap.getOrDefault(row.id(), List.of()))
                            .createdAt("Được đăng vào " + dateTimeFormatter.format(isNotNull(row.createdAt())))
                            .totalComments(String.valueOf(row.totalComments()))
                            .totalLikes(String.valueOf(row.totalLikes()))
                            .attachments(attachmentsMap.getOrDefault(row.id(), List.of()))
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }

    // One HTTP call for all posts instead of N calls
    private Map<Long, List<String>> batchLoadServiceNames(List<Long> postIds) {
        List<UserServicesDTO> services = customerClient.getUserByConditions(
                GetUserServiceRequest.builder()
                        .objectIds(postIds.stream().map(String::valueOf).toList())
                        .objectType(ObjectType.POST.getType())
                        .build()).getData();
        if (CollectionUtils.isEmpty(services)) return Map.of();
        return services.stream()
                .filter(s -> s.getObjectId() != null)
                .collect(Collectors.groupingBy(
                        UserServicesDTO::getObjectId,
                        Collectors.mapping(UserServicesDTO::getName, Collectors.toList())));
    }

    // Redis-first; HTTP only for cache misses (unique users, not per-post)
    private Map<Long, UserInfoResponse> batchLoadUserInfo(List<Long> userInfoIds) {
        Map<Long, UserInfoResponse> result = new HashMap<>();
        List<Long> cacheMisses = new ArrayList<>();

        for (Long id : userInfoIds) {
            UserInfoResponse cached = redisService.hget(
                    RedisConstant.Htable.USER_INFO, Long.toString(id), UserInfoResponse.class);
            if (cached != null) {
                result.put(id, cached);
            } else {
                cacheMisses.add(id);
            }
        }

        for (Long id : cacheMisses) {
            BaseResponse<UserInfoResponse> response = authenticationClient.getUserByConditions(
                    GetUserInfoRequest.builder().userInfoId(id).build());
            log.info("Response from authentication-service for userInfoId={}: {}", id, response);
            if (!SUCCESS_CODE.equals(response.getResultCode())) continue;
            UserInfoResponse userInfo = response.getData();
            result.put(id, userInfo);
            try {
                redisService.hset(RedisConstant.Htable.USER_INFO,
                        Long.toString(id),
                        objectMapper.writeValueAsString(userInfo),
                        RedisConstant.Htable.TTL_DEFAULT);
            } catch (JsonProcessingException e) {
                log.error("Error caching user info to Redis for id={}", id, e);
            }
        }
        return result;
    }
}
