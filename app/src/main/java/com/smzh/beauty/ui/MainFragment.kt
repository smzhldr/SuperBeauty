package com.smzh.beauty.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.smzh.beauty.facedetector.CommonUtils
import com.smzh.beauty.ui.EditActivity.Companion.PIC_PATH
import com.smzh.superbeauty.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.menu_item.view.*

class MainFragment : Fragment() {

    private val imageList: ArrayList<ImageView> by lazy {
        arrayListOf(
                ImageView(context).apply {
                    setImageResource(R.drawable.ic_yezi)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                },
                ImageView(context).apply {
                    setImageResource(R.drawable.face)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                },
                ImageView(context).apply {
                    setImageResource(R.drawable.face)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                },
                ImageView(context).apply {
                    setImageResource(R.drawable.face)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                },
                ImageView(context).apply {
                    setImageResource(R.drawable.face)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }

        )
    }

    private val menuList: ArrayList<MenuItem> = arrayListOf(
            MenuItem("发型", R.drawable.ic_hair, R.drawable.menu_hair),
            MenuItem("滤镜", R.drawable.ic_filter, R.drawable.menu_filter),
            MenuItem("换装", R.drawable.ic_clothing, R.drawable.menu_clothing),
            MenuItem("贴纸", R.drawable.ic_sticker, R.drawable.menu_sticker),
            MenuItem("调整", R.drawable.ic_adjust, R.drawable.menu_adjust),
            MenuItem("美颜", R.drawable.ic_beauty, R.drawable.menu_beauty)
            )

    private var pageIndex = 0

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewPager.adapter = object : PagerAdapter() {

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(imageList[position % imageList.size])
                return imageList[position % imageList.size]
            }

            override fun getCount(): Int {
                return Int.MAX_VALUE
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(imageList[position % imageList.size])
            }
        }

        mainMenuRv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        mainMenuRv.adapter = object : RecyclerView.Adapter<MenuViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
                itemView.layoutParams.width = (resources.displayMetrics.widthPixels - CommonUtils.dpToPx(context, 50f)) / 3
                return MenuViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
                holder.bind(menuList[position])
            }

            override fun getItemCount(): Int {
                return menuList.size
            }
        }

        editButton.setOnClickListener {
            if (checkPermission()) {
                choosePicture()
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        switchPage()
    }

    private fun switchPage() {
        if (isResumed) {
            handler.postDelayed({
                mainViewPager.currentItem  = pageIndex
                pageIndex++
                switchPage()
            }, 3000)
        }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + activity!!.packageName)
                startActivityForResult(intent, 666)
                return false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 666)
                return false
            }
        } else {
            return true
        }
    }

    private fun choosePicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 667)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        choosePicture()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == 666) {
                if (Environment.isExternalStorageManager()) {
                    choosePicture()
                } else {
                    Toast.makeText(activity, "未获取相册权限", Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == 667) {
                val uri = data!!.data
                val imgPath = CommonUtils.getRealPathFromUri(context, uri)
                if (imgPath == null) {
                    Toast.makeText(activity, "图片选择失败", Toast.LENGTH_SHORT).show()
                    return
                }
                val intent = Intent(activity, EditActivity::class.java)
                intent.putExtra(PIC_PATH, imgPath)
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MainFragment().apply {
                }
    }

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(menuItem: MenuItem) {
            itemView.run {
                menu_title.text = menuItem.name
                menu_icon.setImageResource(menuItem.icon)
                setBackgroundResource(menuItem.bg)
                setOnClickListener {  }
            }
        }
    }

    data class MenuItem(val name: String, val icon: Int, val bg: Int)
}