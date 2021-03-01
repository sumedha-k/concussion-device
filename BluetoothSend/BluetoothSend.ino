#include <SoftwareSerial.h>
SoftwareSerial HM10(2, 3); //pins for the bluetooth module
int inputPin = 10; //for the button in my circuit
int out = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);//the computer serial plotter
  HM10.begin(9600);//the bluetooth moduel serial plotter
  pinMode(inputPin, INPUT);
}

void loop() {
  if(digitalRead(inputPin) == HIGH) {
    out = 45;
  } else {
    out = 0;
  }
  HM10.print(out); //This is the command to send data to the bluetooth module
  Serial.println(out); //This is jsut to keep track of what is supposed to be printed
  delay(10); //not really necessary
}
