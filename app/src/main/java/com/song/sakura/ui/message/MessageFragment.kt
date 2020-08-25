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
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.immersionbar.ImmersionBar
import com.song.sakura.R
import com.song.sakura.entity.response.ProjectTree
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewHolder
import com.song.sakura.ui.base.IBaseViewModel
import com.ui.model.AbsentLiveData
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.item_textview.view.*

class MessageFragment : IBaseFragment<MessageViewModel>() {

    private lateinit var data: MutableList<ProjectTree>

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

        mViewModel.categoryList.observe(viewLifecycleOwner, Observer {
            data = it
            adapter.setList(data)
        })

        adapter.setOnItemClickListener { _, _, position ->
            data.forEach {
                it.select = false
            }
            data[position].select = true
            adapter.setList(data)
        }

        mViewModel.refreshCategory()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
            .statusBarDarkFont(true)
            .titleBar(R.id.appbar)
            .init()
    }
}

class MessageViewModel(app: Application) : IBaseViewModel(app) {
    //选中项目分类
    val selectCategory = MutableLiveData<ProjectTree>()

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
            selectCategory.value = list[0]
            list[0].select = true
        }
        list
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