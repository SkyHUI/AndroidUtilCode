package com.blankj.utilcode.util;

import android.os.Environment;
import android.support.annotation.IntDef;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/9/21
 *     desc  : 日志相关工具类
 * </pre>
 */
public final class JLog {

    private JLog() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final int V = 0x01;
    public static final int D = 0x01 << 1;
    public static final int I = 0x01 << 2;
    public static final int W = 0x01 << 3;
    public static final int E = 0x01 << 4;
    public static final int A = 0x01 << 5;

    @IntDef({V, D, I, W, E, A})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    private static final byte

    private static boolean logSwitch      = true;
    private static boolean log2FileSwitch = false;
    private static int     logFilter      = V;
    private static String  tag            = "TAG";
    private static String  dir            = null;
    private static int     stackIndex     = 0;

    private static final int MAX_LEN = 4000;

    /**
     * 初始化函数
     * <p>与{@link #getBuilder()}两者选其一</p>
     *
     * @param logSwitch      日志总开关
     * @param log2FileSwitch 日志写入文件开关，设为true需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}
     * @param logFilter      输入日志类型有{@code v, d, i, w, e}<br>v代表输出所有信息，w则只输出警告...
     * @param tag            标签
     */
    public static void init(boolean logSwitch, boolean log2FileSwitch, @TYPE int logFilter, String tag) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator;
        }
        JLog.logSwitch = logSwitch;
        JLog.log2FileSwitch = log2FileSwitch;
        JLog.logFilter = logFilter;
        JLog.tag = tag;
    }

    /**
     * 获取LogUtils建造者
     * <p>与{@link #init(boolean, boolean, int, String)}两者选其一</p>
     *
     * @return Builder对象
     */
    public static Builder getBuilder() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator + "log" + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator + "log" + File.separator;
        }
        return new Builder();
    }

    public static class Builder {

        private boolean logSwitch      = true;
        private boolean log2FileSwitch = false;
        private int     logFilter      = LogConstants.V;
        private String  tag            = "TAG";

        public Builder setLogSwitch(boolean logSwitch) {
            this.logSwitch = logSwitch;
            return this;
        }

        public Builder setLog2FileSwitch(boolean log2FileSwitch) {
            this.log2FileSwitch = log2FileSwitch;
            return this;
        }

        public Builder setLogFilter(@LogConstants.TYPE int logFilter) {
            this.logFilter = logFilter;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public void create() {
            JLog.logSwitch = logSwitch;
            JLog.log2FileSwitch = log2FileSwitch;
            JLog.logFilter = logFilter;
            JLog.tag = tag;
        }
    }

    /**
     * Verbose日志
     *
     * @param msg 消息
     */
    public static void v(Object msg) {
        log(tag, msg.toString(), null, 'i');
    }

    /**
     * Verbose日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), null, 'i');
    }

    /**
     * Verbose日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    /**
     * Debug日志
     *
     * @param msg 消息
     */
    public static void d(Object msg) {
        log(tag, msg.toString(), null, 'd');
    }

    /**
     * Debug日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void d(String tag, Object msg) {
        log(tag, msg.toString(), null, 'd');
    }

    /**
     * Debug日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /**
     * Info日志
     *
     * @param msg 消息
     */
    public static void i(Object msg) {
        log(tag, msg.toString(), null, 'i');
    }

    /**
     * Info日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void i(String tag, Object msg) {
        log(tag, msg.toString(), null, 'i');
    }

    /**
     * Info日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /**
     * Warn日志
     *
     * @param msg 消息
     */
    public static void w(Object msg) {
        log(tag, msg.toString(), null, 'w');
    }

    /**
     * Warn日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void w(String tag, Object msg) {
        log(tag, msg.toString(), null, 'w');
    }

    /**
     * Warn日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /**
     * Error日志
     *
     * @param msg 消息
     */
    public static void e(Object msg) {
        log(tag, msg.toString(), null, 'e');
    }

    /**
     * Error日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void e(String tag, Object msg) {
        log(tag, msg.toString(), null, 'e');
    }

    /**
     * Error日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void log(String tag, String msg, Throwable tr, char type) {
        if (msg == null || msg.isEmpty()) return;
        if (logSwitch) {
            if ('e' == type && ('e' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'e');
            } else if ('w' == type && ('w' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'w');
            } else if ('d' == type && ('d' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'd');
            } else if ('i' == type && ('d' == logFilter || 'v' == logFilter)) {
                printLog(generateTag(tag), msg, tr, 'i');
            }
            if (log2FileSwitch) {
                log2File(type, generateTag(tag), msg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void printLog(final String tag, final String msg, Throwable tr, char type) {
        for (int i = 0, len = msg.length(); i * MAX_LEN < len; ++i) {
            String subMsg = msg.substring(i * MAX_LEN, (i + 1) * MAX_LEN < len ? (i + 1) * MAX_LEN : len);
            switch (type) {
                case 'e':
                    Log.e(tag, subMsg, tr);
                    break;
                case 'w':
                    Log.w(tag, subMsg, tr);
                    break;
                case 'd':
                    Log.d(tag, subMsg, tr);
                    break;
                case 'i':
                    Log.i(tag, subMsg, tr);
                    break;
                case 'a':
                    Log.wtf(tag, subMsg, tr);
                    break;
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param type 日志类型
     * @param tag  标签
     * @param msg  信息
     **/
    private synchronized static void log2File(final char type, final String tag, final String msg) {
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        final String fullPath = dir + date + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        final String dateLogContent = time + ":" + type + ":" + tag + ":" + msg + '\n';
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(dateLogContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(bw);
                }
            }
        }).start();
    }

    /**
     * 产生tag
     *
     * @return tag
     */
    private static String generateTag(String tag) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stackIndex == 0) {
            while (!stacks[stackIndex].getMethodName().equals("generateTag")) {
                ++stackIndex;
            }
            stackIndex += 3;
        }
        StackTraceElement caller = stacks[stackIndex];
        String callerClazzName = caller.getClassName();
        String format = "[" + tag + " at %s(%s.java:%d)]";
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format(format, caller.getMethodName(), callerClazzName, caller.getLineNumber());
    }
}