#include <MQTT.h>
#include <WiFi.h>

MQTTClient mqtt;

const char host[] = "broker.emqx.io";
const char topic[] = "/smart/car";



void setup() {
    Serial.begin(9600);
    mqtt.begin(host, 1883, WiFi);
    //this is connect to the broker.
    //Will connect to localhost port 1883 be default
    
    if (!mqtt.connected()) {
        mqtt.connect("arduino");  
        mqtt.subscribe(topic,0);
        mqtt.onMessage([](String topic, String message){
        Serial.println("incoming: " + topic + "message: " + message);
      });
      
    }//this is a callback
       
}

void loop() {

    mqtt.loop();//this is keeping the mqtt is running
    delay(35); 
}
