package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class AuthenticateTokenMapper : RowMapper<Boolean> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): Boolean? {
        return resultSet.getInt(DatabaseConnector.FIELD_ADMIN) == 1
    }
}