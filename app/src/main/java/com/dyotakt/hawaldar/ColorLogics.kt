package com.dyotakt.hawaldar

import androidx.compose.ui.graphics.Color

class ColorLogics {

    fun ConvertToColor(backgroundColor: String): Color {
        val red: Int = Integer.parseInt(backgroundColor.take(3))
        val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
        val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
        return Color(red, green, blue)
    }

    fun getFontColorDynamic(backgroundColor: String): Color {
        val red: Int = Integer.parseInt(backgroundColor.take(3))
        val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
        val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
        val averageColor: Int = ((red+green+blue))/3
        return if(averageColor>127) {
            Color(0,0,0,100)
        } else {
            Color(255,255,255,100)
        }
    }

    fun getFontColorDynamicExtraAlpha(backgroundColor: String): Color {
        val red: Int = Integer.parseInt(backgroundColor.take(3))
        val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
        val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
        val averageColor: Int = ((red+green+blue))/3
        return if(averageColor>127) {
            Color(0,0,0,30)
        } else {
            Color(255,255,255,30)
        }
    }
}