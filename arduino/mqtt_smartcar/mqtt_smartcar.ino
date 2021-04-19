#include<WiFi.h>
#include<MQTT.h>
#include<Smartcar.h>





char ssid[] = "GOD  BLESS YOU";
char pass[] = "55555555";
//char clientID[] = "";
//char username[] = "";
//char password[] = "";
char topic[] = "smart/car";
char host[] = "broker.hivemq.com";
int port = 1883;

WiFiClient net;
MQTTClient mqtt;


void connect() {
  Serial.print("checking wifi...");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(1000);
  }

  Serial.print("\nconnecting...");
  while (!mqtt.connect("arduino")) {
    Serial.print(".");
    delay(1000);
  }

  Serial.println("\nconnected!");

  mqtt.subscribe(topic);
  // client.unsubscribe("/hello");
}

void messageReceived(String &topic, String &payload) {
  Serial.println("incoming: " + topic + " - " + payload);

}



void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  WiFi.begin(ssid, pass);
  mqtt.begin(host,net);
  mqtt.onMessage(messageReceived);

  connect();
 
  
}

void loop() {
  // put your main code here, to run repeatedly:
  mqtt.loop();

  if (!mqtt.connected()) {
    connect();
  }
}
