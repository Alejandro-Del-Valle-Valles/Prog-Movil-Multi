package com.alejandro.notas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alejandro.notas.data.dao.DaoCategory
import com.alejandro.notas.data.dao.DaoNote
import com.alejandro.notas.model.Category
import com.alejandro.notas.model.Note

@Database(entities = [Note::class, Category::class], version = 1, exportSchema = true)
abstract class DataBaseApp : RoomDatabase() {

    abstract fun daoNote(): DaoNote
    abstract fun daoCategory(): DaoCategory

    companion object {
        @Volatile
        private var INSTANCE: DataBaseApp? = null

        fun getDataBase(context: Context): DataBaseApp {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseApp::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}