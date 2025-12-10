package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.object.AttachmentDTO;
import com.example.commericalcommon.dto.response.AttachmentResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AttachmentRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<AttachmentResponse> getAllAttachments(Long objectId, String objectType) {
        String sql = """
                select am.id,
                       am.display_name,
                       a.mime_type,
                       a.size,
                       a.thumbnail,
                       a.file_path_sm,
                       a.file_path_lg,
                       a.file_path_original
                from attachment_map am
                         join attachment a on am.attachment_id = a.id
                where object_id = :object_id
                  and object_type = :object_type
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("object_id", objectId)
                .addValue("object_type", objectType);
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
                AttachmentResponse.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .thumbnail(rs.getString("thumbnail"))
                        .pathSmall(rs.getString("path_small"))
                        .pathLarge(rs.getString("path_large"))
                        .pathOriginal(rs.getString("path_original"))
                        .size(rs.getString("size"))
                        .type(rs.getString("type"))
                        .build());
    }

    public AttachmentDTO checkSumExists(String checkSum) {
        String sql = """
                select id, file_name
                from attachment
                where checksum = :checksum
                and status = 'A'
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("checksum", checkSum);
        List<AttachmentDTO> attachments = namedParameterJdbcTemplate.query(sql, params,
                (rs, rowNum) ->
                        AttachmentDTO.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .build());
        return attachments.isEmpty() ? null : attachments.getFirst();
    }




}
