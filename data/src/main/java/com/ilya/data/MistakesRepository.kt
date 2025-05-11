package com.ilya.data

import com.ilya.data.models.Mistake

interface MistakesRepository {
  suspend fun getAllMistakes(): List<Mistake>
  suspend fun getMistakeByWord(word: String): Result<Mistake>
  suspend fun saveMistake(mistake: Mistake)
  suspend fun removeMistakeByWord(word: String)
}