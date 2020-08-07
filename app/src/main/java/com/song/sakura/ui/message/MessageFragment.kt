package com.song.sakura.ui.message

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.databinding.FragmentMessageBinding
import com.song.sakura.entity.request.NewsPageReq
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.model.AbsentLiveData
import com.ui.util.GsonUtil
import kotlinx.android.synthetic.main.fragment_message.*

class MessageFragment : IBaseFragment<MessageViewModel>() {

    private lateinit var mBinding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this, MessageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar?.apply {
            addTextRight("样式")
            navigationIcon = null
            title = "Toolbar"
            setOnMenuItemClickListener {
                val intent = Intent(baseActivity, DialogActivity::class.java)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
        }

        initImmersionBar()

    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
            .statusBarDarkFont(true)
            .titleBar(R.id.appbar)
            .init()
    }
}

class MessageViewModel(app: Application) : IBaseViewModel(app) {

    val title = MutableLiveData<String>()

    fun initData() {
        title.value = "SimpleArch"
    }

}