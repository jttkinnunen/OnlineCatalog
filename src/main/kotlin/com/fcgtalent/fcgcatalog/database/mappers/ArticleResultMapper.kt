package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ARTICLE_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_IMAGE
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LAST_CHANGE
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_DESCRIPTION
import com.fcgtalent.fcgcatalog.util.ArticleResult
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ArticleResultMapper : RowMapper<ArticleResult> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): ArticleResult? {
        return ArticleResult(
            id = resultSet.getInt(FIELD_ID),
            name = resultSet.getString(FIELD_ARTICLE_NAME),
            image = resultSet.getString(FIELD_IMAGE),
            last_change = resultSet.getTimestamp(FIELD_LAST_CHANGE),
            description = resultSet.getString(FIELD_DESCRIPTION)
        )
    }
}