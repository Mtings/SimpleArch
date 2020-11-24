package com.song.sakura.bean.response

import android.text.TextUtils
import com.song.sakura.R

/**
 * Title: com.song.sakura.entity.response
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/07/23 14:34
 *
 * @author SogZiw
 * @version 1.0
 */

class HomePageEntity(
    var curPage: Int,
    var datas: MutableList<ArticleBean>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)

data class BannerVO(
    var id: Int,
    var title: String,
    var desc: String,
    var type: Int,
    var url: String,
    var imagePath: String
)

class ArticleBean(
    var id: Int,
    var audit: Int,
    var author: String?,
    var shareUser: String?,
    var niceDate: String?,
    var collect: Boolean,
    var link: String,
    var desc: String,
    var title: String?,
    var fresh: Boolean,
    var top: Boolean,
    var superChapterId: Int,
    var superChapterName: String?,
    var chapterId: Int,
    var chapterName: String?,
    var shareDate: Long,
    var publishTime: Long,
    var envelopePic: String
) {
    fun getFixedName(): String {
        if (TextUtils.isEmpty(author)) return shareUser ?: ""
        return author ?: ""
    }

    fun getIcon(): Int {
        return when {
            link.contains("wanandroid.com") -> R.drawable.ic_logo_wan
            link.contains("www.jianshu.com") -> R.drawable.ic_logo_jianshu
            link.contains("juejin.im") -> R.drawable.ic_logo_juejin
            link.contains("blog.csdn.net") -> R.drawable.ic_logo_csdn
            link.contains("weixin.qq.com") -> R.drawable.ic_logo_wxi
            else -> R.drawable.ic_logo_other
        }
    }
}

class HomeRefreshControl(
    var isSuccess: Boolean,
    var isRefresh: Boolean,
    var isOver: Boolean
)

data class ProjectTree(
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int,
    var icon: Int,
    var select: Boolean
)