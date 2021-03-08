package com.song.sakura.aop;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.content.ContextCompat;

import com.hjq.toast.ToastUtils;
import com.song.sakura.R;
import com.song.sakura.helper.ActivityManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CheckNetAspect {
    /*** 方法切入点 */
    @Pointcut("execution(@com.song.sakura.aop.CheckNet * *(..))")
    public void method() {}

    /*** 在连接点进行方法替换 */
    @Around("method() && @annotation(checkNet)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, CheckNet checkNet) throws Throwable {
        Application application = ActivityManager.getInstance().getApplication();
        if (application != null) {
            ConnectivityManager manager = ContextCompat.getSystemService(application, ConnectivityManager.class);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                // 判断网络是否连接
                if (info == null || !info.isConnected()) {
                    ToastUtils.show(R.string.common_network);
                    return;
                }
            }
        }
        //执行原方法
        joinPoint.proceed();
    }
}