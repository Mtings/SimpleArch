package com.song.sakura.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.action.ClickAction
import com.ui.widget.flowlayout.FlowLayout
import com.ui.widget.flowlayout.TagAdapter
import com.ui.widget.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : IBaseActivity<IBaseViewModel>(), ClickAction {

    private val datas = listOf("Hello", "Android", "Welcome Hi ", "Button", "TextView")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initImmersionBar()

        tagFlowLayout.adapter = object : TagAdapter<String>(datas) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val textView: TextView = LayoutInflater.from(this@SearchActivity).inflate(
                    R.layout.item_tag_flow_tv,
                    tagFlowLayout, false
                ) as TextView
                textView.text = t ?: ""
                return textView
            }
        }
        tagFlowLayout.setOnTagClickListener(TagFlowLayout.OnTagClickListener { view, position, parent ->
            ToastUtils.show("点击了${datas[position]}")
            return@OnTagClickListener true
        })
    }

}