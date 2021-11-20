package com.smzh.beauty.ui

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smzh.beauty.facedetector.CommonUtils
import com.smzh.beauty.facedetector.FaceDetector
import com.smzh.superbeauty.R
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditFragment : Fragment() {

    private lateinit var srcBitmap: Bitmap
    private val faceDetect = FaceDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val picPath = it.getString(PIC_PATH)
        }
        faceDetect.createFaceDetector(CommonUtils.getFaceShape68ModelPath(activity))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        srcBitmap = BitmapFactory.decodeResource(resources, R.drawable.face_photo);
        val width: Double = resources.displayMetrics.widthPixels.toDouble()
        val height: Double = ((srcBitmap.height).toDouble() / srcBitmap.width * width)
        srcBitmap = CommonUtils.scaleWithWH(srcBitmap, width, height)
        imageView.setImageBitmap(srcBitmap)
        button.setOnClickListener {
            callFaceLandmark()
        }

    }

    private fun callFaceLandmark() {
        GlobalScope.launch(Dispatchers.IO) {
            val faces = faceDetect.detectImage(CommonUtils.bitmap2bytes(srcBitmap), 0, srcBitmap.width, srcBitmap.height)
            GlobalScope.launch(Dispatchers.Main) {
                val canvas = Canvas(srcBitmap)
                val paint = Paint()
                paint.color = Color.RED
                paint.strokeWidth = 7f
                val numPaint = Paint()
                numPaint.color = Color.GREEN
                numPaint.strokeWidth = 20f
                numPaint.textSize = 20f
                for (face in faces) {
                    val facePoint = face.facePoints
                    for ((j, point) in facePoint.withIndex()) {
                        canvas.drawPoint(point.x, point.y, paint)
                        canvas.drawText(j.toString(), point.x - 10, point.y + 20, numPaint);
                    }
                }
                imageView.setImageBitmap(srcBitmap);
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        faceDetect.destroyFaceDetector()
    }


    companion object {

        const val PIC_PATH = "pic_path"

        @JvmStatic
        fun newInstance(path: String) =
                EditFragment().apply {
                    arguments = Bundle().apply {
                        putString(PIC_PATH, path)
                    }
                }
    }
}