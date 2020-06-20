package com.imaec.wishplace.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.KeywordDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.KeywordEntity
import com.imaec.wishplace.room.entity.PlaceEntity

@Database(entities = [
    CategoryEntity::class,
    PlaceEntity::class,
    KeywordEntity::class
], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun placeDao(): PlaceDao
    abstract fun keywordDao(): KeywordDao

    companion object {
        private const val DB_NAME = "room-db"
        private var instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE keywordENTITY (keyword TEXT NOT NULL, saveTime TEXT NOT NULL, keywordId INTEGER NOT NULL, PRIMARY KEY(keywordId))")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS place_backup (" +
                        "placeId INTEGER NOT NULL, foreignId INTEGER NOT NULL, " +
                        "name TEXT NOT NULL, address TEXT NOT NULL, siteUrl TEXT NOT NULL, content TEXT NOT NULL, " +
                        "imageUrl TEXT NOT NULL, saveTime TEXT NOT NULL, visitFlag INTEGER NOT NULL, " +
                        "PRIMARY KEY(placeId)" +
                        ")")
                database.execSQL("INSERT INTO place_backup (placeId, foreignId, name, address, siteUrl, content, imageUrl, saveTime, visitFlag) " +
                        "SELECT placeId, foreignId, name, address, siteUrl, content, imageUrl, saveTime, visitFlag FROM placeENTITY")
                database.execSQL("DROP TABLE placeENTITY")
                database.execSQL("ALTER TABLE place_backup RENAME TO placeENTITY")
            }
        }

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context) : AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addCallback(object : RoomDatabase.Callback() {
                    // Created Database
                }).build()
        }
    }
}