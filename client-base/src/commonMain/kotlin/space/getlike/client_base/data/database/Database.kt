package space.getlike.client_base.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import space.getlike.client_base.data.database.daos.AchievementDao
import space.getlike.client_base.data.database.daos.DeferredMarkedReadDao
import space.getlike.client_base.data.database.daos.RegisteredLocalContactIdDao
import space.getlike.client_base.data.database.daos.MessageDao
import space.getlike.client_base.data.database.daos.ProfileDao
import space.getlike.client_base.data.database.dbos.AchievementDbo
import space.getlike.client_base.data.database.dbos.DeferredMarkedReadDbo
import space.getlike.client_base.data.database.dbos.RegisteredLocalContactIdDbo
import space.getlike.client_base.data.database.dbos.MessageDbo
import space.getlike.client_base.data.database.dbos.ProfileDbo

@Database(
    entities = [
        ProfileDbo::class,
        AchievementDbo::class,
        MessageDbo::class,
        DeferredMarkedReadDbo::class,
        RegisteredLocalContactIdDbo::class,
    ],
    version = 1,
)
@ConstructedBy(DatabaseConstructor::class)
abstract class Database : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    abstract fun achievementDao(): AchievementDao

    abstract fun messageDao(): MessageDao

    abstract fun deferredMarkedReadDao(): DeferredMarkedReadDao

    abstract fun registeredLocalContactIdDao(): RegisteredLocalContactIdDao
}
