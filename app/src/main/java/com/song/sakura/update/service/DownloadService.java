package com.song.sakura.update.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.song.sakura.update.listener.OnDownloadListener;
import com.song.sakura.update.manager.BaseHttpDownloadManager;
import com.song.sakura.update.manager.DownloadManager;
import com.song.sakura.update.manager.HttpDownloadManager;
import com.song.sakura.util.ApkUtil;
import com.song.sakura.util.FileUtil;
import com.song.sakura.util.NotificationUtil;

import java.io.File;
import java.util.List;

public final class DownloadService extends Service implements OnDownloadListener {

    private static final String TAG = "UpdateApp: DownloadService";
    private int smallIcon;
    private String apkUrl;
    private String apkName;
    private String downloadPath;
    private List<OnDownloadListener> listeners;
    /*** 是否需要显示通知栏进度 */
    private boolean showNotification = true;
    /*** 下载开始时是否提示 "正在后台下载新版本…" (默认为true) */
    private boolean showBgdToast = true;
    /*** 下载完成是否自动弹出安装页面 (默认为true) */
    private boolean jumpInstallPage = true;
    private int lastProgress;
    private DownloadManager downloadManager;
    private BaseHttpDownloadManager httpManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_STICKY;
        }
        init();
        return super.onStartCommand(intent, flags, startId);
    }


    private void init() {
        downloadManager = DownloadManager.Companion.getManager();
        if (downloadManager == null) {
            LogUtils.eTag(TAG, "init DownloadManager.getInstance() = null ,请先调用 getInstance(Context context) !");
            return;
        }
        apkUrl = downloadManager.getApkUrl();
        apkName = downloadManager.getApkName();
        downloadPath = downloadManager.getDownLoadPath();
        smallIcon = downloadManager.getSmallIcon();
        //创建apk文件存储文件夹
        FileUtil.createDirDirectory(downloadPath);

        listeners = downloadManager.getOnDownloadListener();

        //获取app通知开关是否打开
        boolean enable = NotificationUtil.notificationEnable(this);
        LogUtils.eTag(TAG, enable ? "应用的通知栏开关状态：已打开" : "应用的通知栏开关状态：已关闭");
        if (checkApkMD5()) {
            LogUtils.eTag(TAG, "文件已经存在直接进行安装");
            //直接调用完成监听即可
            done(FileUtil.createFile(downloadPath, apkName));
        } else {
            LogUtils.eTag(TAG, "文件不存在开始下载");
            download();
        }
    }

    /**
     * 校验Apk是否已经下载好了，不重复下载
     *
     * @return 是否下载完成
     */
    private boolean checkApkMD5() {
        if (FileUtil.fileExists(downloadPath, apkName)) {
            String fileMD5 = FileUtil.getFileMD5(FileUtil.createFile(downloadPath, apkName));
            return fileMD5.equalsIgnoreCase(downloadManager.getApkMD5());
        }
        return false;
    }

    /**
     * 获取下载管理者
     */
    private synchronized void download() {
        if (downloadManager.isDownloading()) {
            LogUtils.eTag(TAG, "download: 当前正在下载，请务重复下载！");
            return;
        }
        if (httpManager == null) {
            httpManager = new HttpDownloadManager(downloadPath);
            downloadManager.setHttpManager(httpManager);
        }
        //如果用户自己定义了下载过程
        httpManager.download(apkUrl, apkName, this);
        downloadManager.setState(true);
    }

    @Override
    public void start() {
        if (showNotification) {
            if (showBgdToast) {
                handler.sendEmptyMessage(0);
            }
            String startDownload = "开始下载";
            String startDownloadHint = "可稍后查看下载进度";
            NotificationUtil.showNotification(this, smallIcon, startDownload, startDownloadHint);
        }
        handler.sendEmptyMessage(1);
    }

    @Override
    public void downloading(int max, int progress) {
        if (showNotification) {
            //优化通知栏更新，减少通知栏更新次数
            int curr = (int) (progress / (double) max * 100.0);
            if (curr != lastProgress) {
                lastProgress = curr;
                String downloading = "正在下载新版本";
                String content = curr < 0 ? "" : curr + "%";
                NotificationUtil.showProgressNotification(this, smallIcon, downloading,
                        content, max == -1 ? -1 : 100, curr);
            }
        }
        handler.obtainMessage(2, max, progress).sendToTarget();
    }

    @Override
    public void done(File apk) {
        LogUtils.eTag(TAG, "done: 文件已下载至" + apk.toString());
        downloadManager.setState(false);
        //如果是android Q（api=29）及其以上版本showNotification=false也会发送一个下载完成通知
        if (showNotification || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String downloadCompleted = "下载完成";
            String clickHint = "点击进行安装";
            NotificationUtil.showDoneNotification(this, smallIcon, downloadCompleted,
                    clickHint, downloadManager.getAuthorities(), apk);
        }
        if (jumpInstallPage) {
            ApkUtil.installApk(this, downloadManager.getAuthorities(), apk);
        }
        //如果用户设置了回调 则先处理用户的事件 在执行自己的
        handler.obtainMessage(3, apk).sendToTarget();
    }

    @Override
    public void cancel() {
        downloadManager.setState(false);
        if (showNotification) {
            NotificationUtil.cancelNotification(this);
        }
        handler.sendEmptyMessage(4);
    }

    @Override
    public void error(Exception e) {
        LogUtils.eTag(TAG, "error: " + e);
        downloadManager.setState(false);
        if (showNotification) {
            String downloadError = "下载出错";
            String conDownloading = "点击继续下载";
            NotificationUtil.showErrorNotification(this, smallIcon, downloadError, conDownloading);
        }
        handler.obtainMessage(5, e).sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(DownloadService.this, "正在后台下载新版本…", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    for (OnDownloadListener listener : listeners) {
                        listener.start();
                    }
                    break;
                case 2:
                    for (OnDownloadListener listener : listeners) {
                        listener.downloading(msg.arg1, msg.arg2);
                    }
                    break;
                case 3:
                    for (OnDownloadListener listener : listeners) {
                        listener.done((File) msg.obj);
                    }
                    //执行了完成开始释放资源
                    releaseResources();
                    break;
                case 4:
                    for (OnDownloadListener listener : listeners) {
                        listener.cancel();
                    }
                    releaseResources();
                    break;
                case 5:
                    for (OnDownloadListener listener : listeners) {
                        listener.error((Exception) msg.obj);
                    }
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 下载完成释放资源
     */
    private void releaseResources() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (httpManager != null) {
            httpManager.release();
            httpManager = null;
        }
        stopSelf();
        downloadManager.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
