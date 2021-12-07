package com.smzh.beauty.opengl

import android.opengl.GLES30


interface IFilter {

    fun draw(frame: Frame): Frame

    fun destroy()

    object FrameBufferProvider {
        fun createFrameBuffers(textureWidth: Int, textureHeight: Int): Frame {
            val frameBuffer = IntArray(1)
            val frameBufferTexture = IntArray(1)
            GLES30.glGenFramebuffers(1, frameBuffer, 0)
            GLES30.glGenTextures(1, frameBufferTexture, 0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, frameBufferTexture[0])
            GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, textureWidth, textureHeight, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer[0])
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameBufferTexture[0], 0)
            return Frame(frameBuffer[0], frameBufferTexture[0], textureWidth, textureHeight)
        }

        fun destroyFrameBuffers(frameBuffer: Frame) {
            val frameBufferTextures = intArrayOf(frameBuffer.textureId)
            GLES30.glDeleteTextures(frameBufferTextures.size, frameBufferTextures, 0)
            val frameBuffers = intArrayOf(frameBuffer.frameBuffer)
            GLES30.glDeleteFramebuffers(frameBuffers.size, frameBuffers, 0)
        }
    }

    class Frame(val frameBuffer: Int, val textureId: Int, val textureWidth: Int, val textureHeight: Int)
}