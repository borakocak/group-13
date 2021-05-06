#include <Smartcar.h>
#include <MQTT.h>
#include <WiFi.h>


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
DistanceCar car(arduinoRuntime, control, leftOdometer, rightOdometer);
SR04 front(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);
                


void setup()
{
    Serial.begin(9600);
    mqtt.begin(host, 1883, WiFi);
    //this is connect to the broker.
    //Will connect to localhost port 1883 be default
    
    if (!mqtt.connected()) {
        mqtt.connect("arduino");  
        mqtt.subscribe(topic,1);
        mqtt.onMessage([](String topic, String message){
          Serial.print("topic: " + topic + "message: "+ message);
        if(topic =="/Group/13/Move" ){
          car.setSpeed(message.toInt());
          }else if (topic == "/Group/13/Turn"){
          car.setAngle(message.toInt());  
          }
          
      });
      
    }//this is a callback
     
}

void loop()
{   
    mqtt.loop();
    FrontDistance = front.getDistance();
    Serial.print("Distance with front obstacle:");
    Serial.println(FrontDistance);
    delay(200);// This delay for not print frequently
    car.update(); // Maintain the speed
    if(FrontDistance <= 180&& movingSituation() && FrontDistance >0)
    {
      car.setSpeed(0);
    }
    
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