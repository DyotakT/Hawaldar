package com.dyotakt.hawaldar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

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

    fun getDarkerShade(backgroundColor: String): Color {
        val red: Int = Integer.parseInt(backgroundColor.take(3))
        val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
        val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
        return Color(red/2,green/2,blue/2)
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

    fun getDifferentShade(backgroundColor: Color): Color {
        return Color(115, 149, 98);
    }
}