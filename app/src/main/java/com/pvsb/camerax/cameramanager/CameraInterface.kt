package com.pvsb.camerax.cameramanager

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView

interface CameraInterface {
    val isRecording: Boolean
    fun initialCameraNeeds(activity: AppCompatActivity, viewFinder: PreviewView)
    fun startCamera()
    fun takePhoto()
    fun captureVideo()
}