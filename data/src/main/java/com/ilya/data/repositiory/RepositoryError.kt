package com.ilya.data.repositiory

sealed class RepositoryError : Throwable() {
    data object MistakeNotFound : RepositoryError() {
        private fun readResolve(): Any = MistakeNotFound
    }
}