package com.ilya.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ilya.data.database.entity.MistakeEntity

@Dao
internal interface MistakesDao {

  @Query("SELECT * FROM mistakes ORDER BY count DESC")
  suspend fun getAllMistakes(): List<MistakeEntity>

  @Query("SELECT * FROM mistakes WHERE atWord = :word")
  suspend fun getMistakeByWord(word: String): MistakeEntity?

  @Upsert
  suspend fun saveMistake(mistake: MistakeEntity)

  @Query("DELETE FROM mistakes WHERE atWord = :atWord")
  suspend fun removeMistake(atWord: String)

}