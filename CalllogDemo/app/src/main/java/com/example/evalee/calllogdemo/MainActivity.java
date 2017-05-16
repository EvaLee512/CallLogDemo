package com.example.evalee.calllogdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends PermissionActivity {
    Button btn;
    TextView tv_callHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.query);
        tv_callHistory=(TextView)findViewById(R.id.id_tv_content1);
    }

    public void btnQueryClick(View v){
        switch (v.getId()){
            case R.id.query:
                //获取通话记录
                ContentResolver  cr = MainActivity.this.getContentResolver();
                String callHistoryListStr=getCallHistoryList(null, cr);
                tv_callHistory.setTextSize(9.0f);
                tv_callHistory.setText(callHistoryListStr);
        }
    }

    public String getCallHistoryList(Context context, ContentResolver cr){

        Cursor cs = cr.query(Uri.parse("content://call_log/calls"),new String[]{
                "name",
                "number",
                "type",  //呼入/呼出(2)/未接
                "date",
                "duration"   //通话时长
        },null,null,"date DESC");

        String callHistoryListStr="";
        int i=0;
        if(cs!=null &&cs.getCount()>0){
            for(cs.moveToFirst();!cs.isAfterLast() & i<50; cs.moveToNext()){
                String callName=cs.getString(0);
                String callNumber=cs.getString(1);
                //通话类型
                int callType=Integer.parseInt(cs.getString(2));
                String callTypeStr="";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr="呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr="呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr="未接";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate=new Date(Long.parseLong(cs.getString(3)));
                String callDateStr=sdf.format(callDate);
                //通话时长
                int callDuration=Integer.parseInt(cs.getString(4));
                int min=callDuration/60;
                int sec=callDuration%60;
                String callDurationStr=min+"分"+sec+"秒";
                String callOne="类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
                        +callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
                        +"\n---------------------\n";

                callHistoryListStr+=callOne;
                i++;
            }
        }

        return callHistoryListStr;
    }



}
