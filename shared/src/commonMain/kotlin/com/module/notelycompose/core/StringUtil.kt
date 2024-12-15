package com.module.notelycompose.core

object StringUtil {
    fun String.sanitiseText(): String {
        return this.replace(Regex("[\\r\\n]+"), "")
    }
}