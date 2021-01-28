package com.song.sakura.ui.center

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.aop.SingleClick
import com.song.sakura.app.App
import com.song.sakura.entity.Word
import com.song.sakura.ui.base.BaseViewHolder
import com.song.sakura.ui.base.IBaseFragment
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.dialog.InputDialog
import com.song.sakura.ui.dialog.MenuDialog
import com.ui.action.ClickAction
import com.ui.base.BaseDialog
import kotlinx.android.synthetic.main.fragment_center.*
import kotlinx.android.synthetic.main.item_word_title.view.*
import kotlinx.coroutines.launch

class CenterFragment : IBaseFragment<CenterViewModel>(), ClickAction {

    override fun <V : View?> findViewById(id: Int): V {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this, CenterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersionBar()
        mToolbar?.apply {
            navigationIcon = null
            title = "中间"
        }

        val adapter = WordListAdapter()
        recyclerView.adapter = adapter

        mViewModel.allWords.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }

        setOnClickListener(floating)

        val data: MutableList<String> = ArrayList()
        data.add("删除String")
        data.add("删除实体")
        data.add("删除所有")
        adapter.setOnItemLongClickListener { _, _, index ->
            MenuDialog.Builder(baseActivity)
                .setGravity(Gravity.CENTER)
                .setList(data)
                .setListener(object : MenuDialog.OnListener<String> {

                    override fun onSelected(dialog: BaseDialog?, position: Int, string: String?) {
                        when (position) {
                            0 -> {
                                ToastUtils.show("删除了${adapter.data[index].word}")
                                mViewModel.deleteWord(adapter.data[index].word)
                            }
                            1 -> {
                                ToastUtils.show("删除了${adapter.data[index].word}")
                                mViewModel.deleteOne(adapter.data[index])
                            }
                            else -> {
                                ToastUtils.show("删除了所有单词")
                                mViewModel.deleteAll()
                            }
                        }
                    }
                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                }).show()

            return@setOnItemLongClickListener true
        }

    }

    @SingleClick
    override fun onClick(v: View?) {
        if (v == floating) {
            InputDialog.Builder(baseActivity)
                .setTitle("输入单词")
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(getString(R.string.common_cancel))
                .setListener(object : InputDialog.OnListener {
                    override fun onConfirm(dialog: BaseDialog?, content: String?) {
                        if (!TextUtils.isEmpty(content)) {
                            mViewModel.insert(Word(content!!))
                        }
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }
    }
}

class WordListAdapter : BaseQuickAdapter<Word, BaseViewHolder>(R.layout.item_word_title, null) {

    override fun convert(holder: BaseViewHolder, item: Word) {
        holder.itemView.textView.text = item.word
    }
}

class CenterViewModel(app: Application) : IBaseViewModel(app) {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>> = App.repository.allWords.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        App.repository.insert(word)
    }

    fun deleteAll() = viewModelScope.launch {
        App.repository.deleteAll()
    }

    fun deleteWord(word: String) = viewModelScope.launch {
        App.repository.deleteWord(word)
    }

    fun deleteOne(word: Word) = viewModelScope.launch {
        App.repository.deleteOne(word)
    }

}