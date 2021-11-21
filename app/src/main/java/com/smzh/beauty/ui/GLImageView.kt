package com.smzh.beauty.ui

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLImageView :GLSurfaceView, GLSurfaceView.Renderer {

    constructor(context: Context?):super(context)

    constructor(context: Context?,attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10?) {

    }

}