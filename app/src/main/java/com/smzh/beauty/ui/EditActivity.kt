package com.smzh.beauty.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.smzh.superbeauty.R

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_edit)
        intent?.apply {
            val path = getStringExtra("PIC_PATH")
            supportFragmentManager.beginTransaction()
                    .add(R.id.editContainer, EditFragment.newInstance(path!!))
                    .commit()
        }

    }
}
