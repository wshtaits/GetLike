package space.getlike.client_base.data.database

import androidx.room.RoomDatabaseConstructor

expect object DatabaseConstructor : RoomDatabaseConstructor<Database> {

    override fun initialize(): Database
}