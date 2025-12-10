package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.object.HashtagsDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HashtagRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<HashtagsDTO> getHashtagsByConditions(Long objectId, String objectType, String name) {
        StringBuilder sql = new StringBuilder("""
                select hm.id, h.hashtag, hm.object_type, hm.object_id
                from hashtags h join hashtags_map hm on hashtags.id = hm.hashtag_id
                where 1 = 1
                """);
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (objectId != null) {
            sql.append(" and object_id = :object_id ");
            params.addValue("object_id", objectId);
        }
        if (StringUtils.hasText(name)) {
            sql.append(" and hashtag = :hashtag ");
            params.addValue("hashtag", name);
        }
        if (StringUtils.hasText(objectType)) {
            sql.append(" and object_type = :object_type ");
            params.addValue("object_type", objectType);
        }
        return namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rowNum) ->
                HashtagsDTO.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("hashtag"))
                        .objectType(rs.getString("object_type"))
                        .objectId(rs.getLong("object_id"))
                        .build());
    }
}
