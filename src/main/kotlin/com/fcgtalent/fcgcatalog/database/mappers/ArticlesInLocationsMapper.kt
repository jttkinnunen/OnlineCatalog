package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ARTICLE_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ARTICLE_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_BRAND
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LAST_CHANGE
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LOCATION_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_QUANTITY
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_DESCRIPTION
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LOCATION_NAME
import com.fcgtalent.fcgcatalog.util.ArticlesInLocationsResult
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ArticlesInLocationsMapper : RowMapper<ArticlesInLocationsResult> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): ArticlesInLocationsResult? {
        return ArticlesInLocationsResult(
            id = resultSet.getInt(FIELD_ARTICLE_ID),
            name = resultSet.getString(FIELD_ARTICLE_NAME),
            brand = resultSet.getString(FIELD_BRAND),
            last_change = resultSet.getTimestamp(FIELD_LAST_CHANGE),
            description = resultSet.getString(FIELD_DESCRIPTION),
            locationId = resultSet.getInt(FIELD_LOCATION_ID),
            locationName = resultSet.getString(FIELD_LOCATION_NAME),
            quantity = resultSet.getInt(FIELD_QUANTITY)
        )
    }
}