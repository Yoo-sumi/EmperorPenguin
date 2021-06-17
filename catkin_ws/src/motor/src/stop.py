#!/usr/bin/env python
# -*- coding: utf-8 -*-

import rospy
from motor.msg import motor 

angle = 90
speed = 90

def talker():
	global angle, speed 

	pub = rospy.Publisher('motor', motor, queue_size=10)
	rospy.init_node('motor', anonymous=True)

	msg = motor()

	while not rospy.is_shutdown():
		msg.angle = angle
		msg.thrust = speed
		
		pub.publish(msg)

		rospy.sleep(2)


if __name__ == '__main__':
	try:
		talker()
	except rospy.ROSInterruptException:
		pass
