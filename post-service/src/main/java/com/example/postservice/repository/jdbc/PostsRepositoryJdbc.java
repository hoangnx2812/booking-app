package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.object.HashtagsDTO;
import com.example.commericalcommon.dto.object.ServicesDTO;
import com.example.commericalcommon.utils.Constant;
import com.example.commericalcommon.utils.DateTimeFormatter;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;
import com.example.postservice.repository.PostCommentRepository;
import com.example.postservice.repository.PostLikeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.commericalcommon.utils.Util.isNotNull;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostsRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    ServicesRepositoryJdbc servicesRepositoryJdbc;
    HashtagRepositoryJdbc hashtagRepositoryJdbc;
    PostCommentRepository postCommentRepository;
    PostLikeRepository postLikeRepository;
    AttachmentRepositoryJdbc attachmentRepositoryJdbc;
    DateTimeFormatter dateTimeFormatter;

    public List<GetPostsResponse> getPostsByConditions(GetPostRequest request, int offset) {
        StringBuilder sql = new StringBuilder("""
                select u.full_name, u.avatar, p.id, p.title, services_ids, p.created_at
                from posts p
                         join user_info u on p.user_id = u.id
                where 1 = 1
                """);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String keyword = request.getKeyword();
        if (StringUtils.hasText(keyword)) {
            List<HashtagsDTO> hashtags = hashtagRepositoryJdbc.getHashtagsByConditions(
                    null,
                    Constant.Hashtag.ObjectType.POST,
                    keyword);

            if (CollectionUtils.isEmpty(hashtags)) {
                sql.append("""
                        and (p.title = :keyword or u.full_name = :keyword)
                        """);
            } else {
                sql.append("""
                        and (p.title = :keyword or u.full_name = :keyword or p.id in (:post_ids))
                        """);
                sqlParameterSource.addValue("keyword", keyword);
            }
            sqlParameterSource.addValue("keyword", keyword);
        }
        if (request.getPriceFrom() != null && request.getPriceTo() != null) {
            List<ServicesDTO> services = servicesRepositoryJdbc.getServicesByConditions(
                    null,
                    request.getPriceFrom(),
                    request.getPriceTo(),
                    null,
                    null);
            if (!CollectionUtils.isEmpty(services)) {
                sql.append("""
                    and services_ids in (:service_ids)
                    """);
                sqlParameterSource.addValue("service_ids", services.stream().map(ServicesDTO::getId).toList());
            }
        }
        sql.append(" order by p.created_at desc ");
        sql.append(" limit :size offset :offset ");
        sqlParameterSource.addValue("size", request.getSize());
        sqlParameterSource.addValue("offset", offset);
        return namedParameterJdbcTemplate.query(sql.toString(), sqlParameterSource, (rs, rowNum) ->
        {
            Long id = rs.getLong("id");
            return GetPostsResponse.builder()
                    .postId(id)
                    .userAvatar(rs.getString("avatar"))
                    .userFullName(rs.getString("full_name"))
                    .postTitle(rs.getString("title"))
                    .serviceName(servicesRepositoryJdbc.getServicesByConditions(null, null,
                                    null, null,
                                    List.of(rs.getLong("services_ids")))
                            .stream().map(ServicesDTO::getName).toList())
                    .createdAt("Được đăng vào " +
                            dateTimeFormatter.format(isNotNull(rs.getTimestamp("created_at"))))
                    .totalComments(String.valueOf(postCommentRepository.countByPosts_Id(id)))
                    .totalLikes(String.valueOf(postLikeRepository.countByPosts_Id(id)))
                    .attachments(attachmentRepositoryJdbc.getAllAttachments(id,
                            Constant.Attachment.ObjectType.POST))
                    .build();
        });
    }

    public Long totalElements(GetPostRequest request) {
        StringBuilder sql = new StringBuilder("""
                select count (*) as total
                from posts p
                         join authentication.public.user_info u on p.user_id = u.id
                where 1 = 1
                """);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        String keyword = request.getKeyword();
        if (StringUtils.hasText(keyword)) {
            List<HashtagsDTO> hashtags = hashtagRepositoryJdbc.getHashtagsByConditions(
                    null,
                    Constant.Hashtag.ObjectType.POST,
                    keyword);
            sql.append("""
                    and (p.title = :keyword or u.full_name = :keyword or p.id in (:post_ids))
                    """);
            sqlParameterSource.addValue("keyword", keyword);
            sqlParameterSource.addValue("post_ids", hashtags.stream().map(HashtagsDTO::getObjectId).toList());
        }
        if (request.getPriceFrom() != null && request.getPriceTo() != null) {
            List<ServicesDTO> services = servicesRepositoryJdbc.getServicesByConditions(
                    null,
                    request.getPriceFrom(),
                    request.getPriceTo(),
                    null,
                    null);
            sql.append("""
                    and services_ids in (:service_ids)
                    """);
            sqlParameterSource.addValue("service_ids", services.stream().map(ServicesDTO::getId).toList());
        }
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), sqlParameterSource, Long.class);
    }
}
