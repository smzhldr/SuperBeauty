package com.smzh.beauty.opengl

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF

class OutScreenFilter(ctx: Context) : GLImageFilter(ctx) {

    var imageRect = RectF()
    private var imageMatrix = Matrix()

    init {
        setRendererOnScreen(true)
        val array = floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
        imageMatrix.setValues(array)
    }

    fun setImageMatrix(matrix: Matrix) {
        this.imageMatrix = matrix
    }


    override fun onDrawArraysPre(frame: IFilter.Frame?) {
        frame?.run {
            if (textureHeight != outputHeight || textureWidth != outputWidth) {
                val result = FloatArray(8)
                System.arraycopy(VERTICES, 0, result, 0, VERTICES.size)

//                val ratio1: Float = outputWidth.toFloat() / textureWidth
//                val ratio2: Float = outputHeight.toFloat() / textureHeight
//                val selectedRatio = Math.min(ratio1, ratio2)
//                val width: Float = textureWidth * selectedRatio
//                val height: Float = textureHeight * selectedRatio
//                imageRect.set((outputWidth - width) / 2, (outputHeight - height) / 2, (outputWidth + width) / 2, (outputHeight + height) / 2)
//                imageMatrix.mapRect(imageRect)
//
//                val cube0: Float = result[0]
//                val cube1: Float = result[1]
//                val cube2: Float = result[2]
//                val cube3: Float = result[3]
//                val cube4: Float = result[4]
//                val cube5: Float = result[5]
//                val cube6: Float = result[6]
//                val cube7: Float = result[7]
//
//                var ratioVertiecs: Float = imageRect.left / outputWidth
//                result[0] = result[0] + (cube2 - cube0) * ratioVertiecs
//                result[1] = result[1] + (cube3 - cube1) * ratioVertiecs
//                result[4] = result[4] + (cube6 - cube4) * ratioVertiecs
//                result[5] = result[5] + (cube7 - cube5) * ratioVertiecs
//
//                ratioVertiecs = (outputWidth - imageRect.right) / outputWidth
//                result[2] = result[2] + (cube0 - cube2) * ratioVertiecs
//                result[3] = result[3] + (cube1 - cube3) * ratioVertiecs
//                result[6] = result[6] + (cube4 - cube6) * ratioVertiecs
//                result[7] = result[7] + (cube5 - cube7) * ratioVertiecs
//
//                ratioVertiecs = imageRect.top / outputHeight
//                result[4] = result[4] + (cube0 - cube4) * ratioVertiecs
//                result[5] = result[5] + (cube1 - cube5) * ratioVertiecs
//                result[6] = result[6] + (cube2 - cube6) * ratioVertiecs
//                result[7] = result[7] + (cube3 - cube7) * ratioVertiecs
//
//                ratioVertiecs = (outputHeight - imageRect.bottom) / outputHeight
//                result[0] = result.get(0) + (cube4 - cube0) * ratioVertiecs
//                result[1] = result.get(1) + (cube5 - cube1) * ratioVertiecs
//                result[2] = result.get(2) + (cube6 - cube2) * ratioVertiecs
//                result[3] = result.get(3) + (cube7 - cube3) * ratioVertiecs
//                setVerticesCoordination(result)

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