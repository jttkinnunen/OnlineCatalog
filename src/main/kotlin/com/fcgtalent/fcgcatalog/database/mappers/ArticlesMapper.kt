package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ARTICLE_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_ARTICLE_NAME
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_DESCRIPTION
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_IMAGE
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LAST_CHANGE
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LOCATION_ID
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_QUANTITY
import com.fcgtalent.fcgcatalog.database.DatabaseConnector.Companion.FIELD_LOCATION_NAME
import com.fcgtalent.fcgcatalog.util.ArticleResult
import com.fcgtalent.fcgcatalog.util.LocationQuantityResult
import org.springframework.jdbc.core.RowMapper
import java.lang.NumberFormatException
import java.sql.ResultSet

open class ArticlesMapper : RowMapper<ArticleResult> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): ArticleResult? {
        val locationQuantity = mutableListOf<LocationQuantityResult>()
        val locationIds = resultSet.getString(FIELD_LOCATION_ID)?.split(",")
        // TODO this causes issue if location name has comma
        val locationNames = resultSet.getString(FIELD_LOCATION_NAME)?.split(",")
        val quantities = resultSet.getString(FIELD_QUANTITY)?.split(",")

        // TODO add what you ma call it, index out of bound protection
        try {
            locationIds?.forEachIndexed { index: Int, it ->
                locationQuantity.add(LocationQuantityResult(it.toInt(), locationNames!![index], quantities!![index].toInt()))
            }
        } catch (e: IndexOutOfBoundsException) {
            assert(false) { "Index out of bounds, shouldn't happen ${e.message}" }
        } catch (ne: NumberFormatException) {
            assert(false) { "Number format exception, shouldn't happen ${ne.message}" }
        }
        return ArticleResult(
            id = resultSet.getInt(FIELD_ARTICLE_ID),
            name = resultSet.getString(FIELD_ARTICLE_NAME),
            image = resultSet.getString(FIELD_IMAGE),
            last_change = resultSet.getTimestamp(FIELD_LAST_CHANGE),
            description = resultSet.getString(FIELD_DESCRIPTION),
            locations = locationQuantity
        )
    }
}

class ArticlesMapperNullDescription: ArticlesMapper() {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): ArticleResult? {
        val articleResult = super.mapRow(resultSet, rowNum)
        articleResult?.description = null
        return articleResult
    }
}
