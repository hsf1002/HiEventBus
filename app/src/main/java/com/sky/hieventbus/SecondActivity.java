package com.sky.hieventbus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SecondActivity extends AppCompatActivity {
    private static Button tofirstBtn;
    private static Button receiveBtn;
    private static TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        tofirstBtn = findViewById(R.id.to_first_btn);
        tofirstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2. 普通事件-发送消息
                EventBus.getDefault().post(new MessageEvent("message from second activiy"));
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
                finish();
            }
        });

        receiveBtn = findViewById(R.id.receive_sticky_btn);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2. 粘性事件-注册
                EventBus.getDefault().register(SecondActivity.this);
            }
        });

        textView = findViewById(R.id.receive_sticky_tv);
    }

    // 3. 粘性事件-接收消息
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void stickyMessageEvent(StickyEvent event)
    {
        textView.setText(event.getMessage());
    }

    @Override
    protected void onDestroy() {
        // 4. 粘性事件-解注册
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
