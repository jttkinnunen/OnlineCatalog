package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ARTICLE_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_BRAND
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LAST_CHANGE
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_SHELF
import com.fcgtalent.fcgcatalog.util.ArticleResult
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ArticleResultMapper : RowMapper<ArticleResult> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): ArticleResult? {
        return ArticleResult(
            id = resultSet.getInt(FIELD_ID),
            name = resultSet.getString(FIELD_ARTICLE_NAME),
            brand = resultSet.getString(FIELD_BRAND),
            last_change = resultSet.getTimestamp(FIELD_LAST_CHANGE),
            shelf = resultSet.getString(FIELD_SHELF)
        )
    }
}