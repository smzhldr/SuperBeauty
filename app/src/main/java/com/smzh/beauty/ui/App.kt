package com.smzh.beauty.ui

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.smzh.beauty.facedetector.CommonUtils
import com.smzh.beauty.facedetector.FaceDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        init(this)
    }


    private fun init(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            CommonUtils.copyModeToSDCard(context)
//            launch(Dispatchers.Main) {
//                Toast.makeText(context, "资源加载完成", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}