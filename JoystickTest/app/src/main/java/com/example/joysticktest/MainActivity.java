package com.example.joysticktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;

import static com.example.joysticktest.JoystickView.*;


public class MainActivity extends AppCompatActivity implements JoystickListener {


    static String MQTTHOST = "tcp://broker.emqx.io:1883";
    static String USERNAME = "";
    static String PASSWORD = "";
    private String topic = "/Group/13";
    private String moveTopic = "/Group/13/Move";
    private String turnTopic = "/Group/13/Turn";
    private String cruiseTopic = "/Group/13/CruiseControl";
    private String moveMessage;
    private String turnMessage;
    private ArrayList MoveX;
    private float MoveY;



    MqttAndroidClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button1);
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
                            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });






        

    }


    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        yPercent = 0 - yPercent;
        /*JoystickView joystickView = new JoystickView(Context context);
        float x1, x2, y1, y2;
        double dis;
        x1 = joystickView.getX();
        x2 = joystickView.centerX();
        y1 = joystickView.getY();
        y2 = joystickView.centerY();
        dis = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));*/

        moveMessage = String.valueOf(yPercent * 80);
        turnMessage = String.valueOf(xPercent * 90);
        Log.d("move", moveMessage + " " + turnMessage);
        try {
            client.publish(moveTopic, moveMessage.getBytes(), 0, false);
            client.publish(turnTopic, turnMessage.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void enableCruiseControl(View v){
        String message = "EnableCruiseControl";

        try{
            client.publish(cruiseTopic,message.getBytes(),0,false);
        }catch (MqttException e){
            e.printStackTrace();
        }




    }



}

