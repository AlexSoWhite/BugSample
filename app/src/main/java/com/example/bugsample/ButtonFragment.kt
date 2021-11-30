package com.example.bugsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.bugsample.databinding.ButtonFragmentBinding
import com.example.bugsample.databinding.PhotoFragmentBinding

class ButtonFragment : Fragment(R.layout.button_fragment) {

    companion object {
        private lateinit var rootBinding: PhotoFragmentBinding

        fun newInstance(
            rootBinding: PhotoFragmentBinding
        ): ButtonFragment {
            this.rootBinding = rootBinding
            return ButtonFragment()
        }
    }

    private lateinit var binding: ButtonFragmentBinding
    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.getStringExtra("uri")?.toUri()
                Toast.makeText(
                    activity,
                    "uri"+uri.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                rootBinding.photo.setImageURI(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = ButtonFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edit.setOnClickListener {
            takePhotoLauncher.launch(Intent(activity, DialogActivity::class.java))
        }
    }
}