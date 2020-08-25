package com.song.sakura.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.song.sakura.R
import com.song.sakura.entity.response.ArticleBean
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewHolder
import com.song.sakura.util.RouterUtil
import com.ui.decoration.Y_Divider
import com.ui.decoration.Y_DividerBuilder
import com.ui.decoration.Y_DividerItemDecoration
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.list.*


/**
 * Title: com.song.sakura.ui.home
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/06/30 14:09
 *
 * @author SogZiw
 * @version 1.0
 */

class HomeListChildFragment : IBaseFragment<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityViewModel(getBaseActivity(), HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = ListAdapter()
        list.adapter = mAdapter
        list.addItemDecoration(DividerItemDecoration(list.context))

        mAdapter.setOnItemClickListener { _, _, position ->
            RouterUtil.navWebView(mAdapter.getItem(position), getBaseActivity())
        }

        mViewModel.articlePage.observe(viewLifecycleOwner, Observer {
            if (null != it?.curPage) {
                if (it.curPage == 1) {
                    mAdapter.setList(it.datas)
                    mViewModel.refreshControlSwitch(true, it.over)
                } else if (it.curPage > 1) {
                    mAdapter.addData(it.datas)
                    mViewModel.refreshControlSwitch(false, it.over)
                }
            }
        })
    }

}

class DividerItemDecoration(context: Context) : Y_DividerItemDecoration(context) {

    override fun getDivider(itemPosition: Int): Y_Divider? {
        return when (itemPosition) {
            0 ->
                Y_DividerBuilder()
                    .setTopSideLine(true, Color.TRANSPARENT, 10f, 0f, 0f)
                    .setBottomSideLine(true, Color.TRANSPARENT, 10f, 0f, 0f)
                    .create()
            else -> {
                Y_DividerBuilder()
                    .setBottomSideLine(true, Color.TRANSPARENT, 10f, 0f, 0f)
                    .create()
            }
        }
    }
}

class ListAdapter() : BaseQuickAdapter<ArticleBean, IBaseViewHolder>(R.layout.item_article, null) {

    override fun convert(holder: IBaseViewHolder, item: ArticleBean) {
        holder.itemView.title.text = Html.fromHtml(item.title ?: "")
        holder.itemView.author.text = item.getFixedName()
        holder.itemView.image.setImageResource(item.getIcon())
        holder.itemView.tvMakeTop.visibility = if (item.top) View.VISIBLE else View.GONE
        holder.itemView.tvNew.visibility = if (item.fresh) View.VISIBLE else View.GONE
        holder.itemView.dateTime.text = item.niceDate ?: ""
        holder.itemView.tag1.apply {
            if (TextUtils.isEmpty(item.superChapterName)) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                text = item.superChapterName ?: ""
            }
        }
        holder.itemView.tag2.apply {
            if (TextUtils.isEmpty(item.chapterName)) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                text = item.chapterName ?: ""
            }
        }
    }
}