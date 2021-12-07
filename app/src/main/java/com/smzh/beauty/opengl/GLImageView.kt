package com.smzh.beauty.opengl

import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GLImageView : GLSurfaceView {

    private var renderer: GLRenderer? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        if (renderer == null) {
            renderer = GLRenderer()
            setEGLContextClientVersion(3)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            holder.setFormat(PixelFormat.RGBA_8888)
            setRenderer(renderer)
            renderMode = RENDERMODE_WHEN_DIRTY
        }
    }

    fun setSource(source: ISource) {
        renderer?.let {
            it.source = source
            requestRender()
        }
    }

    fun adjustImageSize(width: Int, height: Int) {
        renderer?.screenFilter?.setOutSize(width, height)
        requestRender()
    }

    inner class GLRenderer internal constructor() : Renderer {
        private var iSource: ISource? = null
        val screenFilter by lazy { OutScreenFilter(context) }
        private val runOnDraw: Queue<Runnable> by lazy { LinkedList<Runnable>() }
        private val runOnDrawEnd: Queue<Runnable> by lazy { LinkedList<Runnable>() }
        private var filter: IFilter? = null
        private var disable = false
        private var outWidth = 0
        private var outHeight = 0

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            outWidth = width
            outHeight = height
            screenFilter.setOutSize(width, height)
        }

        override fun onDrawFrame(gl: GL10) {
            GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
            runAll(runOnDraw)
            iSource?.run {
                val frame: IFilter.Frame? = createFrame()
                frame?.let {
                    filter?.run {
                        if (!disable) {
                            draw(it)
                        }
                    }
                    screenFilter.draw(it)
                }
            }
            runAll(runOnDrawEnd)
        }

        fun clear() {
            iSource?.destroy()
            iSource = null
            screenFilter.destroy()
        }

        fun setFilter(filter: IFilter) {
            runOnDraw(Runnable {
                val oldFilter: IFilter? = this@GLRenderer.filter
                this@GLRenderer.filter = filter
                oldFilter?.destroy()
            })
        }

        private fun runAll(queue: Queue<Runnable>) {
            synchronized(queue) {
                while (!queue.isEmpty()) {
                    queue.poll().run()
                }
            }
        }

        private fun runOnDraw(runnable: Runnable) {
            synchronized(runOnDraw) { runOnDraw.add(runnable) }
        }

        protected fun runOnDrawEnd(runnable: Runnable?) {
            synchronized(runOnDrawEnd) { runOnDrawEnd.add(runnable) }
        }

        fun setDisable(disable: Boolean) {
            this.disable = disable
        }

        var source: ISource?
            get() = iSource
            set(iSource) {
                this.iSource = iSource
            }

    }

}