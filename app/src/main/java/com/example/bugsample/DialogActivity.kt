package com.example.bugsample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.bugsample.databinding.ActivityDialogBinding
import java.io.File

class DialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDialogBinding
    private var imageUri: Uri? = null
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        handlePhotoResult(success)
    }
    private val galleryLauncher = registerForActivityResult(PickPhotoActivityContract()) {
        imageUri = it
        handlePhotoResult(true)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dialog)

        binding.cameraBtn.setOnClickListener {
            launchCamera()
        }

        binding.galleryBtn.setOnClickListener {
            launchGallery()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun launchCamera() {
        imageUri = FileProvider.getUriForFile(
            this,
            "com.example.bugsample.provider",
            createImageFile()
        )
        cameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun createImageFile(): File {
        val storageDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("temp_image", ".jpg", storageDir)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    // user granted permission
                    cameraLauncher.launch(imageUri)
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    // do something if user denied permission and set Don't ask again
                }
                else -> {
                    // do something if permission for camera denied
                    requestPermissions(Array(1) { CAMERA_SERVICE }, 0)
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun launchGallery() {
        galleryLauncher.launch("")
    }

    class PickPhotoActivityContract : ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, input: String) =
            Intent(
                Intent.ACTION_PICK
            ).apply { type = "image/*" }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            intent?.data?.takeIf { resultCode == Activity.RESULT_OK }

    }

    private fun handlePhotoResult(isSuccess: Boolean) {
        when (isSuccess) {
            true -> {
                val data = Intent()
                data.putExtra("uri", imageUri.toString())
                setResult(RESULT_OK, data)
                this.finish()
            }
        }
    }
}