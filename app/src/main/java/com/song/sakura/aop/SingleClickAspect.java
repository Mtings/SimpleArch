package com.song.sakura.aop;

import com.blankj.utilcode.util.LogUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

@Aspect
public class SingleClickAspect {

    /*** 最近一次点击的时间 */
    private long mLastTime;

    /*** 最近一次点击的标记 */
    private String mLastTag;

    /*** 方法切入点 */
    @Pointcut("execution(@com.song.sakura.aop.SingleClick * *(..))")
    public void method() {
    }

    /*** 在连接点进行方法替换 */
    @Around("method() && @annotation(singleClick)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        // 方法所在类
        String className = codeSignature.getDeclaringType().getName();
        // 方法名
        String methodName = codeSignature.getName();
        // 构建方法 TAG
        StringBuilder builder = new StringBuilder(className + "." + methodName);
        builder.append("(");
        Object[] parameterValues = joinPoint.getArgs();
        for (int i = 0; i < parameterValues.length; i++) {
            Object arg = parameterValues[i];
            if (i == 0) {
                builder.append(arg);
            } else {
                builder.append(", ")
                        .append(arg);
            }
        }
        builder.append(")");

        String tag = builder.toString();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mLastTime < singleClick.value() && tag.equals(mLastTag)) {
            LogUtils.eTag("SingleClick", "%s 毫秒内发生快速点击：%s", singleClick.value());
            return;
        }
        mLastTime = currentTimeMillis;
        mLastTag = tag;
        // 执行原方法
        joinPoint.proceed();
    }
}