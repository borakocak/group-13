#include <Smartcar.h>


const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 1000;
const auto pulsesPerMeter = 600;
int initialSensorDistance = 0;
int laterSensorDistance =0;
const int GYROSCOPE_OFFSET = 37;
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
GY50 gyro(arduinoRuntime, GYROSCOPE_OFFSET);
                


void setup()
{
    Serial.begin(9600);
     
    
}
void handleInput()
{
    // handle serial input if there is any
    if (Serial.available())
    {
        String input = Serial.readStringUntil('\n');
        if (input.startsWith("m"))
        {
            int throttle = input.substring(1).toInt();
            car.setSpeed(throttle);
        }
        else if (input.startsWith("t"))
        {
            int deg = input.substring(1).toInt();
            car.setAngle(deg);
        }
        else if (input.startsWith("c"))
        {
          car.enableCruiseControl();
          int cruiseSpeed = input.substring(1).toInt();
          car.setSpeed(cruiseSpeed);
        }
         else if (input.startsWith("x"))
         {
          car.disableCruiseControl();
          }
        
         
    }
}
 
void loop()
{
    FrontDistance = front.getDistance();
    Serial.print("Distance with front obstacle:");
    Serial.println(FrontDistance);
    delay(200);// This delay for not print frequently
    car.update(); // Maintain the speed
    if(FrontDistance <= 180&& movingSituation() && FrontDistance >0)
    {
      car.setSpeed(0);
    }
    {
    gyro.update();
    Serial.println(gyro.getHeading());
}
    {
    handleInput();
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
