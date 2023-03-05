package com.example.customanalogclock

import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class HandsHelper {
    fun getHandThickness(radius: Float, coefficient: Int) = when (coefficient) {
        0 -> radius * 0.01f
        in 1..10 -> radius * coefficient * 0.01f
        else -> radius * 0.1f
    }

    fun getHandLength(radius: Float, coefficient: Int) = when (coefficient) {
        0 -> radius * 0.1f
        in 1..10 -> radius * coefficient * 0.1f
        else -> radius
    }

    fun getDivisionCoordinates(width: Float, height: Float, radius: Float, angle: Double): Point {
        val x = (width / 2 + cos(angle) * radius).toFloat()
        val y = (height / 2 + sin(angle) * radius).toFloat()
        return Point(x, y)
    }

    fun getHandCoordinates(
        width: Float,
        height: Float,
        handLength: Float,
        loc: Float,
        isBackHand: Boolean
    ): HandCoordinates {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val startPoint = Point(x = width / 2, y = height / 2)
        val endPoint = if (!isBackHand) {
            Point(
                x = (width / 2 + cos(angle) * handLength).toFloat(),
                y = (height / 2 + sin(angle) * handLength).toFloat()
            )
        }
        else{
            Point(
                x = (width / 2 - cos(angle) * handLength).toFloat(),
                y = (height / 2 - sin(angle) * handLength).toFloat()
            )
        }

        return HandCoordinates(
            startPoint,
            endPoint
        )
    }
}

class Time private constructor(val hour: Int, val minute: Int, val second: Int) {
    companion object {
        fun getCurrentTime(): Time {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
            return Time(hour, calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        }
    }
}

data class HandCoordinates(
    val startPoint: Point,
    val stopPoint: Point
)

data class Point(val x: Float, val y: Float)

enum class HandType {
    HOURS,
    MINUTES,
    SECONDS
}