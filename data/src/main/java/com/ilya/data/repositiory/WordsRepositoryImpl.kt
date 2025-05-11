package com.ilya.data.repositiory

import android.content.res.AssetManager
import com.ilya.data.WordsRepository
import javax.inject.Inject

internal class WordsRepositoryImpl @Inject constructor(private val assetManager: AssetManager) : WordsRepository {

  override suspend fun getAll(): List<String> {
    return assetManager.open(FILE_NAME).use { input ->
      input.reader().use { it.readLines() }
    }
  }

  companion object {
    private const val FILE_NAME = "words.txt"
  }

}