package com.example.bancodedados

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bancodedados.dao.UsuarioDao
import com.example.bancodedados.model.Usuario

@Database(entities = [Usuario::class], version = 10)
abstract class AppDatabase: RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao
    companion object{
        private const val DATABASE_NOME = "DB2_USUARIOS"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NOME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}