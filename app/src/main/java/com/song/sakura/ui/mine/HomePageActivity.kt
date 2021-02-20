package com.song.sakura.ui.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.song.sakura.R
import com.song.sakura.bean.QuickMultipleEntity
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.home.HomeListChildFragment
import com.song.sakura.ui.home.HomeViewModel
import com.ui.action.ClickAction
import com.ui.base.FragmentAdapter
import kotlinx.android.synthetic.main.activity_home_page.*
import java.util.*

class HomePageActivity : IBaseActivity<HomeViewModel>(), ClickAction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        initViewModel(this, HomeViewModel::class.java)
        initImmersionBar()
        ImmersionBar.setTitleBar(this, titleBar)

        initView()

        val mAdapter =  MultipleItemQuickAdapter(getMultipleItemData())
        list.adapter = mAdapter

        mViewModel.refreshControl.observe(this, {
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

    private fun getMultipleItemData(): List<QuickMultipleEntity> {
        val list: MutableList<QuickMultipleEntity> = ArrayList<QuickMultipleEntity>()
        for (i in 0..3) {
            list.add(QuickMultipleEntity(QuickMultipleEntity.BANNER))
            list.add(QuickMultipleEntity(QuickMultipleEntity.MULTI_ICON))
            list.add(QuickMultipleEntity(QuickMultipleEntity.BANNER_AD))
        }
        return list
    }

    private fun initView() {
        val titles = listOf("热门推荐", "商品促销")
        val fragments = listOf(HomeListChildFragment(), ColorfulFragment2())

        viewPager.offscreenPageLimit = fragments.size
        viewPager.adapter = FragmentAdapter(
            this.supportFragmentManager, fragments, titles
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