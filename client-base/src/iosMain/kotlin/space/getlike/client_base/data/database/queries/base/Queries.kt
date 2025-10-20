package space.getlike.client_base.data.database.queries.base

import space.getlike.client_base.data.database.Database

actual suspend fun <R> Database.transaction(block: suspend Database.() -> R): R =
    block()