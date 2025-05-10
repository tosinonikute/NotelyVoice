package com.module.notelycompose.notes.ui.extensions

fun String.firstToUpperCase(): String {
    return this.replaceFirstChar { it.uppercaseChar() }
}
