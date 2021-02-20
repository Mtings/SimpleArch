package com.song.sakura.ui.mine

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.home.HomeViewModel
import com.ui.action.ClickAction
import com.ui.base.FragmentAdapter
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity2 : IBaseActivity<HomeViewModel>(), ClickAction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_2)
        initViewModel(this, HomeViewModel::class.java)
        initImmersionBar()
        ImmersionBar.setTitleBar(this, titleBar)
        initView()

        refreshLayout.autoRefresh()
    }


    private fun initView() {
        val titles = listOf("热门推荐", "商品促销")
        val fragments = listOf(ColorfulFragment(), ColorfulFragment())

        viewPager.offscreenPageLimit = fragments.size
        viewPager.adapter = FragmentAdapter(
            this.supportFragmentManager, fragments, titles
        )
        tabLayout.setupWithViewPager(viewPager)


        refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {

            override fun onRefresh(refreshLayout: RefreshLayout) {
                refreshLayout.finishRefresh(500)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                refreshLayout.finishLoadMore(500)
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