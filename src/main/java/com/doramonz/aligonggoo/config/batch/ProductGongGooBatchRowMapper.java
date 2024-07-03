package com.doramonz.aligonggoo.config.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductGongGooBatchRowMapper implements RowMapper<ProductGongGooBatchDao> {
    @Override
    public ProductGongGooBatchDao mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductGongGooBatchDao(rs.getInt("product_gongGoo_id"), rs.getString("gongGoo_url"), rs.getBoolean("gongGoo_status"), rs.getTimestamp("gongGoo_created").toLocalDateTime());
    }
}
