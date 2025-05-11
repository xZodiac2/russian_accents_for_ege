package com.ilya.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("mistakes")
internal data class MistakeEntity(
  @PrimaryKey
  val atWord: String,
  val count: Int
)