/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_tutorial_image_transport;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

/*import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;*/
import android.util.Log;
/*import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;*/
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.apache.xmlrpc.util.ThreadPool;
import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;

import std_msgs.Float32;
import std_msgs.Float64;
import std_msgs.Int16;
import std_msgs.Int32;

/*import static android.os.SystemClock.sleep;*/


/**
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

    private RosImageView<sensor_msgs.CompressedImage> image;
    Button btn_main, btn_find;
    private Find joystick_find;
    /*wifi signal*/
    private WifiSignal wifiSignal;
    private TextView mySignal;
    //private int myCount=1;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private int _rssi = 40;
    static int sendDistance = 300;
    public int flag = 0;
    private int count=1;

    public static Context context_main;
    //private SignalThread signalThread;

    public MainActivity() {
        super("ImageTransportTutorial", "ImageTransportTutorial");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context_main = this;

        image = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.image);
        image.setTopicName("/usb_cam/image_raw/compressed");
        image.setMessageType(sensor_msgs.CompressedImage._TYPE);
        image.setMessageToBitmapCallable(new BitmapFromCompressedImage());


        btn_main = findViewById(R.id.myCart_list);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("aa","aa");
                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                startActivity(intent);
            }
        });


        btn_find = findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
            }
        });


        /*wifi signal*/
        mySignal = (TextView)findViewById(R.id.signalTextView);
        //mySignal.setText("wifi signal 세기: 0");
        mySignal.setText("카트가 ???m 이내에 있습니다");
        signalThread st = new signalThread();
        st.start();
    }
    public void setSignalTextView(String str){
        mySignal.setText(str);
    }

    public void changeFlag(int n){
        flag = n;
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        wifiSignal = new WifiSignal("wifi_signal");
        wifiSignal.setMessage(100);

        joystick_find = new Find("joystick_find");
        joystick_find.setMessage("find");

        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),
                        getMasterUri());


        nodeMainExecutor.execute(wifiSignal, nodeConfiguration);
        nodeMainExecutor.execute(joystick_find, nodeConfiguration);
        nodeMainExecutor.execute(image, nodeConfiguration.setNodeName("android/video_view"));
    }
    private class signalThread extends Thread{
        int oldShowDistance = 0;
        @Override
        public void run() {

            while(true){
                wifiManager = (WifiManager) MainActivity.this.getApplicationContext().getSystemService(WIFI_SERVICE);
                wifiInfo = wifiManager.getConnectionInfo();
                _rssi = wifiInfo.getRssi();
                Log.i("thread",""+_rssi);
                int showDistance = 0;
                String str = "이내";
                /*if(_rssi >= -58){ //이게 진짜임.
                    showDistance = 50;
                }else if(_rssi <= -60 && _rssi >= -65){
                    showDistance = 150;
                }else if(_rssi < -68) {
                    showDistance = 250;
                }else if(_rssi <= -72){
                    showDistance = 300;
                    str = "밖";
                }else{
                    showDistance = oldShowDistance;
                }*/
                if(_rssi >= -45){
                    showDistance = 50;
                }else if(_rssi <= -50 && _rssi >= -55){
                    showDistance = 150;
                }else if(_rssi<-57 &&_rssi >= -60) {
                    showDistance = 250;
                }else if(_rssi<-65){ //&&_rssi >= -65){
                    showDistance = 300;
                    str = "밖";
                }else{
                    showDistance = oldShowDistance;
                }
                sendDistance = showDistance;
                oldShowDistance = showDistance;

                mySignal.setText(String.format("카트가 %.1fm %s에 있습니다", sendDistance*0.01, str));
            }
        }
    }
    /*wifiSignal 클래스: publisher임.*/
    class WifiSignal extends AbstractNodeMain {
        private String topic_name;
        private Integer message;

        public WifiSignal() {topic_name = "chatter";}

        public WifiSignal(String topic){topic_name = topic;}

        public void setMessage(Integer message){
            //this.message=message;

        }

        @Override
        public GraphName getDefaultNodeName() {
            return GraphName.of("android/signal");
        }

        @Override
        public void onStart(final ConnectedNode connectedNode) {
            final Publisher<Int32> publisher =
                    connectedNode.newPublisher(topic_name, std_msgs.Int32._TYPE);
            // This CancellableLoop will be canceled automatically when the node shuts
            // down.
            connectedNode.executeCancellableLoop(new CancellableLoop() {
                private int sequenceNumber;

                @Override
                protected void setup() {
                    sequenceNumber = 0;
                }

                @Override
                protected void loop() throws InterruptedException {
                    //message = _rssi;
                    std_msgs.Int32 val =  publisher.newMessage();
                    val.setData(sendDistance); //_rssi); //wifi 신호 세기 pub.
                    Log.i("PubLoop",""+_rssi);
                    publisher.publish(val);
                    Thread.sleep(500);
                }
            });
        }

    }

}


