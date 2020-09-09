package com.song.sakura.ui.message

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.song.sakura.R
import com.song.sakura.entity.response.ArticleBean
import com.song.sakura.entity.response.ProjectTree
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewHolder
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.util.RouterUtil
import com.ui.model.AbsentLiveData
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.item_project.view.*
import kotlinx.android.synthetic.main.item_textview.view.*

class MessageFragment : IBaseFragment<MessageViewModel>() {

    private var data: MutableList<ProjectTree> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this, MessageViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar?.apply {
            addTextRight("样式")
            navigationIcon = null
            title = "分类"
            setOnMenuItemClickListener {
                val intent = Intent(baseActivity, DialogActivity::class.java)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
        }

        initImmersionBar()

        val adapter = LeftCategoryAdapter()
        list.adapter = adapter

        val rightProjectAdapter = RightProjectAdapter()
        rightList.adapter = rightProjectAdapter

        mViewModel.categoryList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                data = it
                adapter.setList(data)
                mViewModel.refreshArticle(data[0].id)
            } else {
                data = ArrayList()
                adapter.setList(data)
                rightProjectAdapter.setList(ArrayList())
            }

        })

        mViewModel.projectLit.observe(viewLifecycleOwner, Observer {
            if (it.data?.curPage == 1) {
                rightProjectAdapter.setList(it.data?.datas)
                if (it.data!!.over) smartRefreshLayout.finishRefreshWithNoMoreData()
                else smartRefreshLayout.finishRefresh()
            } else {
                rightProjectAdapter.addData(it.data?.datas ?: ArrayList())
                if (it.data?.over!!) smartRefreshLayout.finishLoadMoreWithNoMoreData()
                else smartRefreshLayout.finishLoadMore()
            }
        })

        rightProjectAdapter.setOnItemClickListener { _, _, position ->
            RouterUtil.navWebView(rightProjectAdapter.getItem(position), getBaseActivity())
        }

        adapter.setOnItemClickListener { _, _, position ->
            data.forEach {
                it.select = false
            }
            data[position].select = true
            adapter.setList(data)

            resetRefreshLayout()
            rightProjectAdapter.setList(ArrayList())
            mViewModel.refreshArticle(data[position].id)
        }

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.refreshArticle(mViewModel.categoryId.value ?: 0)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.loadMoreArticle(mViewModel.categoryId.value ?: 0)
            }
        })

        mViewModel.refreshCategory()
    }

    private fun resetRefreshLayout() {
        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setNoMoreData(false)
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
            .statusBarDarkFont(true)
            .titleBar(R.id.appbar)
            .init()
    }
}

class MessageViewModel(app: Application) : IBaseViewModel(app) {

    private val refresh = MutableLiveData<Boolean>()

    fun refreshCategory() {
        refresh.value = true
    }

    private var projectTree = Transformations.switchMap(refresh) {
        if (it == null) AbsentLiveData.create()
        else api.projectTree()
    }

    var categoryList = Transformations.map(projectTree) {
        val list = it.data ?: ArrayList()
        if (list.isNotEmpty()) {
            list[0].select = true
        }
        list
    }

    private var page = 1

    val categoryId = MutableLiveData<Int>()

    var projectLit = Transformations.switchMap(categoryId) {
        if (it == 0) AbsentLiveData.create()
        else api.projectList(page, it)
    }

    fun refreshArticle(cid: Int) {
        page = 1
        categoryId.value = cid
    }

    fun loadMoreArticle(cid: Int) {
        page++
        categoryId.value = cid
    }
}

class LeftCategoryAdapter :
    BaseQuickAdapter<ProjectTree, IBaseViewHolder>(R.layout.item_textview, null) {

    override fun convert(holder: IBaseViewHolder, item: ProjectTree) {
        holder.itemView.textView.apply {
            text = Html.fromHtml(item.name)
            isSelected = item.select
        }
    }
}

class RightProjectAdapter :
    BaseQuickAdapter<ArticleBean, IBaseViewHolder>(R.layout.item_project, null) {

    override fun convert(holder: IBaseViewHolder, item: ArticleBean) {
        holder.itemView.tvTitle.text = item.title
        holder.itemView.tvDesc.text = item.desc
        holder.itemView.tvName.text = item.getFixedName()
        holder.itemView.tvTime.text = item.niceDate

        Glide.with(holder.itemView.context).load(item.envelopePic)
            .transform(RoundedCorners(8))
            .into(holder.itemView.img)
    }
}