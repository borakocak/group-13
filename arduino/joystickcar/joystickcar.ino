#include <vector>
#include <Smartcar.h>
#include <Servo.h>
#include <MQTT.h>
#include <WiFi.h>
#ifdef __SMCE__
#include <OV767X.h>
#endif


#ifndef __SMCE__
WiFiClient net;
#endif

MQTTClient mqtt;

const char host[] = "broker.emqx.io";
const char topic[] = "/Group/13/#";
const char moveTopic[] = "/Group/13/Move";
const char turnTopic[] = "/Group/13/Turn";

const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 1000;
const auto pulsesPerMeter = 600;
int initialSensorDistance = 0;
int laterSensorDistance =0;
int FrontDistance = 0;

std::vector<char> frameBuffer;

 
ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins); 
DirectionlessOdometer leftOdometer{
    arduinoRuntime,
    smartcarlib::pins::v2::leftOdometerPin,
    []() { leftOdometer.update(); },
    pulsesPerMeter};
DirectionlessOdometer rightOdometer{
    arduinoRuntime,
    smartcarlib::pins::v2::rightOdometerPin,
    []() {rightOdometer.update(); },
    pulsesPerMeter};
DifferentialControl control(leftMotor, rightMotor); 
GY50 gyroscope(arduinoRuntime, 37);
SmartCar car(arduinoRuntime, control, gyroscope, leftOdometer, rightOdometer);
 
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

                


void setup() {
    Serial.begin(9600);
#ifdef __SMCE__    
    
    Camera.begin(QVGA, RGB888, 15);
    frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
    mqtt.begin(host, 1883, WiFi);
    mqtt.setOptions(10,true,1500);
    //car.enableCruiseControl();
    //this is connect to the broker.
    //Will connect to localhost port 1883 be default
#else
  mqtt.begin(net);
#endif    
    
    if (!mqtt.connected()) {
        mqtt.connect("arduino");  
        mqtt.subscribe(topic,0);
        mqtt.onMessage([](String topic, String message){
          
        if(topic =="/Group/13/Move" ){
          car.setSpeed(message.toInt());
          Serial.println("Speed: " + message);
          }else if (topic == "/Group/13/Turn"){
          Serial.println("Angle: " + message);
          car.setAngle(message.toInt());  
          }
         if(topic == "/Group/13/CruiseControl"){
          if(message == "EnableCruiseControl"){
            car.enableCruiseControl();
            Serial.println("EnableCruiseControl");
          }
         } 
      });
      
    }//this is a callback
     
}

void loop() {
     if (mqtt.connected()) {
    mqtt.loop();
    const auto currentTime = millis();
#ifdef __SMCE__    
    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 65) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("/Group/13/Camera", frameBuffer.data(), frameBuffer.size(),
                   false, 0);
    }
#endif    
    
    
    FrontDistance = front.getDistance();
    //Serial.print("Distance with front obstacle:");
    //Serial.println(FrontDistance);
    delay(200);// This delay for not print frequently
    car.update(); // Maintain the speed
//    if(FrontDistance <= 180&& movingSituation() && FrontDistance >0)
//    {
//      car.setSpeed(0);
//    }

    mqtt.loop();
    
}
#ifdef __SMCE__
  // Avoid over-using the CPU if we are running in the emulator
  delay(35);
#endif
}



boolean movingSituation(){
  initialSensorDistance= front.getDistance();
  delay(100);
  laterSensorDistance=front.getDistance();
  if (laterSensorDistance - initialSensorDistance <= 0){
    return true;
  }
  else{
    return false;
  }
   
}
