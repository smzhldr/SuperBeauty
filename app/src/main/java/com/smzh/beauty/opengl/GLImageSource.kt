package com.smzh.beauty.opengl

import android.graphics.Bitmap

class GLImageSource(override val width: Int, override val height: Int) :ISource {

    private var bitmap: Bitmap? = null
    private var textureId = -1

    override fun createFrame(): IFilter.Frame? {
        bitmap?.run {
            textureId = OpenGlUtils.loadTexture(this, OpenGlUtils.NO_TEXTURE)
            bitmap = null
        }
        if (textureId < 0) {
            return null
        }
        return IFilter.Frame(0, textureId, width, height)
    }

    override fun destroy() {

    }

    fun setBitmap(bitmap: Bitmap) {
       this.bitmap = bitmap
    }
}