#include <ros.h>
#include <std_msgs/String.h>

const int Passive_BUZZER = 10;

ros::NodeHandle  nh;

std_msgs::String str_msg;

void callback_bell( const std_msgs::String& msg);

ros::Subscriber<std_msgs::String> sub("joystick_find", &callback_bell);

void callback_bell( const std_msgs::String& msg){
  tone(Passive_BUZZER,330);
  delay(500);
  noTone(Passive_BUZZER);
  tone(Passive_BUZZER,330);
  delay(500);
  noTone(Passive_BUZZER);
}

void setup()
{
  nh.initNode();
  nh.subscribe(sub);
}
void loop()
{  
  nh.spinOnce();
}