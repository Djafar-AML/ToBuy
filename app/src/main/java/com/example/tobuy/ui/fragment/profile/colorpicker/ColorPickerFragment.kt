package com.example.tobuy.ui.fragment.profile.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.tobuy.databinding.FragmentColorPickerBinding
import com.example.tobuy.ui.fragment.base.BaseFragment
import com.example.tobuy.ui.fragment.profile.colorpicker.vm.ColorPickerViewModel

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

    }

    private fun setPriorityName() {
        colorPickerViewModel.setPriorityName(selectPriority) { r, g, b ->

        }
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

            val color = Color.rgb(viewState.red, viewState.green, viewState.blue)

            binding.colorView.setBackgroundColor(color)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}