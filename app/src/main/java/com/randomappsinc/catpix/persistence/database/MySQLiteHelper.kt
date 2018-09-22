package com.randomappsinc.catpix.persistence.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper internal constructor(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_FAVORITES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        const val TABLE_NAME = "favorites"

        const val COLUMN_PICTURE_ID = "picture_id"
        const val COLUMN_THUMBNAIL_URL = "thumbnail_url"
        const val COLUMN_FULL_RES_URL= "full_res_url"
        const val COLUMN_TIME_INSERTED = "time_inserted"

        private const val DATABASE_NAME = "CatPix.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_FAVORITES_TABLE = ("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "(" + COLUMN_PICTURE_ID + " TEXT, " + COLUMN_THUMBNAIL_URL
                + " TEXT, " + COLUMN_FULL_RES_URL + " TEXT, " + COLUMN_TIME_INSERTED + " INTEGER);")
    }
}
