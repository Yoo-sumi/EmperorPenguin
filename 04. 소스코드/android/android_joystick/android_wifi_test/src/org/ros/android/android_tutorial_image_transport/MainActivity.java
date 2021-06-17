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

import std_msgs.Int32;

/*import static android.os.SystemClock.sleep;*/


/**
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

    private RosImageView<sensor_msgs.CompressedImage> image;
    Button btn_main;
    private Talker state;
    private Rotate rotate;
    private Find joystick_find;
    private Button go_button, btn_find;
    private Button back_button,speed_level;
    private double general_x, general_y;
    private double start_x,start_y,start_dist,start_ce;
    private double end_x,end_y,end_dist,end_ce;
    private float rotation=0;
    private ImageView handle,blur;
    public int flag_rotate=0, flag_state=0, flag_find=0;
    public static Context context_main;


    private int flag = 0;
    private int count=1;
    //private SignalThread signalThread;

    public MainActivity() {
        super("ImageTransportTutorial", "ImageTransportTutorial");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        image = (RosImageView<sensor_msgs.CompressedImage>) findViewById(R.id.image);
        image.setTopicName("/usb_cam/image_raw/compressed");
        image.setMessageType(sensor_msgs.CompressedImage._TYPE);
        image.setMessageToBitmapCallable(new BitmapFromCompressedImage());

        go_button = findViewById(R.id.go_button);
        back_button = findViewById(R.id.back_button);
        speed_level = findViewById(R.id.speed_level);
        handle=(ImageView)findViewById(R.id.handle);
        blur=(ImageView)findViewById(R.id.blur);

        speed_level=(Button)findViewById(R.id.speed_level);
        speed_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==2) {
                    speed_level.setBackgroundResource(R.drawable.two);
                }
                else if(count==3){
                    speed_level.setBackgroundResource(R.drawable.three);
                }
                else {
                    speed_level.setBackgroundResource(R.drawable.one);
                    count=1;
                }

            }
        });

        btn_find=(Button)findViewById(R.id.btn_find);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_find=1;

            }
        });

        go_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    state.setMessage("go" + count);
                    flag_state=1;
                }
                else if(event.getAction()==MotionEvent.ACTION_UP) {
                    state.setMessage("stop");
                    flag_state=1;
                }
                return false;
            }
        });

        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    state.setMessage("back" + count);
                    flag_state=1;
                }
                else if(event.getAction()==MotionEvent.ACTION_UP) {
                    state.setMessage("stop");
                    flag_state=1;
                }
                return false;
            }
        });



        blur.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                general_x=view.getWidth()/2;
                general_y=view.getHeight()/2;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        handle.setRotation(0);
                        rotate.setMessage(0);
                        flag_rotate=1;
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        start_x=motionEvent.getX()-general_x;
                        start_y=-(motionEvent.getY()-general_y);
                        // 1 사분면
                        if(start_x>=0 && start_y>=0){
                            start_dist=Math.sqrt(Math.pow(Math.abs(start_x), 2) + Math.pow(Math.abs(start_y), 2));
                            start_ce = Math.acos(start_x / start_dist)*(180/Math.PI);
                        }
                        // 2 사분면
                        else if(start_x<0 && start_y>0){
                            start_dist=Math.sqrt(Math.pow(Math.abs(start_x), 2) + Math.pow(Math.abs(start_y), 2));
                            start_ce = Math.acos(Math.abs(start_x) / start_dist)*(180/Math.PI);

                        }
                        // 3 사분면
                        else if(start_x<0 && start_y<0){
                            start_dist=Math.sqrt(Math.pow(Math.abs(start_x), 2) + Math.pow(Math.abs(start_y), 2));
                            start_ce = Math.acos(Math.abs(start_x) / start_dist)*(180/Math.PI);

                        }
                        // 4 사분면
                        else if(start_x>0 && start_y<0){
                            start_dist=Math.sqrt(Math.pow(Math.abs(start_x), 2) + Math.pow(Math.abs(start_y), 2));
                            start_ce = Math.acos(start_x / start_dist)*(180/Math.PI);

                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        end_x=motionEvent.getX()-general_x;
                        end_y=-(motionEvent.getY()-general_y);

                        //Log.i("test","x: "+Double.toString(start_x)+", y: "+ Double.toString(start_y));
                        // 1 사분면
                        if(end_x>0 && end_y>0){
                            end_dist=Math.sqrt(Math.pow(Math.abs(end_x), 2) + Math.pow(Math.abs(end_y), 2));
                            end_ce = Math.acos(end_x / end_dist)*(180/Math.PI);
                        }
                        // 2 사분면
                        else if(end_x<0 && end_y>0){
                            end_dist=Math.sqrt(Math.pow(Math.abs(end_x), 2) + Math.pow(Math.abs(end_y), 2));
                            end_ce = Math.acos(Math.abs(end_x) / end_dist)*(180/Math.PI);

                        }
                        // 3 사분면
                        else if(end_x<0 && end_y<0){
                            end_dist=Math.sqrt(Math.pow(Math.abs(end_x), 2) + Math.pow(Math.abs(end_y), 2));
                            end_ce = Math.acos(Math.abs(end_x) / end_dist)*(180/Math.PI);

                        }
                        // 4 사분면
                        else if(end_x>0 && end_y<0){
                            end_dist=Math.sqrt(Math.pow(Math.abs(end_x), 2) + Math.pow(Math.abs(end_y), 2));
                            end_ce = Math.acos(end_x / end_dist)*(180/Math.PI);
                        }

                        //1
                        if(start_x>0 && start_y>0 && end_x>0 && end_y>0)
                            rotation = (float)(start_ce-end_ce);
                        else if(start_x>0 && start_y>0 && end_x>0 && end_y<0)
                            rotation = (float)(start_ce+end_ce);
                        else if(start_x>0 && start_y>0 && end_x<0 && end_y>0)
                            rotation = -(float)(180-(start_ce+end_ce));
                        else if(start_x>0 && start_y>0 && end_x<0 && end_y<0)
                            rotation = -(float)((90-start_ce)+(90+end_ce));
                            //2
                        else if(start_x<0 && start_y>0 && end_x>0 && end_y>0)
                            rotation = (float)(180-(start_ce+end_ce));
                        else if(start_x<0 && start_y>0 && end_x<0 && end_y<0)
                            rotation = -(float)(start_ce+end_ce);
                        else if(start_x<0 && start_y>0 && end_x<0 && end_y>0)
                            rotation = -(float)(start_ce-end_ce);
                        else if(start_x<0 && start_y>0 && end_x>0 && end_y<0)
                            rotation = (float)((90-start_ce)+(90+end_ce));
                            //3
                        else if(start_x<0 && start_y<0 && end_x<0 && end_y<0)
                            rotation = -(float)(end_ce-start_ce);
                        else if(start_x<0 && start_y<0 && end_x<0 && end_y>0)
                            rotation = (float)(start_ce+end_ce);
                        else if(start_x<0 && start_y<0 && end_x>0 && end_y>0)
                            rotation = (float)((90+start_ce)+(90-end_ce));
                        else if(start_x<0 && start_y<0 && end_x>0 && end_y<0)
                            rotation = -(float)(180-(start_ce+end_ce));
                            //4
                        else if(start_x>0 && start_y<0 && end_x>0 && end_y<0)
                            rotation = (float)(end_ce-start_ce);
                        else if(start_x>0 && start_y<0 && end_x>0 && end_y>0)
                            rotation = -(float)(start_ce+end_ce);
                        else if(start_x>0 && start_y<0 && end_x<0 && end_y>0)
                            rotation = -(float)((90+start_ce)+(90-end_ce));
                        else if(start_x>0 && start_y<0 && end_x<0 && end_y<0)
                            rotation = (float)(180-(start_ce+end_ce));


                        if(Math.abs(rotation)<=70) {
                            handle.setRotation(rotation);
                            rotate.setMessage((int)rotation);
                            flag_rotate=1;
                        }
                        return true;
                }
                return false;
            }
        });


    }

    public void changeFlagState(int n){
        flag_state=n;
    }

    public void changeFlagRotate(int n){
        flag_rotate=n;
    }

    public void changeFlagFind(int n){
        flag_find=n;
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {


        state = new Talker("joystick_state");
        state.setMessage("stop");

        rotate = new Rotate("joystick_rotate");
        rotate.setMessage(0);

        joystick_find = new Find("joystick_find");
        joystick_find.setMessage("find");

        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),
                        getMasterUri());

        nodeMainExecutor.execute(state, nodeConfiguration);
        nodeMainExecutor.execute(rotate, nodeConfiguration);
        nodeMainExecutor.execute(joystick_find, nodeConfiguration);
        nodeMainExecutor.execute(image, nodeConfiguration.setNodeName("android/video_view"));
    }


}


