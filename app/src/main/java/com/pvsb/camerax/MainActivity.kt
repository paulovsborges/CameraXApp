package com.pvsb.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pvsb.camerax.cameramanager.CameraInterface
import com.pvsb.camerax.cameramanager.CameraInterfaceImpl
import com.pvsb.camerax.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
    CameraInterface by CameraInterfaceImpl() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialCameraNeeds(this, binding.pvViewFinder)
        initialSetUp()
    }

    private fun initialSetUp() {
        if (allPermissionsGranted()) startCamera()
        else ActivityCompat.requestPermissions(this, neededPermissions, REQUEST_CODE_PERMISSIONS)

        binding.apply {
            btnCaptureImage.setOnClickListener {
                takePhoto()
            }
            btnCaptureVideo.setOnClickListener {
                captureVideo()
                handleRecordingBtnState()
            }
        }
    }

    private fun handleRecordingBtnState() {
        binding.btnCaptureVideo.apply {
            isEnabled = false

            if (isRecording) {
                text = getString(R.string.stop_capture)
                isEnabled = true
            } else {
                text = getString(R.string.start_capture)
                isEnabled = true
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return neededPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
        val neededPermissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}