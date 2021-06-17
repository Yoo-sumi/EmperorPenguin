#!/usr/bin/env python

# This file saves the full images and creates labelled files about target

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

Width = 640
Height = 480

arr = Int32MultiArray()
arr.data = [0,0]
count = 0


def img_callback(data):
    global image3
    image3 = bridge.imgmsg_to_cv2(data, "bgr8")


def signal_handler(sig, frame):
    time.sleep(2)
    os.system('killall -9 python rosout')
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)


box_data = []

def init_node():
    rospy.init_node('detect_person')
    rospy.Subscriber('/darknet_ros/bounding_boxes', BoundingBoxes, callback)
    rospy.Subscriber("/usb_cam/image_raw", Image, img_callback)


def callback(msg):
    global box_data
    box_data = msg


def exit_node():
    print('finished')


if __name__ == '__main__':

    time.sleep(15)
    
    init_node()
    
    while not rospy.is_shutdown():
        boxes = box_data

        if not boxes:
            continue

        for i in range(len(boxes.bounding_boxes)):
            if boxes.bounding_boxes[i].Class == 'person':
                  xmin=boxes.bounding_boxes[i].xmin
                  ymin=boxes.bounding_boxes[i].ymin
                  xmax=boxes.bounding_boxes[i].xmax
                  ymax=boxes.bounding_boxes[i].ymax

		  arr.data = [xmin,ymin,xmax,ymax]
		  rospy.loginfo(arr)

                  path = '/home/hansung/workspace/catkin_ws/src/darknet/data/img/image%04d.jpg' % (count)
		  
		  cv2.imwrite(path, image3)

		  labelX = (xmax + xmin) / 2.0 / float(Width)
		  labelY = (ymax + ymin) / 2.0 / float(Height)
		  labelW = (xmax - xmin) / float(Width)
		  labelH = (ymax - ymin) / float(Height)

		  label = "0 %0.6f %0.6f %0.6f %0.6f" % (labelX, labelY, labelW, labelH)
		  f = open('/home/hansung/workspace/catkin_ws/src/darknet/data/img/image%04d.txt' % (count), 'w')
		  f.write(label)
		  f.close()

		  roi=image3[ymin:ymax,xmin:xmax]
                  print(type(roi))

		  count+=1

    rospy.on_shutdown(exit_node)
