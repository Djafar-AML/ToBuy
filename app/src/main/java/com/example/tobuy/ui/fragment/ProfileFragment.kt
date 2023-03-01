package com.example.tobuy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.databinding.FragmentProfileBinding
import com.example.tobuy.ui.fragment.base.BaseFragment

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding by lazy { _binding!! }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}