package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_PASSWORD
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

data class LoginResult(val id: Int, val password: String)

class LoginMapper : RowMapper<LoginResult> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): LoginResult? {
        return LoginResult(
            resultSet.getInt(FIELD_ID),
            resultSet.getString(FIELD_PASSWORD)
        )
    }
}