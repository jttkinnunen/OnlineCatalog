package com.fcgtalent.fcgcatalog.database.mappers

import com.fcgtalent.fcgcatalog.database.DatabaseConnector
import com.fcgtalent.fcgcatalog.util.LocationResult
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class LocationResultMapper : RowMapper<LocationResult> {
    override fun mapRow(resultSet: ResultSet, rowNum: Int): LocationResult? {
        return LocationResult(
            resultSet.getInt(DatabaseConnector.FIELD_ID),
            resultSet.getString(DatabaseConnector.FIELD_LOCATION_NAME)
        )
    }
}