package com.smzh.beauty.opengl

import android.content.Context
import android.opengl.GLES30
import android.util.Size
import com.smzh.beauty.opengl.IFilter.FrameBufferProvider.createFrameBuffers
import com.smzh.beauty.opengl.IFilter.FrameBufferProvider.destroyFrameBuffers
import com.smzh.beauty.opengl.OpenGlUtils.loadProgram
import com.smzh.superbeauty.R
import java.nio.ByteBuffer
import java.nio.ByteOrder


abstract class GLImageFilter @JvmOverloads constructor(protected var context: Context?, private val vertexResId: Int = R.raw.single_input_v, private val fragmentResId: Int = R.raw.texture_f) : IFilter {
    protected var glProgramId = 0
    protected var glAttrPosition = 0
    protected var glAttrTextureCoordinate = 0
    protected var glUniformTexture = 0
    protected var selfFrame: IFilter.Frame? = null
    var outputWidth = 0
    var outputHeight = 0
    private var outSize: Size? = null
    private var hasInit = false
    private var isRendererScreen = false
    private val cubeBuffer = ByteBuffer.allocateDirect(8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
    private val textureBuffer = ByteBuffer.allocateDirect(8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
    protected val cube: FloatArray = floatArrayOf(-1.0f, 1.0f,
            1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f)
    protected val textureCords: FloatArray = floatArrayOf(0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f)

    private fun init() {
        if (!hasInit) {
            glProgramId = loadProgram(context!!, vertexResId, fragmentResId)
            glAttrPosition = GLES30.glGetAttribLocation(glProgramId, "position")
            glAttrTextureCoordinate = GLES30.glGetAttribLocation(glProgramId, "inputTextureCoordinate")
            glUniformTexture = GLES30.glGetUniformLocation(glProgramId, "inputImageTexture")
            onInit()
            hasInit = true
        }
    }

    protected fun onInit() {}

    override fun draw(frame: IFilter.Frame): IFilter.Frame {
        init()
        if (glProgramId <= 0) {
            return frame
        }
        val textureWidth = frame.textureWidth
        val textureHeight = frame.textureHeight
        if (isRendererScreen) {
            updateFrame(outSize!!.width, outSize!!.height)
        } else {
            updateFrame(textureWidth, textureHeight)
        }
        bindFrameBuffer()
        GLES30.glUseProgram(glProgramId)
        onDrawArraysPre(frame)
        cubeBuffer.clear()
        cubeBuffer.put(cube).position(0)
        GLES30.glVertexAttribPointer(glAttrPosition, 2, GLES30.GL_FLOAT, false, 0, cubeBuffer)
        GLES30.glEnableVertexAttribArray(glAttrPosition)
        textureBuffer.clear()
        textureBuffer.put(textureCords).position(0)
        GLES30.glVertexAttribPointer(glAttrTextureCoordinate, 2, GLES30.GL_FLOAT, false, 0, textureBuffer)
        GLES30.glEnableVertexAttribArray(glAttrTextureCoordinate)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        bindTexture(frame.textureId)
        GLES30.glUniform1i(glUniformTexture, 1)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
        GLES30.glDisableVertexAttribArray(glAttrPosition)
        GLES30.glDisableVertexAttribArray(glAttrTextureCoordinate)
        onDrawArraysAfter(frame)
        bindTexture(0)
        return if (selfFrame == null) {
            IFilter.Frame(0, 0, 0, 0)
        } else {
            selfFrame!!
        }
    }

    open fun onDrawArraysPre(frame: IFilter.Frame?) {}

    protected fun onDrawArraysAfter(frame: IFilter.Frame?) {}

    protected fun bindTexture(textureId: Int) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId)
    }

    fun setTextureCoordination(coordination: FloatArray) {
        for (index in coordination.indices) {
            textureCords[index] = coordination[index]
        }
    }

    fun setOutSize(outputWidth: Int, outputHeight: Int) {
        outSize = Size(outputWidth, outputHeight)
    }

    fun setVerticesCoordination(vertices: FloatArray) {
        for (index in vertices.indices) {
            cube[index] = vertices[index]
        }
    }

    private fun updateFrame(width: Int, height: Int) {
        if (width != outputWidth || height != outputHeight) {
            outputWidth = width
            outputHeight = height
            selfFrame?.let {
                destroyFrameBuffers(it)
            }
            selfFrame = createFrameBuffers(outputWidth, outputHeight)
        }
    }

    private fun bindFrameBuffer() {
        if (isRendererScreen) {
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
        } else {
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, selfFrame!!.frameBuffer)
        }
        GLES30.glViewport(0, 0, outputWidth, outputHeight)
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
    }

    fun setRendererOnScreen(isRendererScreen: Boolean) {
        this.isRendererScreen = isRendererScreen
    }

    override fun destroy() {
        hasInit = false
        selfFrame?.let {
            destroyFrameBuffers(it)
        }
        selfFrame = null
        outputWidth = 0
        outputHeight = 0
    }

    companion object {

        val VERTICES: FloatArray = floatArrayOf(-1.0f, 1.0f,
                1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, -1.0f)
        val TEXTURECOOED: FloatArray = floatArrayOf(0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f)

    }

}