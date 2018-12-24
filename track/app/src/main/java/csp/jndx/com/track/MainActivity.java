package csp.jndx.com.track;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String SMS_ACTION = "com.android.TinySMS.RESULT";
    private MapView mapView;
    private BaiduMap baiduMap;
    private ImageView imageView;
    private ToggleButton refreshButton;
    private LatLng cenpt=new LatLng(22.2559,113.541112);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//初始化要在setcontent前执行
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(cenpt).zoom(17).build();//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
        CircleOptions circleOptions = new CircleOptions();
        // 设置数据 以世界之窗为圆心，1000米为半径绘制
        circleOptions.center(cenpt)//中心
                .radius(30)  //半径
                .fillColor(ContextCompat.getColor(getBaseContext(), R.color.colorGreen))//填充圆的颜色
                .stroke(new Stroke(1, ContextCompat.getColor(getBaseContext(), R.color.colorGreen)));  //边框的宽度和颜色
        //把绘制的圆添加到百度地图上去
        baiduMap.addOverlay(circleOptions);
        addfriendmarker(new LatLng(22.2630,113.541320),"friend");
        addfriendmarker(new LatLng(22.2530,113.581320),"enermy");



        //刷新键
        refreshButton=(ToggleButton)findViewById(R.id.btn_refresh);
        refreshButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation anim =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.93f);
                // anim.setFillAfter(true); // 设置保持动画最后的状态
                anim.setDuration(3000); // 设置动画时间
                anim.setInterpolator(new LinearInterpolator()); // 设置插入器
                anim.setRepeatMode(Animation.RESTART);
                anim.setRepeatCount(0);
                if(b)
                {
                    imageView=(ImageView)findViewById(R.id.imageview_sweep) ;
                    imageView.startAnimation(anim);
                }
                else
                    anim.cancel();

                //刷新发送信息，代号333，请求地址
                sendShortMessage("333","please give me your position",getBaseContext());
            }
        });

        //好友键
        Button btn_friend=(Button)findViewById(R.id.btn_friends);
        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,FriendPageActivity.class);
                startActivity(intent);
            }
        });
        //敌人键
        Button ene_friend=(Button)findViewById(R.id.btn_enemies);
        ene_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EnermyPageActivity.class);
                startActivity(intent);
            }
        });
        //receiver.onReceive(getBaseContext(),getIntent());



    }
    //添加标点
    private void addfriendmarker(LatLng lng,String text)
    {
        if(text=="friend") {
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
            //准备 marker option 添加 marker 使用
            MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(lng);
            //获取添加的 marker 这样便于后续的操作
            Marker marker = (Marker) baiduMap.addOverlay(markerOption);
            OverlayOptions textOption = new TextOptions().fontSize(60).fontColor(ContextCompat.getColor(getBaseContext(), R.color.colorGreen)).text(text).rotate(0).position(lng);
            baiduMap.addOverlay(textOption);
            //添加连线
            List<LatLng> points = new ArrayList<>();
            points.add(lng);
            points.add(cenpt);
            OverlayOptions polyline = new PolylineOptions().width(10).color(ContextCompat.getColor(getBaseContext(), R.color.colorGreen)).points(points);
            baiduMap.addOverlay(polyline);
            double dis = Distance(cenpt.latitude, cenpt.longitude, lng.latitude, lng.longitude);
            OverlayOptions distance = new TextOptions().fontSize(50).fontColor(ContextCompat.getColor(getBaseContext(), R.color.colorGreen)).text(Double.toString((int) dis) + "米").rotate(1).position(getmiddle(cenpt, lng));
            baiduMap.addOverlay(distance);
        }
        else {
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.enemy_marker);
            //准备 marker option 添加 marker 使用
            MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(lng);
            //获取添加的 marker 这样便于后续的操作
            Marker marker = (Marker) baiduMap.addOverlay(markerOption);
            OverlayOptions textOption = new TextOptions().fontSize(60).fontColor(ContextCompat.getColor(getBaseContext(), R.color.colorRed)).text(text).rotate(0).position(lng);
            baiduMap.addOverlay(textOption);
            //添加连线
            List<LatLng> points = new ArrayList<>();
            points.add(lng);
            points.add(cenpt);
            OverlayOptions polyline = new PolylineOptions().width(10).color(ContextCompat.getColor(getBaseContext(), R.color.colorRed)).points(points);
            baiduMap.addOverlay(polyline);
            double dis = Distance(cenpt.latitude, cenpt.longitude, lng.latitude, lng.longitude);
            OverlayOptions distance = new TextOptions().fontSize(50).fontColor(ContextCompat.getColor(getBaseContext(), R.color.colorRed)).text(Double.toString((int) dis) + "米").rotate(1).position(getmiddle(cenpt, lng));
            baiduMap.addOverlay(distance);
        }

    }
    //获取距离

    private Double Distance(double lat1, double lng1,double lat2, double lng2) {
        Double R=6370996.81;  //地球的半径
        /*
         * 获取两点间x,y轴之间的距离
         */
        Double x = (lng2 - lng1)*Math.PI*R*Math.cos(((lat1+lat2)/2)*Math.PI/180)/180;
        Double y = (lat2 - lat1)*Math.PI*R/180;
        Double distance = Math.hypot(x, y);   //得到两点之间的直线距离
        return   distance;
    }
     private LatLng getmiddle(LatLng a,LatLng b)
     {
         return new LatLng((a.latitude+b.latitude)/2,(a.longitude+b.longitude)/2);
     }


    /*发送信息的函数*/

    public  void sendShortMessage(String phone,String content,Context context) {
        String phoneNo = "13160651030";
        String message = "This is testing,please send your location";
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS} , 0);
        //
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);

        }
        try {
            SmsManager smsManager = SmsManager.getDefault();
            List<String> texts = smsManager.divideMessage(message);
            for (String text : texts) {
                smsManager.sendTextMessage(phoneNo, null, text, null, null);
            }
            Toast.makeText(context, "send successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context,
                    "fail to send",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    public void MDestroy() {
        if(mapView!=null)mapView.onDestroy();
    }
    public void MResume() {
        if(mapView!=null)mapView.onResume();
    }
    public void MPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(mapView!=null)mapView.onPause();
    }
}
