package com.example.tobuy.ui.fragment.profile.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.tobuy.databinding.FragmentColorPickerBinding
import com.example.tobuy.prefs.Prefs
import com.example.tobuy.ui.fragment.base.BaseFragment
import com.example.tobuy.ui.fragment.profile.colorpicker.vm.ColorPickerViewModel
import java.util.*

class ColorPickerFragment : BaseFragment() {

    private var _binding: FragmentColorPickerBinding? = null
    private val binding by lazy { _binding!! }

    private val safeArgs: ColorPickerFragmentArgs by navArgs()
    private val selectPriority by lazy { safeArgs.priorityName }

    private val colorPickerViewModel: ColorPickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorPickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPriorityName()
        initViews()
        setupObservers()
        setOnClickListeners()

    }

    private fun setOnClickListeners() {

        binding.apply {

            saveButton.setOnClickListener {

                val viewState =
                    colorPickerViewModel.viewStateLiveData.value ?: return@setOnClickListener

                val color = currentRGB(viewState)

                when (selectPriority.lowercase(Locale.US)) {
                    "low" -> Prefs.setLowPriorityColor(color)
                    "medium" -> Prefs.setMediumPriorityColor(color)
                    "high" -> Prefs.setHighPriorityColor(color)
                }

            }
        }
    }

    private fun setPriorityName() {
        colorPickerViewModel.setPriorityName(selectPriority, ::colorsCallback)
    }

    private fun initViews() {

        binding.apply {

            redColorLayout.apply {
                textView.text = "Red"
                seekBar.setOnSeekBarChangeListener(SeekBarListener(colorPickerViewModel::onRedChange))
            }

            greenColorLayout.apply {
                textView.text = "Green"
                seekBar.setOnSeekBarChangeListener(SeekBarListener(colorPickerViewModel::onGreenChange))
            }

            blueColorLayout.apply {
                textView.text = "Blue"
                seekBar.setOnSeekBarChangeListener(SeekBarListener(colorPickerViewModel::onBlueChange))
            }
        }
    }

    private fun setupObservers() {

        colorPickerViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->

            binding.titleTextView.text = viewState.getFormattedTitle()

            val color = currentRGB(viewState)

            binding.colorView.setBackgroundColor(color)

        }
    }

    private fun currentRGB(viewState: ColorPickerViewModel.ViewState) =
        Color.rgb(viewState.red, viewState.green, viewState.blue)

    private fun colorsCallback(red: Int, green: Int, blue: Int) {

        binding.apply {
            redColorLayout.seekBar.progress = red
            greenColorLayout.seekBar.progress = green
            blueColorLayout.seekBar.progress = blue
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}