package com.song.sakura.ui.base

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ui.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R

class FragmentParentActivity : BaseActivity() {


    fun initImmersionBar() {
        ImmersionBar.with(this)!!.statusBarDarkFont(true).init()
    }

    fun initImmersionBar2() {
        ImmersionBar.with(this)!!.titleBar(R.id.appbar).statusBarColorInt(getColors(R.color.white))
                .statusBarDarkFont(true).init()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private var baseFragment: IBaseFragment<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isImmbar = intent.getBooleanExtra(KEY_IMMBAR, true)
        val isToolbar = intent.getBooleanExtra(KEY_TOOLBAR, true)
        val clz = intent.getSerializableExtra(KEY_FRAGMENT) as Class<*>
        var cls = ""
        if (TextUtils.isEmpty(cls)) {
            cls = clz.name
        } else {
            cls = intent.getStringExtra(KEY_FRAGMENT_NAME)
        }


        if (isToolbar) {
            setContentView(R.layout.activity_with_toolbar_layout)
            if (isImmbar) {
                initImmersionBar2()
            }
            val fragment = Fragment.instantiate(activity, cls)
            if (fragment is IBaseFragment<*>)
                baseFragment = fragment
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frame_holder, fragment, cls)
            ft.commitAllowingStateLoss()
        } else {
            setRootView(window.decorView as ViewGroup)
            initProgressLayout()
            val fragment = Fragment.instantiate(activity, cls)
            if (fragment is IBaseFragment<*>)
                baseFragment = fragment
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(android.R.id.content, fragment, cls)
            ft.commitAllowingStateLoss()
            if (isImmbar) {
                initImmersionBar()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (baseFragment != null)
            baseFragment!!.onActivityResult(requestCode, resultCode, data)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {


        fun startActivity(context: Activity, clz: Class<*>, isToolbar: Boolean) {
            val intent = Intent(context, FragmentParentActivity::class.java)
            intent.putExtra(KEY_FRAGMENT, clz)
            intent.putExtra(KEY_TOOLBAR, isToolbar)
            context.startActivity(intent)
        }


        fun startActivity(context: Activity, clz: Class<*>) {
            val intent = Intent(context, FragmentParentActivity::class.java)
            intent.putExtra(KEY_FRAGMENT, clz)
            context.startActivity(intent)
        }

        val KEY_FRAGMENT_NAME = "KEY_FRAGMENT_NAME"

        const val KEY_FRAGMENT = "KEY_FRAGMENT"
        const val KEY_TOOLBAR = "KEY_TOOLBAR"
        const val KEY_IMMBAR = "KEY_IMMBAR"
    }

}
