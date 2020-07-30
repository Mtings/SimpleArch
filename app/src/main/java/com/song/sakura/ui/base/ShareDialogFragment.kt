package com.song.sakura.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.song.sakura.R
import com.song.sakura.extension.setDrawable
import com.song.sakura.util.*
import com.song.sakura.util.ShareUtil.share
import kotlinx.android.synthetic.main.fragment_share_dialog.*

/**
 * 分享对话框的弹出界面。
 */
open class ShareDialogFragment : BottomSheetDialogFragment() {

    private lateinit var shareContent: String

    private lateinit var attachedActivity: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let { act ->
            attachedActivity = act
            tvToWechatFriends.setDrawable(
                ContextCompat.getDrawable(
                    act,
                    R.drawable.ic_share_wechat_black_30dp
                ), 30f, 30f, 1
            )
            tvShareToWeibo.setDrawable(
                ContextCompat.getDrawable(
                    act,
                    R.drawable.ic_share_weibo_black_30dp
                ), 30f, 30f, 1
            )
            tvShareToQQ.setDrawable(
                ContextCompat.getDrawable(
                    act,
                    R.drawable.ic_share_qq_black_30dp
                ), 30f, 30f, 1
            )
            tvShareToQQzone.setDrawable(
                ContextCompat.getDrawable(
                    act,
                    R.drawable.ic_share_qq_zone_black_30dp
                ), 30f, 30f, 1
            )

            tvShareToQQ.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_QQ)
                dismiss()
            }
            tvToWechatFriends.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_WECHAT)
                dismiss()
            }
            tvShareToWeibo.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_WEIBO)
                dismiss()
            }
            tvShareToQQzone.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_QQZONE)
                dismiss()
            }
            llMore.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_MORE)
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    fun showDialog(activity: AppCompatActivity, shareContent: String) {
        show(activity.supportFragmentManager, "share_dialog")
        this.shareContent = shareContent
    }
}