package com.example.postservice.repository.jdbc;

import com.example.commericalcommon.dto.object.ServicesDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServicesRepositoryJdbc {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<ServicesDTO> getServicesByConditions(String title, Double priceFrom, Double priceTo,
                                                     String duration, List<Long> serviceIds) {
        StringBuilder sql = new StringBuilder("select * from services where 1 = 1 ");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (StringUtils.hasText(title)) {
            sql.append(" and name = :title ");
            parameterSource.addValue("title", "%" + title + "%");
        }
        if (priceFrom != null && priceTo != null) {
            sql.append(" and price between :priceFrom and :priceTo ");
            parameterSource.addValue("priceFrom", priceFrom);
            parameterSource.addValue("priceTo", priceTo);
        }
        if (StringUtils.hasText(duration)) {
            sql.append(" and time = :duration ");
            parameterSource.addValue("duration", duration);
        }
        if (CollectionUtils.isEmpty(serviceIds)) {
            sql.append(" and id in (:serviceIds) ");
            parameterSource.addValue("serviceIds", serviceIds);
        }
        return namedParameterJdbcTemplate.query(sql.toString(), parameterSource, (rs, rowNum) ->
                ServicesDTO.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("title"))
                        .price(rs.getDouble("price"))
                        .userId(rs.getLong("user_id"))
                        .storeId(rs.getLong("store_id"))
                        .time(rs.getString("duration_time"))
                        .build());
    }
}
