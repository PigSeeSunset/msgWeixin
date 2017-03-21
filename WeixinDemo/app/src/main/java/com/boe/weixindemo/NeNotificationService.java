package com.boe.weixindemo;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by HLB on 2017/3/21.
 *监听通知栏  AccessibilityService辅助服务
 */
public class NeNotificationService extends AccessibilityService {

    private static String qqpimsecure = "com.tencent.qqpimsecure";
    String TAG = "NeNotificationService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            if (event.getPackageName().toString().equals("com.tencent.mm")) {
                List<CharSequence> texts = event.getText();
                for (CharSequence text : texts) {
                    String content = text.toString();
                    if (!TextUtils.isEmpty(content)) { //需要在此处理微信数据，然后广播出去，在首页中显示出来。
                        String msgUserame = "";
                        String msgContent = "";
                        if (content.contains(":")) {
                            msgUserame = content.substring(0, content.indexOf(':'));
                            msgContent = content.substring(content.indexOf(':') + 1);
                            Intent intent = new Intent(MainActivity.ACTION_NOTIFICATION);
                            intent.putExtra("WechatMsgUserName", msgUserame);
                            intent.putExtra("WechatMsgContent", msgContent);
                            sendBroadcast(intent);
                        }
                    }
                }
            }
        } else {
        }


    }


    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {

    }
}
