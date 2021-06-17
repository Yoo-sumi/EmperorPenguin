#!/usr/bin/env python

import rospy, time, cv2, rospkg
from sensor_msgs.msg import Image
from darknet_ros_msgs.msg import BoundingBoxes
import signal
import sys
import os
import random
from cv_bridge import CvBridge, CvBridgeError
from std_msgs.msg  import Int32MultiArray
import numpy as np
import shutil

image3 = np.empty(shape=[0])
bridge = CvBridge()

ack_publisher = None
arr = Int32MultiArray()
arr.data = [] 
arr.data = [0 for i in range(4)] 
count = 0
past_arr = []
past_arr = [0 for i in range(4)] 
now_arr = []

def img_callback(data):
    global image3
    image3 = bridge.imgmsg_to_cv2(data, "bgr8")

def detect_color(frame):
    color=[0,0,255]
    pixel=np.uint8([[color]])

    hsv=cv2.cvtColor(pixel,cv2.COLOR_BGR2HSV)
    hsv=hsv[0][0]

    img_hsv=cv2.cvtColor(frame,cv2.COLOR_BGR2HSV)

    lower_red=(-10,100,100)
    upper_red=(10,255,255)

    lower_green=(50,100,100)
    upper_green=(70,255,255)

    img_red_mask=cv2.inRange(img_hsv,lower_red, upper_red)
    img_red_result=cv2.bitwise_and(frame,frame,mask=img_red_mask)

    img_green_mask=cv2.inRange(img_hsv,lower_green, upper_green)
    img_green_result=cv2.bitwise_and(frame,frame,mask=img_green_mask)

    count_red=cv2.countNonZero(img_red_mask)
    count_green=cv2.countNonZero(img_green_mask)
    
    if count_red > 0:
        print("red light!!!!!!!!!!!!")
    elif count_green > 0:
        print("green light!!!!!!!!!!!!!!")

    #cv2.imwrite('./img_result.JPG',img_result)
   

def signal_handler(sig, frame):
    time.sleep(2)
    os.system('killall -9 python rosout')
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)



box_data = []

def init_node():
    global ack_publisher

    rospy.init_node('detect_person')
    rospy.Subscriber('/darknet_ros/bounding_boxes', BoundingBoxes, callback)
    rospy.Subscriber("/usb_cam/image_raw", Image, img_callback)
    ack_publisher = rospy.Publisher('person_location', Int32MultiArray,queue_size=10)

def callback(msg):
    global box_data
    box_data = msg


def exit_node():
    print('finished')


if __name__ == '__main__':
    global image3, ack_publisher, count

    time.sleep(15)
    
    init_node()
    
    while not rospy.is_shutdown():
        boxes = box_data

        if not boxes:
            continue

        now_arr = []
        now_arr = [0 for i in range(len(boxes.bounding_boxes)*4)] 

        for i in range(len(boxes.bounding_boxes)):
            if boxes.bounding_boxes[i].Class == 'person':
                  xmin=boxes.bounding_boxes[i].xmin
                  ymin=boxes.bounding_boxes[i].ymin
                  xmax=boxes.bounding_boxes[i].xmax
                  ymax=boxes.bounding_boxes[i].ymax

		  #arr.data = [xmin,ymin,xmax,ymax]

                  now_arr[i*4+0]=xmin
                  now_arr[i*4+1]=ymin
                  now_arr[i*4+2]=xmax
                  now_arr[i*4+3]=ymax

        if count == 0:
             past_arr[0] = now_arr[0]
             past_arr[1] = now_arr[1]
             past_arr[2] = now_arr[2]
             past_arr[3] = now_arr[3]
             count += 1
             continue

                  #roi=image3[ymin:ymax,xmin:xmax]
		  #path = path = '/home/hansung/workspace/catkin_ws/src/darknet/data/img/image%04d.jpg' % (count)
		  #cv2.imwrite(path, roi)
		  #shutil.copy('/home/hansung/workspace/catkin_ws/src/detect_person/src/image.txt', '/home/hansung/workspace/catkin_ws/src/darknet/data/img/image%04d.txt' %(count))
                  #print(type(roi))
		  #count+=1
                  #detect_color(roi)
        print(past_arr)
        print(now_arr)
        sum_xy = []
        sum_xy = [0 for i in range(len(now_arr)/4)] 
        for i in range(len(sum_xy)):
            sum_xy[i] = abs(past_arr[0]-now_arr[i*4+0])+abs(past_arr[1]-now_arr[i*4+1])
        print(sum_xy)

        value_min = -1
        index = -1
        for i in range(len(sum_xy)):
            '''if value_min > sum_xy[i] or index == -1:
                value_min = sum_xy[i]
                index = i'''
            if sum_xy[i] < 10 :
                index = i
        print(index)
        if index != -1 and count != 0:
            arr.data[0] = now_arr[index*4+0]
            arr.data[1] = now_arr[index*4+1]
            arr.data[2] = now_arr[index*4+2]
            arr.data[3] = now_arr[index*4+3]

            past_arr[0] = now_arr[index*4+0]
            past_arr[1] = now_arr[index*4+1]
            past_arr[2] = now_arr[index*4+2]
            past_arr[3] = now_arr[index*4+3]
            ack_publisher.publish(arr)
        rospy.loginfo(past_arr)

    rospy.on_shutdown(exit_node)
