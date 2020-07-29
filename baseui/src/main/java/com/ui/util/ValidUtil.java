package com.ui.util;

import android.text.TextUtils;

public class ValidUtil {

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、   159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通） 17 ,19
    总结起来就是第一位必定为1，第二位必定为3或5或7,8,9，其他位置的可以为0-9
    */
        String num = "[1][35789]\\d{9}";//"[1]"代表第1位为数字1，"[35789]"代表第二位可以为3、5、7,8,9中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (StringUtils.isBlank(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    public static boolean phoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11) {
            try {
                Long phone = Long.valueOf(phoneNumber);
                if (phone >= 0) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return isValid;
    }

    public static boolean accountValid(String pwd) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() >= 3) {
            return true;
        }
        return isValid;
    }

    public static boolean pwdValid(String pwd) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() >= 6) {
            return true;
        }
        return isValid;
    }

    public static boolean payPwdValid(String pwd) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() == 6) {
            return true;
        }
        return isValid;
    }
}
