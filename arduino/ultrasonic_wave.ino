#include <ros.h>
#include <std_msgs/String.h>
#include <string.h>
#include <stdio.h>

ros::NodeHandle  nh;

std_msgs::String str_msg;
ros::Publisher wave1("wave1", &str_msg);
ros::Publisher wave2("wave2", &str_msg);
ros::Publisher wave3("wave3", &str_msg);
ros::Publisher wave4("wave4", &str_msg);

char value1[50];
char value2[50];
char value3[50];
char value4[50];

int trigPin1=2; // 초음파센서1 출력핀
int echoPin1=3; // 초음파센서1 입력핀

int trigPin2=4; // 초음파센서2 출력핀
int echoPin2=5; // 초음파센서2 입력핀

int trigPin3=6; // 초음파센서3 출력핀
int echoPin3=7; // 초음파센서3 입력핀

int trigPin4=8; // 초음파센서4 출력핀
int echoPin4=9; // 초음파센서4 입력핀

float duration1;
float distance1;

float duration2;
float distance2;

float duration3;
float distance3;

float duration4;
float distance4;

void setup() {
  nh.initNode();
  nh.advertise(wave1);
  nh.advertise(wave2);
  nh.advertise(wave3);
  nh.advertise(wave4);

  pinMode(trigPin1,OUTPUT);
  pinMode(echoPin1,INPUT);
  pinMode(trigPin2,OUTPUT);
  pinMode(echoPin2,INPUT);
  pinMode(trigPin3,OUTPUT);
  pinMode(echoPin3,INPUT);
  pinMode(trigPin4,OUTPUT);
  pinMode(echoPin4,INPUT);
}
void ultrasonic1(){
  digitalWrite(trigPin1,HIGH);
  delay(10);
  digitalWrite(trigPin1,LOW);

  duration1=pulseIn(echoPin1,HIGH);

  distance1=((float)(340*duration1)/10000)/2;

}
void ultrasonic2(){
  digitalWrite(trigPin2,HIGH);
  delay(10);
  digitalWrite(trigPin2,LOW);

  duration2=pulseIn(echoPin2,HIGH);

  distance2=((float)(340*duration2)/10000)/2;

}
void ultrasonic3(){
  digitalWrite(trigPin3,HIGH);
  delay(10);
  digitalWrite(trigPin3,LOW);

  duration3=pulseIn(echoPin3,HIGH);

  distance3=((float)(340*duration3)/10000)/2;

}

void ultrasonic4(){
  digitalWrite(trigPin4,HIGH);
  delay(10);
  digitalWrite(trigPin4,LOW);

  duration4=pulseIn(echoPin4,HIGH);

  distance4=((float)(340*duration4)/10000)/2;
}

void loop() {
  ultrasonic1();
  ultrasonic2();
  ultrasonic3();
  ultrasonic4();
  
  if(distance1<=150){
    sprintf(value1,"distance: %d",(int)distance1);
    str_msg.data = value1;
    wave1.publish( &str_msg );
    nh.spinOnce();
  }
  if(distance2<=150){
    sprintf(value2,"distance: %d",(int)distance2);
    str_msg.data = value2;
    wave2.publish( &str_msg );
    nh.spinOnce();
  }
  if(distance3<=150){
    sprintf(value3,"distance: %d",(int)distance3);
    str_msg.data = value3;
    wave3.publish( &str_msg );
    nh.spinOnce();
  }
  if(distance4<=150){
    sprintf(value4,"distance: %d",(int)distance4);
    str_msg.data = value4;
    wave4.publish( &str_msg );
    nh.spinOnce();
  }
  
  delay(500);
}
