#include <Servo.h>ex
#include <ros.h>
#include <motor/motor.h>

#define motorAnglePin 9
#define motorThrustPin 10

#define sensorValue 8

ros::NodeHandle nh;

Servo motorAngle; // steering - steering motor
Servo motorThrust; // speed - speed motor


void motor_cb(const motor::motor &msg)
{ 
  char my_log[256];
  sprintf(my_log, "angle, thrust [%d, %d] subscribed", msg.angle, msg.thrust);
  nh.logwarn(my_log);
  
  motorAngle.write(msg.angle);
  motorThrust.write(msg.thrust);
}

ros::Subscriber<motor::motor> sub("motor", motor_cb);

void setup() {  
  nh.initNode();
  nh.subscribe(sub);
  
  pinMode(sensorValue, INPUT);
  
  motorAngle.attach(motorAnglePin); // attaches the servo on pin 10 to the servo object (angle)
  motorThrust.attach(motorThrustPin); // attaches the servo on pin 9 to the servo object (speed)
}

void loop() {
  nh.spinOnce();
  delay(15);
}
