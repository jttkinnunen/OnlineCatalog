package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ADMIN
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_EMAIL
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_FIRST_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LAST_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_TOKEN
import com.fcgtalent.fcgcatalog.util.UserResult
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class UserResultMapper : RowMapper<UserResult> {
    override fun mapRow(rs: ResultSet, rowNum: Int): UserResult? {
        return UserResult(
            rs.getInt(FIELD_ID),
            rs.getString(FIELD_FIRST_NAME),
            rs.getString(FIELD_LAST_NAME),
            rs.getString(FIELD_EMAIL),
            rs.getInt(FIELD_ADMIN) == 1,
            rs.getString(FIELD_TOKEN)
        )
    }
}