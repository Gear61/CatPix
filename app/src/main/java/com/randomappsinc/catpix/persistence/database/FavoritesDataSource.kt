package com.randomappsinc.catpix.persistence.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.HandlerThread
import com.randomappsinc.catpix.models.CatPicture
import com.randomappsinc.catpix.utils.assertOnNonUiThread
import java.util.*

class FavoritesDataSource constructor(context: Context) {

    private var database: SQLiteDatabase? = null
    private val dbHelper: MySQLiteHelper = MySQLiteHelper(context)
    private val backgroundHandler: Handler

    init {
        val handlerThread = HandlerThread("Database")
        handlerThread.start()
        backgroundHandler = Handler(handlerThread.looper)
    }

    // Open connection to database
    @Throws(SQLException::class)
    private fun open() {
        database = dbHelper.writableDatabase
    }

    // Terminate connection to database
    private fun close() {
        dbHelper.close()
    }

    fun fetchFavorites(): ArrayList<CatPicture> {
        assertOnNonUiThread()
        open()
        val columns = arrayOf(
                MySQLiteHelper.COLUMN_PICTURE_ID,
                MySQLiteHelper.COLUMN_THUMBNAIL_URL,
                MySQLiteHelper.COLUMN_FULL_RES_URL,
                MySQLiteHelper.COLUMN_TIME_INSERTED)
        val orderBy = MySQLiteHelper.COLUMN_TIME_INSERTED + " DESC"
        val catPictures = ArrayList<CatPicture>()
        val cursor = database!!.query(
                MySQLiteHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                orderBy)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                catPictures.add(CatPicture(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)))
            }
            cursor.close()
        }
        close()
        return catPictures
    }

    fun addFavorite(catPicture: CatPicture) {
        backgroundHandler.post {
            open()
            val values = ContentValues()
            values.put(MySQLiteHelper.COLUMN_PICTURE_ID, catPicture.id)
            values.put(MySQLiteHelper.COLUMN_THUMBNAIL_URL, catPicture.getThumbnailUrlWithFallback())
            values.put(MySQLiteHelper.COLUMN_FULL_RES_URL, catPicture.getFullResUrlWithFallback())
            values.put(MySQLiteHelper.COLUMN_TIME_INSERTED, System.currentTimeMillis())
            database!!.insert(MySQLiteHelper.TABLE_NAME, null, values)
            close()
        }
    }

    fun removeFavorite(catPicture: CatPicture) {
        backgroundHandler.post {
            open()
            val whereArgs = arrayOf(catPicture.id)
            database!!.delete(
                    MySQLiteHelper.TABLE_NAME,
                    MySQLiteHelper.COLUMN_PICTURE_ID + " = ?",
                    whereArgs)
            close()
        }
    }
}
