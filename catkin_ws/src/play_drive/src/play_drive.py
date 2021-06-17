#!/usr/bin/env python
# -*- coding: utf-8 -*-

import rospy
from motor.msg import motor
import threading
from std_msgs.msg import Int32MultiArray
from std_msgs.msg import Int16MultiArray
from std_msgs.msg import Int32

WIDTH = 640
HEIGHT = 480

STOP = 0
GO = 1
BOOST = 2
BACK = 3

ANGLE_RATIO = 0.19

Target = [-1, -1, -1, -1] # [x, y, width, height]
Ultra = [-1, -1, -1, -1] # [left, left-front, front, right-front]
Wifi = -1 # 50(STOP) 150(GO) 250(BOOST) 300

Angle = 90
Thrust = STOP

pub = None


def location_callback(data):
	global Target
	Target = data.data


def ultra_callback(data):
	global Ultra
	Ultra = data.data


def wifi_callback(data):
	global Wifi
	Wifi = data.data


def th_motor():
	global Angle, Thrust
	global pub

	r = rospy.Rate(10)

	while True:
		msg = motor()

		msg.angle = Angle
		msg.thrust = Thrust 

		pub.publish(msg)

		sndData = str.format("P, {}, {}", Angle, Thrust)
		rospy.loginfo(sndData)

		r.sleep()


def decide_thrust():
	global GO, STOP, BACK, BOOST
	global Target

	thrust = STOP

	# waiting for subscribing target values
	if Target[0] == -1:
		return thrust

	# ultrasonic
	if Ultra[2] >= 0 and (Ultra[2] < 25 or Ultra[2] < 25 or Ultra[3] < 25):
		thrust = BACK
	elif Ultra[2] >= 0 and (Ultra[2] < 40 or Ultra[2] < 40 or Ultra[3] < 40):
		thrust = STOP
	else:	
		# wifi
		if Wifi == 150:
			thrust = GO
		elif Wifi == 250:
			thrust = BOOST
		else:
			thrust = STOP

		return thrust


def decide_angle():
	global WIDTH, ANGLE_RATIO
	global Target

	angle = 90

	# go (detect target)
	if Target[0] != -1 and Target[2] != -1:
		center = Target[0] + Target[2] / 2
		angle = -(WIDTH / 2 - center)
		angle = angle * ANGLE_RATIO
		angle = angle + 90
		angle = int(angle)

	return angle


def init_node():
	global pub

	rospy.init_node('play_drive')

	rospy.Subscriber('person_location', Int32MultiArray, location_callback)
	rospy.Subscriber('wave1', Int16MultiArray, ultra_callback)
	rospy.Subscriber('wifi_signal', Int32, wifi_callback)

	pub = rospy.Publisher('play_drive', motor, queue_size=10)


def main():
	global Angle, Thrust

	Thrust = decide_thrust()
	
	if Thrust == STOP:
		Angle = 90
	else:
		Angle = decide_angle()


if __name__ == '__main__':

	init_node()

	threading.Thread(target=th_motor).start()

	try:
		while not rospy.is_shutdown():
			main()
	except rospy.ROSInterruptException:
		pass
