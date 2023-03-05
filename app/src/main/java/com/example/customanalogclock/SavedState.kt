package com.example.customanalogclock

import android.graphics.Color
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.annotation.RequiresApi

class SavedState : View.BaseSavedState {

    var backgroundClockColorState = Color.WHITE
    var clockColorState = Color.BLACK
    var centerColorState = Color.BLACK
    var numeralColorState = Color.BLACK

    var divisionsColorState = Color.BLACK
    var isShowDivisionsState: Boolean = true

    var hourHandColorState = Color.BLACK
    var hourHandThicknessCoeffState: Int = 0
    var hourHandLengthCoeffState: Int = 0

    var minutesHandColorState = Color.BLACK
    var minutesHandThicknessCoeffState: Int = 0
    var minutesHandLengthCoeffState: Int = 0

    var secondsHandColorState = Color.BLACK
    var secondsThicknessCoeffState: Int = 0
    var secondsLengthCoeffState: Int = 0

    constructor(superState: Parcelable) : super(superState)

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : super(parcel) {

        with(parcel) {
            backgroundClockColorState = readInt()
            clockColorState = readInt()
            centerColorState = readInt()
            numeralColorState = readInt()
            divisionsColorState = readInt()
            isShowDivisionsState = readBoolean()

            hourHandColorState = readInt()
            hourHandThicknessCoeffState = readInt()
            hourHandLengthCoeffState = readInt()
            minutesHandColorState = readInt()
            minutesHandThicknessCoeffState = readInt()
            minutesHandLengthCoeffState = readInt()
            secondsHandColorState = readInt()
            secondsThicknessCoeffState = readInt()
            secondsLengthCoeffState = readInt()
        }
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
         out.writeInt(backgroundClockColorState)
         out.writeInt(clockColorState)
         out.writeInt(centerColorState)
         out.writeInt(numeralColorState)
         out.writeInt(divisionsColorState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            out.writeBoolean(isShowDivisionsState)
        }
        out.writeInt(hourHandColorState)
         out.writeInt(hourHandThicknessCoeffState)
         out.writeInt(hourHandLengthCoeffState)
         out.writeInt(minutesHandColorState)
         out.writeInt(minutesHandThicknessCoeffState)
         out.writeInt(minutesHandLengthCoeffState)
         out.writeInt(secondsHandColorState)
         out.writeInt(secondsThicknessCoeffState)
         out.writeInt(secondsLengthCoeffState)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun createFromParcel(source: Parcel): SavedState {
                return SavedState(source)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return Array(size) { null }
            }
        }
    }
}