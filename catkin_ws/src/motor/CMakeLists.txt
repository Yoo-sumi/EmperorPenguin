cmake_minimum_required(VERSION 3.0.2)
project(motor)

find_package(catkin REQUIRED COMPONENTS
  message_generation
  message_runtime
  rospy
  std_msgs
)

add_message_files(
  FILES
  motor.msg
)

generate_messages(
  DEPENDENCIES
  std_msgs
)

catkin_package(
#  INCLUDE_DIRS include
#  LIBRARIES motor
#  CATKIN_DEPENDS rospy std_msgs
#  DEPENDS system_lib
)

include_directories(
# include
  ${catkin_INCLUDE_DIRS}
)
