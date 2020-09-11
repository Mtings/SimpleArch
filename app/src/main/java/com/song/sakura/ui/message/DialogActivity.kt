package com.song.sakura.ui.message

import android.os.Bundle
import android.view.Gravity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import com.song.sakura.R
import com.song.sakura.ui.base.IBaseActivity
import com.song.sakura.ui.base.IBaseViewModel
import com.song.sakura.ui.dialog.*
import com.ui.base.BaseDialog
import com.ui.util.RxUtil
import kotlinx.android.synthetic.main.activity_dialog.*
import java.util.*

/**
 * Title: com.song.sakura.ui.message
 * Description:
 * Copyright:Copyright(c) 2020
 * CreateTime:2020/04/15 10:45
 *
 * @author SogZiw
 * @version 1.0
 */

class DialogActivity : IBaseActivity<IBaseViewModel>() {

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .titleBar(R.id.appbar)
            .keyboardEnable(true).statusBarDarkFont(true)
            .navigationBarWithKitkatEnable(false).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        initImmersionBar()

        // 消息对话框
        bindUi(RxUtil.click(btn_dialog_message)) {
            MessageDialog.Builder(this)
                // 标题可以不用填写
                .setTitle("我是标题")
                // 内容必须要填写
                .setMessage("我是内容")
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(object : MessageDialog.OnListener {

                    override fun onConfirm(dialog: BaseDialog?) {
                        ToastUtils.show("确定了")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        // 输入对话框
        bindUi(RxUtil.click(btn_dialog_input)) {
            InputDialog.Builder(this)
                // 标题可以不用填写
                .setTitle("我是标题")
                // 内容可以不用填写
                .setContent("我是内容")
                // 提示可以不用填写
                .setHint("我是提示")
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                //设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(object : InputDialog.OnListener {

                    override fun onConfirm(dialog: BaseDialog?, content: String?) {
                        ToastUtils.show("确定了${content ?: ""}")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        // 底部选择框
        bindUi(RxUtil.click(btn_dialog_bottom_menu)) {
            val data: MutableList<String> =
                ArrayList()
            for (i in 0..9) {
                data.add("我是数据$i")
            }
            MenuDialog.Builder(this)
                // 设置 null 表示不显示取消按钮
                //.setCancel(getString(R.string.common_cancel))
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setList(data)
                .setListener(object : MenuDialog.OnListener<String> {

                    override fun onSelected(dialog: BaseDialog?, position: Int, string: String?) {
                        ToastUtils.show("位置：$position，文本：$string")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        // 居中选择框
        bindUi(RxUtil.click(btn_dialog_center_menu)) {
            val data: MutableList<String> =
                ArrayList()
            for (i in 0..9) {
                data.add("我是数据$i")
            }
            MenuDialog.Builder(this)
                .setGravity(Gravity.CENTER)
                // 设置 null 表示不显示取消按钮
                //.setCancel(null)
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setList(data)
                .setListener(object : MenuDialog.OnListener<String> {

                    override fun onSelected(dialog: BaseDialog?, position: Int, string: String?) {
                        ToastUtils.show("位置：$position，文本：$string")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        // 选择地区对话框
        bindUi(RxUtil.click(btn_dialog_address)) {
            AddressDialog.Builder(this)
                .setTitle("请选择地区")
                .setListener(object : AddressDialog.OnListener {
                    override fun onSelected(
                        dialog: BaseDialog?,
                        province: String?,
                        city: String?,
                        area: String?
                    ) {
                        ToastUtils.show("$province $city $area")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        super.onCancel(dialog)
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        bindUi(RxUtil.click(btn_dialog_time)) {
            // 时间选择对话框
            TimeDialog.Builder(this)
                .setTitle("请选择时间")
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 设置时间
                //.setTime("23:59:59")
                //.setTime("235959")
                // 设置小时
                //.setHour(23)
                // 设置分钟
                //.setMinute(59)
                // 设置秒数
                //.setSecond(59)
                // 不选择秒数
                //.setIgnoreSecond()
                .setListener(object : TimeDialog.OnListener {
                    override fun onSelected(
                        dialog: BaseDialog?,
                        hour: Int,
                        minute: Int,
                        second: Int
                    ) {
                        ToastUtils.show("${hour}时${minute}分${second}秒")

                        // 如果不指定年月日则默认为今天的日期
                        val calendar = Calendar.getInstance()
                        calendar[Calendar.HOUR_OF_DAY] = hour
                        calendar[Calendar.MINUTE] = minute
                        calendar[Calendar.SECOND] = second
//                        ToastUtils.showLong(this@DialogActivity, "${calendar.timeInMillis}")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        bindUi(RxUtil.click(btn_dialog_date)) {
            // 日期选择对话框
            DateDialog.Builder(this)
                .setTitle("请选择日期")
                // 确定按钮文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置 null 表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 设置日期
                //.setDate("2018-12-31")
                //.setDate("20181231")
                //.setDate(1546263036137)
                // 设置年份
                //.setYear(2018)
                // 设置月份
                //.setMonth(2)
                // 设置天数
                //.setDay(20)
                // 不选择天数
                //.setIgnoreDay()
                .setListener(object : DateDialog.OnListener {

                    override fun onSelected(dialog: BaseDialog?, year: Int, month: Int, day: Int) {

                        ToastUtils.show("${year}年${month}月${day}日")

                        // 如果不指定时分秒则默认为现在的时间
                        val calendar = Calendar.getInstance()
                        calendar[Calendar.YEAR] = year
                        // 月份从零开始，所以需要减 1
                        calendar[Calendar.MONTH] = month - 1
                        calendar[Calendar.DAY_OF_MONTH] = day
                        ToastUtils.show("时间戳：" + calendar.timeInMillis)
                        //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }

                })
                .show()
        }

        bindUi(RxUtil.click(btn_dialog_pay)) {
            // 支付密码输入对话框
            PayPasswordDialog.Builder(this)
                .setTitle(getString(R.string.pay_title))
                .setSubTitle("用于购买一个女盆友")
                .setMoney("￥ 100.00")
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setListener(object : PayPasswordDialog.OnListener {
                    override fun onCompleted(dialog: BaseDialog?, password: String?) {
                        ToastUtils.show(password ?: "")
                    }

                    override fun onCancel(dialog: BaseDialog?) {
                        ToastUtils.show("取消了")
                    }
                })
                .show()
        }

        bindUi(RxUtil.click(btn_dialog_fail_toast)) {
            error("这就是错误提示")
        }

    }

}