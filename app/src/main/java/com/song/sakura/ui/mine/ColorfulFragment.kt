package com.song.sakura.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.song.sakura.R
import com.song.sakura.bean.QuickMultipleEntity
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_home_page.*
import java.util.ArrayList

class ColorfulFragment : IBaseFragment<HomeViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityViewModel(getBaseActivity(), HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.persistent_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = MultipleItemQuickAdapter(getMultipleItemData())
        list.adapter = mAdapter
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
}