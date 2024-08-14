package stream.alchemists.db

import javax.sql.DataSource

interface DatabaseFactory {
    fun createDataSource(): DataSource
}