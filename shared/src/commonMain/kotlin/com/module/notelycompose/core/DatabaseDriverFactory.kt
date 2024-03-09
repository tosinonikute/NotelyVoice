package com.module.notelycompose.core

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {

    fun create(): SqlDriver
}