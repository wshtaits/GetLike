package space.getlike.client_base.data.database.queries.base

import androidx.room.withTransaction
import space.getlike.client_base.data.database.Database

actual suspend fun <R> Database.transaction(block: suspend Database.() -> R): R =
    withTransaction { block() }