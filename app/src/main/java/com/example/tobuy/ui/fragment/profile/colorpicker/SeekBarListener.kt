package com.example.tobuy.ui.fragment.profile.colorpicker

import android.widget.SeekBar

class SeekBarListener(
    private val onProgressChange: (Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onProgressChange(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // Nothing to do
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // Nothing to do
    }
}