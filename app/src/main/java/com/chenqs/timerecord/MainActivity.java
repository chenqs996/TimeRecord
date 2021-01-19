package com.chenqs.timerecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button addRecordBTN;
    Button delRecordBTN;
    Button clsRecordBTN;
    static TextView timeRecordTV;


    static List<Long> timeStrList= new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MHandler.init(MainActivity.this);

        addRecordBTN = findViewById(R.id.AddRecordBTN);
        delRecordBTN = findViewById(R.id.DelRecordBTN);
        clsRecordBTN = findViewById(R.id.CLSRecordBTN);
        timeRecordTV = findViewById(R.id.TimeTV);


        addRecordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t = System.currentTimeMillis();
//                MHandler.GlobalMessage(MHandler.RECORD,(Object)t);
                timeStrList.add(t);
                MHandler.RecordUpdate();
            }
        });

        delRecordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeStrList!=null && timeStrList.size()>0)
                    timeStrList.remove(timeStrList.size() - 1);
                MHandler.RecordUpdate();
            }
        });

        clsRecordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeStrList!=null)
                    timeStrList.clear();
                MHandler.RecordUpdate();
            }
        });
    }

    public static void TextViewUpdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
        StringBuilder sb = new StringBuilder();
        int i =0;
        for(Long t:timeStrList) {
            String timeStr = sdf.format(new Date(t));
            sb.append(i+": ").append(timeStr).append('\n');
            i++;
        }
        timeRecordTV.setText(sb.toString());
    }

    static class MHandler extends Handler {
        public static final int RECORD_UPDATE = 509;
        public static final int TOAST = 981;

        private static Context context;

        private static MHandler mHandler;

        private MHandler(Context context) {
            MHandler.context = context;
        }

        public static void init(Context context){
            if(mHandler==null)
                mHandler = new MHandler(context);
        }

        public static MHandler getInstance() {
            return MHandler.mHandler;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case RECORD_UPDATE:
                    TextViewUpdate();
                    break;
                case TOAST:
                    Toast.makeText(context,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }

        public static void RecordUpdate() {
            getInstance().obtainMessage(RECORD_UPDATE).sendToTarget();
        }

        public static void GlobalToast(String s) {
            MHandler.getInstance().obtainMessage(TOAST,s).sendToTarget();
        }
        public static void GlobalMessage(int WHAT, Object obj) {
            getInstance().obtainMessage(WHAT,obj).sendToTarget();
        }
    }
}