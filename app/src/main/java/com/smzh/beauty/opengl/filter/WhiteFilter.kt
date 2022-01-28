package com.smzh.beauty.opengl.filter

import android.content.Context
import com.smzh.beauty.opengl.GLImageFilter

class WhiteFilter(ctx: Context) : GLImageFilter(ctx, 1, 1) {

    private var progress: Int = 50


    fun setProgress(progress: Int) {
        this.progress = progress
    }
}