package com.example.mqttconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    private Button btn1;

    static String MQTTHOST = "tcp://broker.hivemq.com:1883";
    static String USERNAME = "";
    static String PASSWORD = "";
    String topicStr = "smart/car";

    MqttAndroidClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientId = MqttClient.generateClientId();


                MqttConnectOptions options = new MqttConnectOptions();
                //options.setUserName(USERNAME);//set the username
                //options.setPassword(PASSWORD.toCharArray());//set the username



                client = new MqttAndroidClient(MainActivity.this, MQTTHOST,
                        clientId);

                try {
                    //IMqttToken token = client.connect(options);
                    IMqttToken token = client.connect();
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            Toast.makeText(MainActivity.this,"Not connected",Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }


            }
        });



    }

    public void forward (View v){
        String topic = topicStr;
        String message = "70";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }


    public void r_eturn (View v){
        String topic = topicStr;
        String message = "-70";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void turnLeft (View v){
        String topic = topicStr;
        String message = "-50";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
    public void turnRight (View v){
        String topic = topicStr;
        String message = "50";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }






}