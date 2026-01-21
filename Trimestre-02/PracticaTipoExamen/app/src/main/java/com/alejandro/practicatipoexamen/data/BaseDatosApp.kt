package com.alejandro.practicatipoexamen.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

@Database(entities = [Carta::class], version = 1, exportSchema = true)
abstract class BaseDatosApp : RoomDatabase() {

    abstract fun daoCarta(): DaoCarta

    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosApp? = null
        fun getBaseDatos(context: Context): BaseDatosApp {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosApp::class.java,
                    "todo_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}