package com.example.tobuy.ui.fragment.profile.colorpicker.vm

import android.graphics.Color
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tobuy.extensions.asLiveData
import com.example.tobuy.prefs.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ColorPickerViewModel @Inject constructor() : ViewModel() {

    data class ViewState(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0,
        private val priorityName: String = ""
    ) {
        fun getFormattedTitle(): String {
            return "$priorityName ($red, $green, $blue)"
        }
    }

    private var blueValue: Int = 0
    private var greenValue: Int = 0
    private var redValue: Int = 0

    private lateinit var priorityName: String

    private val _viewStateLiveData = MutableLiveData<ViewState>()
    val viewStateLiveData = _viewStateLiveData.asLiveData()

    fun setPriorityName(_priorityName: String, colorCallback: (Int, Int, Int) -> Unit) {

        priorityName = _priorityName

        val colorInt = colorIntBasedOnPriorityName(priorityName)

        initRGBBasedOnColorInt(colorInt)

        colorCallback(redValue, greenValue, blueValue)

        _viewStateLiveData.postValue(
            ViewState(
                red = redValue,
                green = greenValue,
                blue = blueValue,
                priorityName = priorityName
            )
        )
    }


    private fun colorIntBasedOnPriorityName(priorityName: String) =
        when (priorityName.lowercase(Locale.US)) {
            "low" -> Prefs.getLowPriorityColor()
            "medium" -> Prefs.getMediumPriorityColor()
            "high" -> Prefs.getHighPriorityColor()
            else -> 0
        }

    private fun initRGBBasedOnColorInt(colorInt: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val color = Color.valueOf(colorInt)
            redValue = (color.red() * 255).roundToInt()
            greenValue = (color.green() * 255).roundToInt()
            blueValue = (color.blue() * 255).roundToInt()
        } else {
            redValue = Color.red(colorInt) / 255
            greenValue = Color.green(colorInt) / 255
            blueValue = Color.blue(colorInt) / 255
        }
    }

    fun onRedChange(red: Int) {
        val viewState = _viewStateLiveData.value ?: ViewState()
        _viewStateLiveData.postValue(viewState.copy(red = red))
    }

    fun onGreenChange(green: Int) {
        val viewState = _viewStateLiveData.value ?: ViewState()
        _viewStateLiveData.postValue(viewState.copy(green = green))
    }

    fun onBlueChange(blue: Int) {
        val viewState = _viewStateLiveData.value ?: ViewState()
        _viewStateLiveData.postValue(viewState.copy(blue = blue))
    }
}