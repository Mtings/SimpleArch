package com.song.sakura.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.center.CenterFragment
import com.song.sakura.ui.favorite.FavoriteFragment
import com.song.sakura.ui.home.HomeFragment
import com.song.sakura.ui.home.HomeViewModel
import com.song.sakura.ui.message.MessageFragment
import com.song.sakura.ui.mine.MineFragment
import com.ui.base.BaseFragmentStateAdapter
import com.ui.util.IntentBuilder
import com.ui.util.RxUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : IBaseActivity<HomeViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    @Autowired(name = IntentBuilder.KEY_PAGE_INDEX)
    @JvmField
    var id: Int = 0


    private lateinit var mPagerAdapter: BaseFragmentStateAdapter

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

    override fun initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
            .statusBarDarkFont(true)
            .init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.color.color_background)
        setContentView(R.layout.activity_main)
        initImmersionBar()
        initViewModel(this, HomeViewModel::class.java)
        initView()

        bindUi(RxUtil.clickNoEnable(fab)) {
            val centerItemId = bottomNav.menu.getItem(2).itemId
            if (bottomNav.selectedItemId != centerItemId) {
                changeBadgeVisible(centerItemId, true)
                fab.setImageResource(R.drawable.ic_camera_black_24dp)
                viewPager.setCurrentItem(2, false)
                bottomNav.menu.getItem(2).isChecked = true
            }
        }
    }

    private fun initView() {
        val fragments =
            listOf<Fragment>(HomeFragment(), MessageFragment(), CenterFragment(), FavoriteFragment(), MineFragment())
        mPagerAdapter = BaseFragmentStateAdapter(this.supportFragmentManager, this.lifecycle, fragments)
        viewPager.apply {
            isUserInputEnabled = false
            offscreenPageLimit = fragments.size
            adapter = mPagerAdapter
        }

        bottomNav.itemIconTintList = null
        bottomNav.setOnNavigationItemSelectedListener(this)
        bottomNav.getOrCreateBadge(bottomNav.menu.getItem(1).itemId).number = 2
        bottomNav.getOrCreateBadge(bottomNav.menu.getItem(3).itemId).number = 11
        bottomNav.getOrCreateBadge(bottomNav.menu.getItem(4).itemId).number = 3
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_home -> {
                changeBadgeVisible(item.itemId, true)
                fab.setImageResource(R.drawable.ic_camera_gray_24dp)
                viewPager.setCurrentItem(0, false)
                return true
            }
            R.id.tab_message -> {
                otherTabDo(item)
                viewPager.setCurrentItem(1, false)
                return true
            }
            R.id.tab_empty -> {
                viewPager.setCurrentItem(2, false)
                return true
            }
            R.id.tab_favorite -> {
                otherTabDo(item)
                viewPager.setCurrentItem(3, false)
                return true
            }
            R.id.tab_mine -> {
                otherTabDo(item)
                viewPager.setCurrentItem(4, false)
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

    private var isFirst = true
    override fun onBackPressed() {
        //bottomNav不在首页则返回首页
        if (bottomNav.selectedItemId != bottomNav.menu.getItem(0).itemId) {
            fab.setImageResource(R.drawable.ic_camera_gray_24dp)
            changeBadgeVisible(bottomNav.menu.getItem(0).itemId, true)
            viewPager.setCurrentItem(0, false)
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
