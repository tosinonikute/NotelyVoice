package com.module.notelycompose.core

import android.content.Context
import com.module.notelycompose.database.NoteDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(NoteDatabase.Schema, context = context, "notes.db")
    }
}