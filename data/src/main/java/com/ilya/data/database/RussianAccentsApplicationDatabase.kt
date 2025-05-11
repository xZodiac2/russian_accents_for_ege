package com.ilya.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilya.data.database.dao.MistakesDao
import com.ilya.data.database.entity.MistakeEntity

@Database(entities = [MistakeEntity::class], version = 1)
internal abstract class RussianAccentsApplicationDatabase : RoomDatabase() {
  abstract val mistakesDao: MistakesDao
}