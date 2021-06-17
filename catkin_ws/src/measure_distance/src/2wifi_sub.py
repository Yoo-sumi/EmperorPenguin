#!/usr/bin/env python

import rospy
from std_msgs.msg import Int32
from std_msgs.msg import Empty
import threading

moveDistance = 3
signal = [0, 0]
index = -1
pub = rospy.Publisher('toServo', Empty, queue_size=10)
pubToMotor = rospy.Publisher('toMotor', Int32, queue_size=10)
direction_signal = {}
flag = False
wifiSignal_data = 0

def callback(data):
	global wifiSignal_data, flag, index
	wifiSignal_data = data.data
	str = "callback %d"%flag
	rospy.loginfo(str)
	if flag: return
	
	index += 1
	signal.append(data.data)
	if(index > 0):
		if(abs(signal[index-1]-signal[index]) > moveDistance):
			flag = True
			str = "[%d, %d]"%(signal[index-1], signal[index])
			rospy.loginfo(str)
			talker_thread = threading.Thread(target=talker)
			talker_thread.start()
			#talker()

def listener():
	#rospy.loginfo("listener")
	rospy.init_node('wifi_signal', anonymous=True)
	rospy.Subscriber("wifi_signal", Int32, callback)
	
	rospy.spin()


def talker():
	global index, pub, flag, direction_signal
	pub.publish()
	rospy.sleep(5)
	flag = True
	directionArr = []
	for i in range(3):
		tmp = []
		for j in range(5):#10):
			rospy.loginfo("%d"%j)
			tmp.append(wifiSignal_data)
			rospy.sleep(1)#0.5)
		direction_signal[i] = tmp
		print(direction_signal[i])
#		directionArr.append(sum(direction_signal[i])/len(direction_signal[i]))
		directionArr.append(min(direction_signal[i]))
	print(directionArr)
	maxIndex = 0
	for i in range(3):
		if(directionArr[maxIndex] < directionArr[i]):
			maxIndex = i
	print("maxIndex = %d"%maxIndex)
	pubToMotor.publish(maxIndex)

	maxIndex = 0;
	del directionArr[:]
	rospy.loginfo("end of talker")
	
	index = -1 #reset
	del signal[:] #flush buffer
	flag = False
	return

if __name__=='__main__':
	listener()
