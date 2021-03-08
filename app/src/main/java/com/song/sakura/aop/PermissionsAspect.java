package com.song.sakura.aop;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.song.sakura.helper.ActivityManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

@Aspect
public class PermissionsAspect {

    /*** 方法切入点 */
    @Pointcut("execution(@com.song.sakura.aop.Permissions * *(..))")
    public void method() {
    }

    /*** 在连接点进行方法替换 */
    @SuppressLint("CheckResult")
    @Around("method() && @annotation(permissions)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, Permissions permissions) {
        Activity activity = ActivityManager.getInstance().getTopActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        XXPermissions.with(activity)
                .permission(permissions.value())
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            try {
                                // 获得权限，执行原方法
                                joinPoint.proceed();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            ToastUtils.show("授权失败，请手动授予权限");
                            XXPermissions.startPermissionActivity(ActivityManager.getInstance().getTopActivity(), permissions);
                        } else {
                            ToastUtils.show("请先授予权限");
                        }
                    }
                });
    }
}