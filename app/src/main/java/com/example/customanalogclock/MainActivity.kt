package com.example.customanalogclock

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customanalogclock.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            changeStyle()
        }
    }

    private fun changeStyle() {
        binding.firstClock.secondsHandColor = getRandomColor()
        binding.firstClock.backgroundClockColor = getRandomColor()
        binding.firstClock.minutesHandThicknessCoeff = 5
        binding.firstClock.minutesHandLengthCoeff = 10
        binding.firstClock.hourHandLengthCoeff = 10

        binding.secondClock.clockColor = getRandomColor()
        binding.secondClock.backgroundClockColor = getRandomColor()
        binding.secondClock.numeralColor = getRandomColor()
        binding.secondClock.changeHandsColor(getRandomColor())
        binding.secondClock.divisionsColor = getRandomColor()
        binding.secondClock.centerColor = getRandomColor()
    }

    private fun getRandomColor(): Int {
        val random = Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }
}