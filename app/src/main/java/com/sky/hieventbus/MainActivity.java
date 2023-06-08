package com.sky.hieventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static TextView messageTv;
    private static Button nextBtn;
    private static Button stickyBtn;
    private static String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        // 1. 普通事件-注册
        EventBus.getDefault().register(this);

        messageTv = findViewById(R.id.message_tv);
        nextBtn = findViewById(R.id.to_next_btn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked");
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        stickyBtn = findViewById(R.id.send_sticky_btn);
        stickyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 粘性事件-发送消息
                EventBus.getDefault().postSticky(new StickyEvent("I am stick message"));
            }
        });
    }

    // 3. 普通事件-接收消息
    /**
     *
     * @param event
     * ThreadMode.MAIN：主线程执行
     * ThreadMode.MAIN_ORDERED：主线程执行，消息按照队列发送，保证post调用不会阻塞
     * ThreadMode.POSTING：默认值，订阅方法将在post线程中被直接调用，事件交付意味着最少的开销，因为它完全避免了线程切换，
     *                    对于已知可以在很短时间内完成而不需要主线程的简单任务，推荐使用这种模式，但是必须快速返回，以避免阻塞post线程(可能是主线程)
     * ThreadMode.BACKGROUND：在Android上，如果post线程非主线程，订阅方法直接在post线程执行，如果post线程是主线程，EventBus在一个后台线程依次发送消息
     *                      使用此模式的订阅者应该快速返回，以防止阻塞后台线程；如果不在Android上，则总是后台线程执行
     * ThreadMode.ASYNC：订阅者将总是在一个独立于主线程和post线程的单独线程被调用，post事件不会等待订阅者，订阅方法如果耗时如访问网络，应该使用此模式
     *                   避免同时触发大量长时间运行的异步订阅者方法，以限制并发线程的数量，EventBus使用线程池来有效地重用已完成的异步订阅者通知中的线程
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event)
    {
        Log.d(TAG, "onMessageEvent: -----------");
        // 接收很快，但是刷新较慢，如果在此setText，则看不到更新
        message = event.getMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageTv.setText(message);
    }

    @Override
    protected void onDestroy() {
        // 4. 普通事件-解注册
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}