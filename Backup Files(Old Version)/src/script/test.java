//#include <AccelStepper.h>
//#define HALFSTEP 8
//
//// motor pins
//#define motorPin1  8     // IN1 on ULN2003 ==> Blue   on 28BYJ-48  
//#define motorPin2  9     // IN2 on ULN2004 ==> Pink   on 28BYJ-48  
//#define motorPin3  10    // IN3 on ULN2003 ==> Yellow on 28BYJ-48  
//#define motorPin4  11    // IN4 on ULN2003 ==> Orange on 28BYJ-48  
//
//// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper with 28BYJ-48
//AccelStepper stepper1(HALFSTEP, motorPin1, motorPin3, motorPin2, motorPin4);
//
//// variables
//int turnSteps = 2100; // number of steps for a 90 degree turn
//int lineSteps = -6600; //number of steps to drive straight
//int stepperSpeed = 1000; //speed of the stepper (steps per second)
//int steps1 = 0; // keep track of the step count for motor 1
//int endPoint = 1024;        // Move this many steps - 1024 = approx 1/4 turn  
//
//boolean turn1 = false; //keep track if we are turning or going straight next
//
//
//void setup() {
//void setup()  
//{  
//  Serial.begin(9600);  
//  Serial.println(stepper1.currentPosition());  
//  delay(5000);  
//  stepper1.setMaxSpeed(1000.0);  
//  stepper1.setAcceleration(100.0);  
//  stepper1.setSpeed(200);  
//  stepper1.moveTo(endPoint);  
//}  
//
//}
//
// void serialEvent() {
//  while (Serial.available()) {
//    
//    char inChar = (char)Serial.read(); 
//    inputString += inChar;
//    if (inChar == '\n') {
//      stringComplete = true;
//                                    }
//   Serial.flush();
//    //end of while()
//                                     }
//    //end of serialEvent
//                    }
//void loop() {
//     if(stringComplete){
//             String name = "Hello "+ inputString; 
//             inputString = "";
//             Serial.println(name);
//             stringComplete = false;
//                                 }
//             }
//  steps1 = stepper1.distanceToGo();
//  stepper1.runSpeedToPosition();
//
//    // confirm values received in serial monitor window
//    Serial.print("--Arduino received: ");
//    Serial.println(recv);
//}

