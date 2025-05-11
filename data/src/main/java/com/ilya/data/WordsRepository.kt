package com.ilya.data

interface WordsRepository {
  suspend fun getAll(): List<String>
}