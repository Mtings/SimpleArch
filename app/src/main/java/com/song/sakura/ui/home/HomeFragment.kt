package com.song.sakura.ui.home

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.network.api.ApiResponse
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.song.sakura.R
import com.song.sakura.entity.response.ArticleBean
import com.song.sakura.entity.response.BannerVO
import com.song.sakura.entity.response.HomeRefreshControl
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.util.LiveDataUtil
import com.song.sakura.util.RouterUtil
import com.ui.base.FragmentAdapter
import com.ui.model.AbsentLiveData
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : IBaseFragment<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityViewModel(getBaseActivity(), HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initImmersionBar()
        ImmersionBar.setTitleBar(getBaseActivity(), titleBar)

        mViewModel.bannerList.observe(viewLifecycleOwner, Observer {
            if (!it.data.isNullOrEmpty()) {
                val adapter = BannerImageAdapter(it.data!!)
                banner.apply {
                    addBannerLifecycleObserver(viewLifecycleOwner)
                    indicator = CircleIndicator(getBaseActivity())
//                    setBannerRound(20f)
                    this.adapter = adapter
                    setOnBannerListener { _, position ->
                        RouterUtil.navWebView(it.data!![position].url, getBaseActivity())
                    }
                }
            }
        })

        mViewModel.refreshControl.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                if (it.isRefresh) {
                    refreshLayout.finishRefresh()
                    if (it.isOver) refreshLayout.setNoMoreData(it.isOver)
                } else {
                    if (it.isOver) refreshLayout.finishLoadMoreWithNoMoreData() else refreshLayout.finishLoadMore()
                }
            } else {
                refreshLayout.finishRefreshWithNoMoreData()
            }
        })

        refreshLayout.autoRefresh()

    }

    private fun initView() {
        val titles = listOf("热门", "其他")
        val fragments = listOf(HomeListChildFragment(), Fragment())

        viewPager.offscreenPageLimit = fragments.size
        viewPager.adapter = FragmentAdapter(
            this.childFragmentManager, fragments, titles
        )
        tabLayout.setupWithViewPager(viewPager)

        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.refresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.loadMore()
            }
        })
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
            .statusBarDarkFont(true)
            .titleBar(R.id.topBar)
            .init()
    }

}

class BannerImageAdapter(list: List<BannerVO>) :
    BannerAdapter<BannerVO, BannerImageAdapter.ImageHolder>(list) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        // 通过裁剪实现圆角
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            BannerUtils.setBannerRound(imageView, 20f)
//        }
        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder?, data: BannerVO?, position: Int, size: Int) {
        Glide.with(holder!!.itemView)
            .load(data?.imagePath)
            .into(holder.imageView)
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view as ImageView
    }

}

class HomeViewModel(app: Application) : IBaseViewModel(app) {

    private var currentPage = 0

    private val page = MutableLiveData<Int>()

    val bannerList = Transformations.switchMap(page) {
        if (it == 0)
            api.bannerList()
        else
            AbsentLiveData.create()
    }

    private val articleList = Transformations.switchMap(page) {
        if (it == null) AbsentLiveData.create()
        else api.getHomeArticleList(it)
    }

    private val topArticleList = Transformations.switchMap(page) {
        if (it == 0) api.homeTopList
        else {
            val data = MutableLiveData<ApiResponse<List<ArticleBean>>>()
            data.value =
                ApiResponse(
                    null,
                    0,
                    ""
                )
            data
        }
    }

    val articlePage =
        LiveDataUtil.zip2(articleList, topArticleList) { list, top ->
            list.data?.datas?.let {
                top.data?.run {
                    forEach { a -> a.top = true }
                    it.addAll(0, this)
                }
            }
            list.data
        }


    fun refresh() {
        currentPage = 0
        page.value = currentPage
    }

    fun loadMore() {
        currentPage++
        page.value = currentPage
    }

    val refreshControl = MutableLiveData<HomeRefreshControl>()

    fun refreshControlSwitch(isSuccess: Boolean, isRefresh: Boolean, isOver: Boolean) {
        refreshControl.value = HomeRefreshControl(isSuccess, isRefresh, isOver)
    }

}