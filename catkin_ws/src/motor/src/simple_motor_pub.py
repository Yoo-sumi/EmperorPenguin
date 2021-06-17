#!/usr/bin/env python
# -*- coding: utf-8 -*-

import rospy
from motor.msg import motor
from std_msgs.msg  import Int32MultiArray

Width = 640
Height = 480

Angle = 60
Thrust = 90

Target = [-1, -1, -1, -1]
angle_ratio = 0.19


def location_callback(data):
	global Target
	Target = data.data


def drive(angle, thrust):
	msg = motor()

	msg.angle = angle
	msg.thrust = thrust 

	sndData = str.format("R, {}, {}", angle, thrust)
	rospy.loginfo(sndData)

	pub.publish(msg)


def adjust_angle(target):
	global Width

	# stop (no target)
	if target[0] == -1 or target[2] == -1:
		angle = 90
		thrust = 90

	# go (detect target)
	else:
		center = target[0] + target[2] / 2
		angle = -(Width / 2 - center)
		angle = angle * angle_ratio
		angle = angle + 90
		angle = int(angle)
		thrust = 75

	drive(angle, thrust)


if __name__ == '__main__':

	rospy.init_node('motor', anonymous=True)
	pub = rospy.Publisher('motor', motor, queue_size=10)
	rospy.Subscriber('person_location', Int32MultiArray, location_callback)

	try:
		while not rospy.is_shutdown():
			adjust_angle(Target)
	except rospy.ROSInterruptException:
		pass
