#include <MQTT.h>
#include <WiFi.h>
#include <vector>

MQTTClient mqtt;
#ifndef __SMCE__
WiFiClient net;
#endif
std::vector<char> frameBuffer;

#ifdef __SMCE__
#include <OV767X.h>
#endif

const char host[] = "broker.emqx.io";
const char topic[] = "/smart/car";
const char cameraTopic[]="/smart/car/camera";



void setup() {
    Serial.begin(9600);

    #ifdef __SMCE__
      Camera.begin(QVGA, RGB888, 15);
      frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
      mqtt.begin(host, 1883, WiFi);
    #else
      mqtt.begin(net);
    #endif
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
    long currentTime = millis();

    static auto previousFrame = 0UL;
    if (currentTime - previousFrame >= 50) {
      previousFrame = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqtt.publish("marsOrbiter/camera", frameBuffer.data(), bufferSize, false, 0);
    }
    
    //Serial.println(mqtt.publish(topic,"fight"));
    delay(35); 
}
