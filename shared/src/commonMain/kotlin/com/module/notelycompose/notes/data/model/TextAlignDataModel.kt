package com.module.notelycompose.notes.data.model

sealed class TextAlignDataModel {
    data object Left : TextAlignDataModel()
    data object Right : TextAlignDataModel()
    data object Center : TextAlignDataModel()
    data object Justify : TextAlignDataModel()
    data object Start : TextAlignDataModel()
    data object End : TextAlignDataModel()

    companion object {
        fun valueOf(name: String): TextAlignDataModel {
            return when (name.lowercase()) {
                "left" -> Left
                "right" -> Right
                "center" -> Center
                "justify" -> Justify
                "start" -> Start
                "end" -> End
                else -> throw IllegalArgumentException("Invalid TextAlign value: $name")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Left -> "Left"
            is Right -> "Right"
            is Center -> "Center"
            is Justify -> "Justify"
            is Start -> "Start"
            is End -> "End"
        }
    }
}
