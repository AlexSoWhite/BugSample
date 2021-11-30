package com.example.bugsample

import android.os.Bundle
import androidx.fragment.app.commit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bugsample.databinding.PhotoFragmentBinding

class PhotoFragment : Fragment(R.layout.photo_fragment) {

    private lateinit var binding: PhotoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = PhotoFragmentBinding.inflate(inflater)
        childFragmentManager.commit {
            replace(R.id.button_container, ButtonFragment.newInstance(binding))
        }
        return binding.root
    }
}