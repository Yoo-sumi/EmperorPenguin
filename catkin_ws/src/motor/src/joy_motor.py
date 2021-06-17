#!/usr/bin/env python
# -*- coding: utf-8 -*

import rospy
import time
from std_msgs.msg import Int32
from std_msgs.msg import String
from motor.msg import motor

msg_motor = motor()
motor_publisher = None

def callback_speed(msg_speed):
	global msg_motor
	if msg_speed.data == "go1":
		msg_motor.thrust = 75
	elif msg_speed.data == "go2":
		msg_motor.thrust = 71
	elif msg_speed.data == "go3":
		msg_motor.thrust = 68
	elif msg_speed.data == "back1":
		msg_motor.thrust = 105
	elif msg_speed.data == "back2":
		msg_motor.thrust = 109
	elif msg_speed.data == "back3":
		msg_motor.thrust = 112
	else:
		msg_motor.thrust = 90


def callback_angle(msg_angle):
	global msg_motor
	msg_motor.angle = msg_angle.data + 90

def talker():
	global motor_publisher, msg_motor

	rospy.init_node('joystick')
	rospy.Subscriber('joystick_state', String, callback_speed)
	rospy.Subscriber('joystick_rotate', Int32, callback_angle)
	motor_publisher = rospy.Publisher('motor', motor, queue_size = 10)

	while not rospy.is_shutdown():
		motor_publisher.publish(msg_motor)
		time.sleep(0.01)

if __name__ == '__main__':
	try:
		talker()
	except rospy.ROSInterruptException:
		pass
