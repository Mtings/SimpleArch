package com.song.sakura.ui.base

import android.app.Activity

class ErrorDelegate{
    companion object {
        fun error(context: Activity, code:Int):Boolean{
            when(code){
//                ErrorCode.ERROR_NO_AUTH,
//                ErrorCode.ERROR_EXPIRE_TOKEN,
//                ErrorCode.ERROR_FAILED_TOKEN,
//                ErrorCode.ERROR_NO_LOGIN,
//                ErrorCode.ERROR_USER_NOT_EXIST->{
//                    login(context)
//                    return false
//                }
                else->{
                    return true
                }
            }
        }

//         fun login(context: Activity){
//            FragmentParentActivity.startActivity(context , LoginFragment::class.java, false)
//        }
    }

}