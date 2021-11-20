package com.smzh.superbeauty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EditFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val picPath = it.getString(PIC_PATH)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
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