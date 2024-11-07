#include <Joystick.h>

const int potPin = A0; //Pin A0, aka the first analog pin to which the potentiometer is attached

void setup() {
  Joystick.begin(); //Initialize the joystick library
}

void loop() {
  int rawValue = analogRead(potPin); //Read the value of the potentiometer 
  int value = 255 - map(rawValue, 0, 1023, 0, 255); //Map the value of the pot between the min and max value
  Joystick.setThrottle(value); //Send the value to the joystick
}
