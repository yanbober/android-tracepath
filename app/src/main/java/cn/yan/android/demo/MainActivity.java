package cn.yan.android.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.yan.android.tracepath.AndroidTracePath;
import cn.yan.android.demo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mETHostname;
    private Button mBTStart;
    private TextView mTVInfo;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private AndroidTracePath mAndroidTracePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETHostname = findViewById(R.id.et_host_name);
        mBTStart = findViewById(R.id.bt_start_trace);
        mTVInfo = findViewById(R.id.tv_result_info);

        mBTStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBTStart) {
            testTraceAsync();
        }
    }

    private void testTraceAsync() {
        new Thread() {
            @Override
            public void run() {
                mAndroidTracePath = new AndroidTracePath(new AndroidTracePath.StateListener() {
                    @Override
                    public void onStart() {
                        callbackStart();
                    }

                    @Override
                    public void onUpdate(String update) {
                        callbackUpdate(update);
                    }

                    @Override
                    public void onEnd() {
                        callbackEnd();
                    }
                });
                mAndroidTracePath.startTrace(mETHostname.getText().toString());
            }
        }.start();
    }

    void callbackStart() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mBTStart.setEnabled(false);
                mETHostname.setEnabled(false);
                mTVInfo.setText("");
            }
        });
    }

    void callbackUpdate(String update) {
//        mMainHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mTVInfo.setText(String.format("%s\n%s\n", mTVInfo.getText(), update));
//            }
//        });
    }

    void callbackEnd() {
//        mMainHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mBTStart.setEnabled(true);
//                mETHostname.setEnabled(true);
//            }
//        });
    }
}