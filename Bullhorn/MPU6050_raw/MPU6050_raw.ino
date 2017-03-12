// I2C device class (I2Cdev) demonstration Arduino sketch for MPU6050 class
// 10/7/2011 by Jeff Rowberg <jeff@rowberg.net>
// Updates should (hopefully) always be available at https://github.com/jrowberg/i2cdevlib
//
// Changelog:
//     2011-10-07 - initial release

/* ============================================
I2Cdev device library code is placed under the MIT license
Copyright (c) 2011 Jeff Rowberg

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
===============================================
*/

// Arduino Wire library is required if I2Cdev I2CDEV_ARDUINO_WIRE implementation
// is used in I2Cdev.h
#include "Wire.h"

// I2Cdev and MPU6050 must be installed as libraries, or else the .cpp/.h files
// for both classes must be in the include path of your project
#include "I2Cdev.h"
#include "MPU6050.h"

// class default I2C address is 0x68
// specific I2C addresses may be passed as a parameter here
// AD0 low = 0x68 (default for InvenSense evaluation board)
// AD0 high = 0x69
MPU6050 accelgyro;

int16_t ax, ay, az;
int16_t gx, gy, gz;

#define LED_PIN 13
bool blinkState = false;
const int buttonPin = 4;
int buttonState = 0;  
int divi = 100;
int dely = 5;
int lastButtonState = LOW;

void setup() {
    // join I2C bus (I2Cdev library doesn't do this automatically)
    Wire.begin();

    // initialize serial communication
    // (38400 chosen because it works as well at 8MHz as it does at 16MHz, but
    // it's really up to you depending on your project)
    Serial.begin(9600);
    pinMode(buttonPin, INPUT);
    // initialize device
    //Serial.println("Initializing I2C devices...");
    accelgyro.initialize();
    accelgyro.setFullScaleAccelRange(0);
    // verify connection
    //Serial.println("Testing device connections...");
    //Serial.println(accelgyro.testConnection() ? "MPU6050 connection successful" : "MPU6050 connection failed");

    // configure Arduino LED for
    pinMode(LED_PIN, OUTPUT);
}


void sendNumber(int num){
  if(num<0){
    Serial.write('-');
    delay(dely);
    num = -num;
  }
  do{
    Serial.write(num%10);
    delay(dely);
    num/=10;
  }while(num!=0);
  Serial.write(',');
  delay(dely);
}

void loop() {
     buttonState = digitalRead(buttonPin);
     if(buttonState != lastButtonState){
        //envio informacion a node
        Serial.write(11);
        lastButtonState = buttonState;
     }
  // check if the pushbutton is pressed.
  // if it is, the buttonState is HIGH:
    if (buttonState == HIGH) {
      //accelgyro.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);
      accelgyro.getRotation(&gx, &gy, &gz);
      if(1){
//        //sendNumber(ax);
//        //sendNumber(ay);
//        //sendNumber(az);
          sendNumber(gx);
          sendNumber(gy);
          sendNumber(gz);
      }else{
        //Serial.print(ax/divi); Serial.print(" , ");
        //Serial.print(ay/divi); Serial.print(" , ");
        //Serial.print(az/divi); Serial.print(" , ");
        Serial.print(gx); Serial.print(" , ");
        Serial.print(gy); Serial.print(" , ");
        Serial.println(gz);
        
      }

//    Serial.write(0);
//    delay(dely);
//    Serial.write(5);
//    delay(dely);
//    Serial.write(-9);delay(dely);
//    Serial.write('-');delay(dely);
//    Serial.write(',');delay(dely);
    //Serial.write(100);delay(dely);
    //Serial.write(-5);delay(dely);
    //Serial.write(-50);delay(dely);
    //Serial.write("Test");delay(dely);
    //Serial.write("test2");delay(dely);
    //delay(8000);
    
    
  }
    //Serial.println("Boton Soltado");
  
    // read raw accel/gyro measurements from device
    
    // these methods (and a few others) are also available
    //accelgyro.getAcceleration(&ax, &ay, &az);
    //accelgyro.getRotation(&gx, &gy, &gz);
    int ax_, ay_, az_, gx_, gy_, gz_;
    ax_ = ax;
    ay_ = ay;
    az_ = az;
    gx_ = gx;
    gy_ = gy;
    gz_ = gz;
    // display tab-separated accel/gyro x/y/z values
    
    delay(300);

    // blink LED to indicate activity
    //blinkState = !blinkState;
    //digitalWrite(LED_PIN, blinkState);
}

