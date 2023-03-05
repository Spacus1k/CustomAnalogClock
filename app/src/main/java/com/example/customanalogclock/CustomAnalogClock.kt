package com.example.customanalogclock

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.customanalogclock.databinding.ClockLayoutBinding

class CustomAnalogClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private val HOURS_RANGE = (1..12)
        private val MINUTES_RANGE = (1..60)
    }

    private val binding: ClockLayoutBinding

    private var height: Float = 0f
    private var width: Float = 0f
    private var padding: Float = 0f
    private var fontSize: Float = 0f
    private var radius: Float = 0f
    private var _paint: Paint? = null
    private val rect = Rect()

    private val paint get() = _paint!!
    private var isInit = false

    private var backgroundColor = Color.WHITE
    private var clockColor = Color.BLACK
    private var centerColor = Color.BLACK
    private var numeralColor = Color.BLACK

    private var divisionsColor = Color.BLACK
    private var isShowDivisions: Boolean = true

    private val handsHelper = HandsHelper()

    private var hourHandColor = Color.BLACK
    private var hourHandThicknessCoeff: Int = 0
    private var hourHandLengthCoeff: Int = 0

    private var minutesHandColor = Color.BLACK
    private var minutesHandThicknessCoeff: Int = 0
    private var minutesHandLengthCoeff: Int = 0

    private var secondsHandColor = Color.BLACK
    private var secondsThicknessCoeff: Int = 0
    private var secondsLengthCoeff: Int = 0

    init {
        val inflater = LayoutInflater.from(context)
        //inflater.inflate(R.layout.clock_layout, this, true)
        binding = ClockLayoutBinding.bind(this)
        initAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomAnalogClock,
            defStyleAttr,
            defStyleRes
        )
        with(typedArray) {
            isShowDivisions =
                getBoolean(R.styleable.CustomAnalogClock_showDivisions, true)

            initColorsAttrs(this)
            initHandsAttrs(this)

            typedArray.recycle()
        }
    }

    private fun initHandsAttrs(customAttrs: TypedArray) {
        with(customAttrs) {
            hourHandThicknessCoeff =
                getInteger(R.styleable.CustomAnalogClock_hourHandThickness, 6)
            minutesHandThicknessCoeff =
                getInteger(R.styleable.CustomAnalogClock_minutesHandThickness, 3)
            secondsThicknessCoeff =
                getInteger(R.styleable.CustomAnalogClock_secondsHandThickness, 1)

            hourHandLengthCoeff = getInteger(R.styleable.CustomAnalogClock_hourHandLength, 4)
            minutesHandLengthCoeff =
                getInteger(R.styleable.CustomAnalogClock_minutesHandLength, 6)
            secondsLengthCoeff = getInteger(R.styleable.CustomAnalogClock_secondsHandLength, 10)
        }
    }

    private fun initColorsAttrs(customAttrs: TypedArray) {
        with(customAttrs) {
            backgroundColor =
                getColor(R.styleable.CustomAnalogClock_clockBackground, Color.WHITE)
            clockColor =
                getColor(R.styleable.CustomAnalogClock_clockColor, Color.BLACK)
            centerColor =
                getColor(R.styleable.CustomAnalogClock_centerColor, Color.BLACK)
            divisionsColor =
                getColor(R.styleable.CustomAnalogClock_divisionsColor, Color.BLACK)
            numeralColor =
                getColor(R.styleable.CustomAnalogClock_numeralColor, Color.BLACK)
            hourHandColor =
                getColor(R.styleable.CustomAnalogClock_hourHandColor, Color.BLACK)
            minutesHandColor =
                getColor(R.styleable.CustomAnalogClock_minutesHandColor, Color.BLACK)
            secondsHandColor =
                getColor(R.styleable.CustomAnalogClock_secondsHandColor, Color.BLACK)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (!isInit) {
            initClock()
        }
        canvas?.let {
            it.drawColor(Color.TRANSPARENT)
            drawBackground(it)
            drawCircle(it)
            drawNumerals(it)
            if (isShowDivisions) drawDivisions(it)
            drawHands(it)
            drawCenter(it)
        }
        invalidate()
    }

    private fun initClock() {
        height = getHeight().toFloat()
        width = getWidth().toFloat()
        val minSide = height.coerceAtMost(width)
        padding = minSide / 15
        radius = minSide / 2 - padding
        fontSize = radius * 0.3f
        _paint = Paint()
        isInit = true
    }

    private fun drawBackground(canvas: Canvas) {
        with(paint) {
            reset()
            color = backgroundColor
            canvas.drawCircle(width / 2, height / 2, radius + padding / 2, this)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        with(paint) {
            reset()
            strokeWidth = radius / 20
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = clockColor
            canvas.drawCircle(width / 2, height / 2, radius + padding / 2, this)
        }
    }

    private fun drawNumerals(canvas: Canvas) {
        with(paint) {
            color = numeralColor
            textSize = fontSize
            strokeWidth = 5f
            style = Paint.Style.FILL
        }

        HOURS_RANGE.forEach { number ->
            val numAsString = number.toString()
            paint.getTextBounds(numAsString, 0, numAsString.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val point =
                handsHelper.getDivisionCoordinates(width, height, radius * 0.8f, angle)
            val x = point.x - rect.width() / 2
            val y = point.y + rect.height() / 2
            canvas.drawText(numAsString, x, y, paint)
        }
    }

    private fun drawHands(canvas: Canvas) {
        val currentTime = Time.getCurrentTime()
        with(currentTime) {
            drawHand(canvas, (hour + minute.toFloat() / 60) * 5f, HandType.HOURS)
            drawHand(canvas, minute.toFloat(), HandType.MINUTES)
            drawHand(canvas, second.toFloat(), HandType.SECONDS)
        }
    }

    private fun drawHand(canvas: Canvas, loc: Float, handType: HandType) {

        val handLength = when (handType) {
            HandType.HOURS -> {
                paint.strokeWidth = handsHelper.getHandThickness(radius, hourHandThicknessCoeff)
                paint.color = hourHandColor
                handsHelper.getHandLength(radius, hourHandLengthCoeff)
            }

            HandType.MINUTES -> {
                paint.strokeWidth = handsHelper.getHandThickness(radius, minutesHandThicknessCoeff)
                paint.color = minutesHandColor
                handsHelper.getHandLength(radius, minutesHandLengthCoeff)
            }

            HandType.SECONDS -> {
                paint.color = secondsHandColor
                paint.strokeWidth = handsHelper.getHandThickness(radius, secondsThicknessCoeff)
                handsHelper.getHandLength(radius, secondsLengthCoeff)
            }
        }

        val handCoordinates = handsHelper.getHandCoordinates(width, height, handLength, loc, false)
        val backHandCoordinates = handsHelper.getHandCoordinates(width, height, handLength / 4, loc, true)
        canvas.drawLine(
            handCoordinates.startPoint.x,
            handCoordinates.startPoint.y,
            handCoordinates.stopPoint.x,
            handCoordinates.stopPoint.y,
            paint
        )
        canvas.drawLine(
            backHandCoordinates.startPoint.x,
            backHandCoordinates.startPoint.y,
            backHandCoordinates.stopPoint.x,
            backHandCoordinates.stopPoint.y,
            paint
        )
    }

    private fun drawDivisions(canvas: Canvas) {
        with(paint) {
            textSize = fontSize
            color = divisionsColor
            strokeWidth = 5f
            style = Paint.Style.FILL
        }
        (MINUTES_RANGE).forEach { number ->
            val angle = Math.PI * number / 30 - Math.PI / 2
            val pointRadius = if (number % 5 == 0) {
                radius / 50
            } else {
                radius / 100
            }
            val point = handsHelper.getDivisionCoordinates(width, height, radius, angle)
            canvas.drawCircle(point.x, point.y, pointRadius, paint)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = centerColor
        paint.isAntiAlias = true
        canvas.drawCircle(width / 2, height / 2, radius / 25f, paint)
    }
}