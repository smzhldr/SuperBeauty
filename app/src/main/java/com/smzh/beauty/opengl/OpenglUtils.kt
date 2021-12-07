package com.smzh.beauty.opengl

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object OpenGlUtils {
    const val NO_TEXTURE = -1

    @JvmOverloads
    fun loadTexture(img: Bitmap, usedTexId: Int, recycle: Boolean = false): Int {
        val textures = IntArray(1)
        if (usedTexId == NO_TEXTURE) {
            GLES30.glGenTextures(1, textures, 0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0])
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
                    GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR.toFloat())
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
                    GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR.toFloat())
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
                    GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE.toFloat())
            GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
                    GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE.toFloat())
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, img, 0)
        } else {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, usedTexId)
            GLUtils.texSubImage2D(GLES30.GL_TEXTURE_2D, 0, 0, 0, img)
            textures[0] = usedTexId
        }
        if (recycle) {
            img.recycle()
        }
        return textures[0]
    }

    fun loadShader(strSource: String, iType: Int): Int {
        val compiled = IntArray(1)
        val iShader = GLES30.glCreateShader(iType)
        GLES30.glShaderSource(iShader, strSource)
        GLES30.glCompileShader(iShader)
        GLES30.glGetShaderiv(iShader, GLES30.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.d("Load Shader Failed", "Compilation\n ${GLES30.glGetShaderInfoLog(iShader)}")
            return 0
        }
        return iShader
    }

    fun loadProgram(context: Context, vertexId: Int, fragmentId: Int): Int {
        return loadProgram(readGlShader(context, vertexId), readGlShader(context, fragmentId))
    }

    fun loadProgram(strVSource: String, strFSource: String): Int {
        val link = IntArray(1)
        val iVShader: Int = loadShader(strVSource, GLES30.GL_VERTEX_SHADER)
        if (iVShader == 0) {
            Log.d("Load Program", "Vertex Shader Failed")
            return 0
        }
        val iFShader: Int = loadShader(strFSource, GLES30.GL_FRAGMENT_SHADER)
        if (iFShader == 0) {
            Log.d("Load Program", "Fragment Shader Failed")
            return 0
        }
        val iProgId: Int = GLES30.glCreateProgram()
        GLES30.glAttachShader(iProgId, iVShader)
        GLES30.glAttachShader(iProgId, iFShader)
        GLES30.glLinkProgram(iProgId)
        GLES30.glGetProgramiv(iProgId, GLES30.GL_LINK_STATUS, link, 0)
        if (link[0] <= 0) {
            Log.d("Load Program", "Linking Failed")
            return 0
        }
        GLES30.glDeleteShader(iVShader)
        GLES30.glDeleteShader(iFShader)
        return iProgId
    }

    fun getShowMatrix(matrix: FloatArray?, imgWidth: Int, imgHeight: Int, viewWidth: Int, viewHeight: Int) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (sWhImg > sWhView) {
                Matrix.orthoM(projection, 0, -sWhView / sWhImg, sWhView / sWhImg, -1f, 1f, 1f, 3f)
            } else {
                Matrix.orthoM(projection, 0, -1f, 1f, -sWhImg / sWhView, sWhImg / sWhView, 1f, 3f)
            }
            Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun rotate(m: FloatArray, angle: Float): FloatArray {
        Matrix.rotateM(m, 0, angle, 0f, 0f, 1f)
        return m
    }

    fun flip(m: FloatArray, x: Boolean, y: Boolean): FloatArray {
        if (x || y) {
            Matrix.scaleM(m, 0, if (x) (-1).toFloat() else 1.toFloat(), if (y) (-1).toFloat() else 1.toFloat(), 1f)
        }
        return m
    }

    fun readGlShader(context: Context, resourceId: Int): String {
        val builder = StringBuilder()
        val inputStream = context.resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(inputStream)
        val reader = BufferedReader(inputStreamReader)
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
                builder.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}