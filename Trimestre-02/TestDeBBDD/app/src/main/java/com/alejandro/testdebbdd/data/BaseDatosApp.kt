package com.alejandro.testdebbdd.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tarea::class], version = 1, exportSchema = true)
abstract class BaseDatosApp : RoomDatabase(){

    abstract fun daoTarea(): DaoTarea

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