package space.getlike.client_base.data.database.queries.base

import space.getlike.client_base.data.database.Database

abstract class Queries(private val database: Database) {

    suspend fun <R> transaction(block: suspend Database.() -> R) =
        database.transaction(block)
}

internal expect suspend fun <R> Database.transaction(block: suspend Database.() -> R): R
