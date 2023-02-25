package com.example.tobuy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tobuy.room.AppDatabase

class MainActivity : AppCompatActivity() {

    private val appDatabase: RoomDatabase by lazy {
        createRoomDatabase()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun createRoomDatabase(): AppDatabase {
        return Room.databaseBuilder(this, AppDatabase::class.java, "to_buy_database").build()
    }

}
