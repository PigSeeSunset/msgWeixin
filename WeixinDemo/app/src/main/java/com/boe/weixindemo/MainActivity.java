package com.boe.weixindemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //微信消息Action
    public static String ACTION_NOTIFICATION = "com.boe.weixindemo.NeNotificationService";//通知消息Action

    Button tv_main;

    View viewWeiXin;
    RelativeLayout rl;
    TextView tv_weixin_content, tv_weixin_username;
    private Handler handler = new Handler();
    private PopupWindow popupWindowWeiXin;


    // 新微信
    Runnable newWeixinRunnable = new Runnable() {

        public void run() {
            if (popupWindowWeiXin != null) {
                popupWindowWeiXin.dismiss();
                popupWindowWeiXin = null;
            }
        }
    };


    //广播接受信息

    BroadcastReceiver mNewMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(ACTION_NOTIFICATION)){
                String msgusername = intent.getStringExtra("WechatMsgUserName");
                String msgcontent = intent.getStringExtra("WechatMsgContent");
                showWindow(msgusername,msgcontent);
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_main = (Button) findViewById(R.id.tv_main);
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNewMsgReceiver, makeNewMsgIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();

         unregisterReceiver(mNewMsgReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (popupWindowWeiXin != null) {
            popupWindowWeiXin.dismiss();
            popupWindowWeiXin = null;
            handler.removeCallbacks(newWeixinRunnable);
        }
    }


    /**
     * 广播过滤器
     */
    private static IntentFilter makeNewMsgIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();


        intentFilter.addAction(ACTION_NOTIFICATION);
        return intentFilter;
    }


    private void showWindow(String msgUserName,String msgContent){
        if(popupWindowWeiXin !=null && popupWindowWeiXin.isShowing()){
            popupWindowWeiXin.dismiss();
            popupWindowWeiXin =null;
        }

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewWeiXin =layoutInflater.inflate(R.layout.popwindow_weixin,null);
        rl = (RelativeLayout) viewWeiXin.findViewById(R.id.ll_weixin);


        tv_weixin_content = (TextView) viewWeiXin.findViewById(R.id.tv_weixin_content);
        tv_weixin_username = (TextView) viewWeiXin.findViewById(R.id.tv_weixi_username);
        tv_weixin_username.setText(msgUserName);
        tv_weixin_content.setText(msgContent);


        //创建一个PopuWindow 对象
        popupWindowWeiXin =new PopupWindow(viewWeiXin,800,400);

        //聚焦
        popupWindowWeiXin.setFocusable(false);

        //设置允许在外点击消失
        popupWindowWeiXin.setOutsideTouchable(true);

        popupWindowWeiXin.showAtLocation(findViewById(R.id.main_weinxi), Gravity.CENTER, 0, 0);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindowWeiXin.getWidth() / 2;

        popupWindowWeiXin.showAsDropDown(viewWeiXin, xPos, 0);
        handler.postDelayed(newWeixinRunnable, 20000);
    }
}
