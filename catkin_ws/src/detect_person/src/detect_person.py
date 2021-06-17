#!/usr/bin/env python

import rospy, time, cv2, rospkg
from sensor_msgs.msg import Image
from darknet_ros_msgs.msg import BoundingBoxes
import signal
import sys
import os
import random
import threading
from cv_bridge import CvBridge, CvBridgeError
from std_msgs.msg  import Int32MultiArray
import numpy as np
import shutil


ack_publisher = None
arr = Int32MultiArray()
arr.data = [-1, -1, -1, -1]

box_data = []


def th_target():
    global arr

    r = rospy.Rate(10)

    while True:	
	rospy.loginfo(arr)
	ack_publisher.publish(arr)

	r.sleep()


def th_init_box():
    global box_data

    while True:
        box_data = []
        time.sleep(2)


def box_callback(msg):
    global box_data
    box_data = msg


def signal_handler(sig, frame):
    time.sleep(2)
    os.system('killall -9 python rosout')
    sys.exit(0)


signal.signal(signal.SIGINT, signal_handler)


def init_node():
    global ack_publisher

    rospy.init_node('detect_person')
    rospy.Subscriber('/darknet_ros/bounding_boxes', BoundingBoxes, box_callback)
    ack_publisher = rospy.Publisher('person_location', Int32MultiArray,queue_size=10)


def exit_node():
    rospy.loginfo('shutdown \'detect_person\' node')


if __name__ == '__main__':

    time.sleep(15)
    
    init_node()

    threading.Thread(target=th_target).start()
    threading.Thread(target=th_init_box).start()
 

    while not rospy.is_shutdown():
        boxes = box_data

        if not boxes:
	    arr.data = [-1, -1, -1, -1]
            continue

        for i in range(len(boxes.bounding_boxes)):
            if boxes.bounding_boxes[i].Class == 'person':
                  xmin=boxes.bounding_boxes[i].xmin
                  ymin=boxes.bounding_boxes[i].ymin
                  xmax=boxes.bounding_boxes[i].xmax
                  ymax=boxes.bounding_boxes[i].ymax

		  arr.data = [xmin, ymin, xmax, ymax]


    rospy.on_shutdown(exit_node)

