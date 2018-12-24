package csp.jndx.com.track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class ReceiveMessage extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
               Log.d("main","number:" + msg.getOriginatingAddress()
                        + "   body:" + msg.getDisplayMessageBody() + "  time:"
                        + msg.getTimestampMillis());
                //在这里写自己的逻辑
                //代号333，发送回自己的地址
                if (msg.getOriginatingAddress().equals("333")) {
                    //TODO
                    //发送信息
                    String content ="22.2830/113.521320";//短信内容
                    String phone = "666";//电话号码
                    SmsManager sm = SmsManager.getDefault();
                    List<String> sms = sm.divideMessage(content);
                    for (String smslist :sms){
                        sm.sendTextMessage(phone,null,smslist,null,null);
                    }
                    Toast.makeText(context,
                            "Reply the message",
                            Toast.LENGTH_SHORT).show();
                }//代号666为获取朋友位置
                if(msg.getOriginatingAddress().equals("666"))
                {
                    Toast.makeText(context,
                            "Your friend position:"+msg.getDisplayMessageBody(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
