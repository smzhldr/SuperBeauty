package com.smzh.beauty.opengl

import android.content.Context

class OutScreenFilter(ctx: Context) : GLImageFilter(ctx) {

    init {
        setRendererOnScreen(true)
    }

    override fun onDrawArraysPre(frame: IFilter.Frame?) {
        frame?.run {
            if (textureHeight != outputHeight || textureWidth != outputWidth) {
                val srcRatio = textureHeight / textureWidth.toFloat()
                val dstRatio = outputHeight / outputWidth.toFloat()
                if (srcRatio > dstRatio) {
                    val ratio = dstRatio / srcRatio
                    cube[0] = -1f * ratio
                    cube[2] = 1f * ratio
                    cube[4] = -1f * ratio
                    cube[6] = 1f * ratio
                } else {
                    val ratio = srcRatio / dstRatio
                    cube[1] = 1f * ratio
                    cube[3] = 1f * ratio
                    cube[5] = -1f * ratio
                    cube[7] = -1f * ratio
                }
            }
        }
    }
}