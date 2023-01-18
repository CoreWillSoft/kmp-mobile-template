package io.template.app.shared.cache

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database by lazy { AppDatabase(databaseDriverFactory.createDriver()) }
    private val dbQuery by lazy { database.appDatabaseQueries }
}