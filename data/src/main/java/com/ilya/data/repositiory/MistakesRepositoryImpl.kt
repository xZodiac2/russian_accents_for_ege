package com.ilya.data.repositiory

import com.ilya.data.MistakesRepository
import com.ilya.data.database.RussianAccentsApplicationDatabase
import com.ilya.data.models.Mistake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MistakesRepositoryImpl @Inject constructor(
  private val database: RussianAccentsApplicationDatabase
) : MistakesRepository {

  override suspend fun getAllMistakes(): List<Mistake> {
    return withContext(Dispatchers.IO) { database.mistakesDao.getAllMistakes().map { it.toMistake() } }
  }

  override suspend fun getMistakeByWord(word: String): Result<Mistake> {
    val mistake = withContext(Dispatchers.IO) { database.mistakesDao.getMistakeByWord(word)?.toMistake() }
    return mistake?.let { Result.success(it) } ?: Result.failure(RepositoryError.MistakeNotFound)
  }

  override suspend fun saveMistake(mistake: Mistake) {
    withContext(Dispatchers.IO) { database.mistakesDao.saveMistake(mistake.toEntity()) }
  }

  override suspend fun removeMistakeByWord(word: String) {
    withContext(Dispatchers.IO) { database.mistakesDao.removeMistake(word) }
  }

}