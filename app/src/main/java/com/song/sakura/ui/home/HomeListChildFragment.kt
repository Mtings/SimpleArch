package com.song.sakura.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.song.sakura.R
import com.song.sakura.entity.response.ArticleBean
import com.song.sakura.event.HomeRefreshEvent
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewHolder
import com.song.sakura.util.RouterUtil
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.list.*
import org.greenrobot.eventbus.EventBus


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

        mAdapter.setOnItemClickListener { _, _, position ->
            RouterUtil.navWebView(mAdapter.getItem(position), getBaseActivity())
        }

        mViewModel.articlePage.observe(viewLifecycleOwner, Observer {
            if (it?.curPage == 1) {
                mAdapter.setList(it.datas)
                EventBus.getDefault().post(HomeRefreshEvent(true, it.over))
            } else if (it?.curPage!! > 1) {
                mAdapter.addData(it.datas)
                EventBus.getDefault().post(HomeRefreshEvent(false, it.over))
            }
        })
    }

}

class ListAdapter() : BaseQuickAdapter<ArticleBean, IBaseViewHolder>(R.layout.item_article, null) {

    override fun convert(holder: IBaseViewHolder, item: ArticleBean) {
        holder.itemView.title.text = item.title ?: ""
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