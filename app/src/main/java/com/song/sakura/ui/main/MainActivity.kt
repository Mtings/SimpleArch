package com.song.sakura.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.event.OpenDrawerEvent
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.center.CenterFragment
import com.song.sakura.ui.favorite.FavoriteFragment
import com.song.sakura.ui.home.HomeFragment
import com.song.sakura.ui.home.HomeViewModel
import com.song.sakura.ui.message.MessageFragment
import com.song.sakura.ui.mine.MineFragment
import com.ui.base.BaseFragment
import com.ui.base.BaseFragmentAdapter
import com.ui.util.IntentBuilder
import com.ui.util.RxUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.viewPager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : IBaseActivity<HomeViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    @Autowired(name = IntentBuilder.KEY_PAGE_INDEX)
    @JvmField
    var id: Int = 0


    private lateinit var mPagerAdapter: BaseFragmentAdapter<BaseFragment>

    companion object {
        fun goMain(context: Activity) {
            context.startActivity(
                Intent(context, MainActivity::class.java)
                    .putExtra(IntentBuilder.KEY_PAGE_INDEX, 0)
                    .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            )
            context.overridePendingTransition(R.anim.right_in, R.anim.left_out)
            ActivityCompat.finishAffinity(context)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.color.color_background)
        setContentView(R.layout.activity_main)
        initViewModel(this, HomeViewModel::class.java)

        mPagerAdapter = BaseFragmentAdapter(this)
        mPagerAdapter.addFragment(HomeFragment())
        mPagerAdapter.addFragment(MessageFragment())
        mPagerAdapter.addFragment(CenterFragment())
        mPagerAdapter.addFragment(FavoriteFragment())
        mPagerAdapter.addFragment(MineFragment())
        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true)
        viewPager.adapter = mPagerAdapter

        bottomNav.itemIconTintList = null
        bottomNav.setOnNavigationItemSelectedListener(this)
        bottomNav.getOrCreateBadge(bottomNav.menu.getItem(1).itemId).number = 2
        bottomNav.getOrCreateBadge(bottomNav.menu.getItem(3).itemId).number = 11
        bottomNav.getOrCreateBadge(bottomNav.menu.getItem(4).itemId).number = 3

        bindUi(RxUtil.clickNoEnable(fab)) {
            val centerItemId = bottomNav.menu.getItem(2).itemId
            if (bottomNav.selectedItemId != centerItemId) {
                changeBadgeVisible(centerItemId, true)
                fab.setImageResource(R.drawable.ic_camera_black_24dp)
                viewPager.currentItem = 2
                bottomNav.menu.getItem(2).isChecked = true
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_home -> {
                changeBadgeVisible(item.itemId, true)
                fab.setImageResource(R.drawable.ic_camera_gray_24dp)
                viewPager.currentItem = 0
                return true
            }
            R.id.tab_message -> {
                otherTabDo(item)
                viewPager.currentItem = 1
                return true
            }
            R.id.tab_empty -> {
                viewPager.currentItem = 2
                return true
            }
            R.id.tab_favorite -> {
                otherTabDo(item)
                viewPager.currentItem = 3
                return true
            }
            R.id.tab_mine -> {
                otherTabDo(item)
                viewPager.currentItem = 4
                return true
            }
            else -> {
            }
        }
        return false
    }

    private fun otherTabDo(item: MenuItem) {
        bottomNav.getOrCreateBadge(item.itemId)?.isVisible = false
        changeBadgeVisible(item.itemId, true)
        fab.setImageResource(R.drawable.ic_camera_gray_24dp)
    }

    /**
     * 改变其他item的badge显隐
     */
    private fun changeBadgeVisible(currentItemId: Int, isVisible: Boolean) {
        for (i in 1..4) {
            val itemId = bottomNav.menu.getItem(i).itemId
            if (currentItemId != itemId && i != 2) {
                bottomNav.getOrCreateBadge(itemId)?.isVisible = isVisible
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(event: OpenDrawerEvent) {

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private var isFirst = true
    override fun onBackPressed() {
        //bottomNav不在首页则返回首页
        if (bottomNav.selectedItemId != bottomNav.menu.getItem(0).itemId) {
            fab.setImageResource(R.drawable.ic_camera_gray_24dp)
            changeBadgeVisible(bottomNav.menu.getItem(0).itemId, true)
            viewPager.currentItem = 0
            bottomNav.menu.getItem(0).isChecked = true
            return
        }
        if (isFirst) {
            Toast.makeText(this, "再次点击返回退出", Toast.LENGTH_SHORT).show()
            isFirst = false
            bottomNav.postDelayed({ isFirst = true }, 3500)
            return
        }
        // 进行内存优化，销毁掉所有的界面
//        ActivityStackManager.getInstance().finishAllActivities()
        ActivityCompat.finishAffinity(this)
    }

    override fun onDestroy() {
        viewPager.adapter = null
        bottomNav.setOnNavigationItemSelectedListener(null)
        super.onDestroy()
    }
}
