#!/usr/bin/env python
# -*- coding: utf-8 -*-

import rospy, time
from motor.msg import motor

Angle = 90
Thrust = 0 # 0(STOP), 1(GO), 2(BOOST), 3(BACK)

pub = None


def motor_callback(data):
	global Angle, Thrust
	
	Angle = data.angle
	Thrust = data.thrust


def drive(angle, thrust):
	global pub

	msg = motor()

	msg.angle = angle
	msg.thrust = thrust 

	sndData = str.format("M, {}, {}", angle, thrust)
	rospy.loginfo(sndData)

	pub.publish(msg)


def init_node():
	global pub

	rospy.init_node('motor', anonymous=True)
	rospy.Subscriber('play_drive', motor, motor_callback)
	pub = rospy.Publisher('motor', motor, queue_size=10)


def main():
	global Angle, Thrust

	angle = Angle
	speed = Thrust

	if Thrust == 1: # GO
		speed = 71
	elif Thrust == 2: # BOOST
		speed = 67
	elif Thrust == 3: # BACK
		for stop_cnt in range(10):
			drive(90, 90)

		for stop_cnt in range(4):
			drive(90, 90)
			time.sleep(0.1)
			drive(90, 93)
			time.sleep(0.1)

		for back_cnt in range(30):
			drive(90, 97)
			time.sleep(0.1)
		return
	else:
		speed = 90

	drive(angle, speed)


if __name__ == '__main__':

	init_node()

	try:
		while not rospy.is_shutdown():
			main()
	except rospy.ROSInterruptException:
		pass
