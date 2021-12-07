package com.smzh.beauty.ui

import android.graphics.*
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smzh.beauty.facedetector.CommonUtils
import com.smzh.beauty.facedetector.Face
import com.smzh.beauty.facedetector.FaceDetector
import com.smzh.beauty.opengl.GLImageSource
import com.smzh.beauty.ui.EditActivity.Companion.PIC_PATH
import com.smzh.superbeauty.R
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditFragment : Fragment(), View.OnClickListener {

    private lateinit var glImageSource: GLImageSource
    private var path: String? = null

    private val beautyList = arrayListOf(
            BeautyMenuItem(0, "美白"),
            BeautyMenuItem(1, "磨皮"),
            BeautyMenuItem(2, "大眼"),
            BeautyMenuItem(3, "瘦脸"),
            BeautyMenuItem(4, "小脸"),
            BeautyMenuItem(5, "窄脸"),
            BeautyMenuItem(6, "小嘴"),
            BeautyMenuItem(7, "厚嘴"),
            BeautyMenuItem(8, "厚嘴"),
            BeautyMenuItem(9, "隆鼻"),
            BeautyMenuItem(10, "两眼"),
            BeautyMenuItem(11, "洁牙")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            path = it.getString(PIC_PATH)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backView.setOnClickListener(this)
        shareButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)

        val adapterMenu = MenuAdapter()
        menu_bar.run {
            adapter = adapterMenu
            adapterMenu.setData(beautyList as ArrayList<Any>, MenuType.Beauty)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }


        val srcBitmap = BitmapFactory.decodeFile(path!!)
        val width: Double = resources.displayMetrics.widthPixels.toDouble()
        val height: Double = ((srcBitmap.height).toDouble() / srcBitmap.width * width)
        glImageSource = GLImageSource(width.toInt(), height.toInt())
        glImageSource.setBitmap(srcBitmap)
        glImageView.setSource(glImageSource)


//        GlobalScope.launch(Dispatchers.IO) {
//            val faceDetector: FaceDetector = FaceDetector()
//            faceDetector.createFaceDetector(CommonUtils.getFaceShape68ModelPath(activity))
//            val faces = faceDetector.detectImage(CommonUtils.bitmap2bytes(srcBitmap), 0, srcBitmap.width, srcBitmap.height)
//            faceDetector.destroyFaceDetector()
//        }

    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.backView -> {
                    activity?.finish()
                }
                R.id.shareButton -> {
                }
                R.id.saveButton -> {
                }
                else -> {
                }
            }
        }
    }


    @Suppress("unused")
    private fun drawFacePoints(faces: Array<Face>) {
        val srcBitmap = BitmapFactory.decodeFile(path!!)
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
//        imageView.setImageBitmap(srcBitmap);
    }

    enum class MenuType {
        Beauty,
        Filter,
        Edit,
        Adjust,
        Sticer
    }

    class MenuAdapter : RecyclerView.Adapter<MenuViewHolder>() {

        private var items = ArrayList<Any>()
        private var type = MenuType.Beauty
        var selectIndex = 0;
        var menuViewHolder: MenuViewHolder? = null

        fun setData(items: ArrayList<Any>, type: MenuType) {
            this.items = items
            this.type = type
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val view = TextView(parent.context)
            view.gravity = Gravity.CENTER
            view.width = parent.context.resources.displayMetrics.widthPixels / 7
            view.setTextColor(Color.GRAY)
            view.height = CommonUtils.dpToPx(parent.context, 60f)
            return MenuViewHolder(view)
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            holder.bind(type, items[position], position, this)

        }

        override fun getItemCount(): Int {
            return items.count()
        }

    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(type: MenuType, menuItem: Any, position: Int, parent: MenuAdapter) {
            if (menuItem is BeautyMenuItem) {
                (itemView as TextView).text = menuItem.name
            }
            if (position == parent.selectIndex) {
                (itemView as TextView).setTextColor(Color.BLACK)
            } else {
                (itemView as TextView).setTextColor(Color.GRAY)
            }
            itemView.setOnClickListener {
                parent.selectIndex = position
                parent.menuViewHolder?.run {
                    (parent.menuViewHolder!!.itemView as TextView).setTextColor(Color.GRAY)
                    parent.menuViewHolder = this@MenuViewHolder
                    (parent.menuViewHolder!!.itemView as TextView).setTextColor(Color.BLACK)
                }
                parent.notifyDataSetChanged()
            }
        }
    }


    companion object {


        @JvmStatic
        fun newInstance(path: String) =
                EditFragment().apply {
                    arguments = Bundle().apply {
                        putString(PIC_PATH, path)
                    }
                }
    }

    data class BeautyMenuItem(val index: Int, val name: String)
}