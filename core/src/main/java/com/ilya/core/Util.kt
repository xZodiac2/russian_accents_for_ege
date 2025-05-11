package com.ilya.core

import android.util.Log

fun myLog(loggable: Any?) {
  Log.d("mytag", loggable.toString())
}