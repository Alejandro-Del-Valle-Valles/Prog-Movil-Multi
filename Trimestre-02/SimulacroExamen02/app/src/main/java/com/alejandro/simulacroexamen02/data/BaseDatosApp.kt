package com.alejandro.simulacroexamen02.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Hechizo::class], version = 1, exportSchema =
    true)
abstract class BaseDatosApp : RoomDatabase() {
    abstract fun daoTarea(): DaoHechizo
    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosApp? = null
        fun getBaseDatos(context: Context): BaseDatosApp {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosApp::class.java,
                    "GrimorioDB"
                ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}