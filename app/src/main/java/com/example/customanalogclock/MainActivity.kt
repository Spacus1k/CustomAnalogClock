package com.example.customanalogclock

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customanalogclock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener{
            binding.firstClock.secondsHandColor = Color.RED
            binding.firstClock.backgroundClockColor = Color.DKGRAY
            binding.firstClock.minutesHandThicknessCoeff = 5
            binding.firstClock.minutesHandLengthCoeff = 10
            binding.firstClock.hourHandLengthCoeff = 10

            binding.secondClock.clockColor = Color.BLACK
            binding.secondClock.backgroundClockColor = Color.DKGRAY
            binding.secondClock.numeralColor = Color.WHITE
            binding.secondClock.changeHandsColor(Color.WHITE)
            binding.secondClock.divisionsColor = Color.WHITE
            binding.secondClock.centerColor = Color.WHITE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}