package com.example.joysticktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import java.util.LinkedList;
import java.util.Queue;

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
    public Queue<Float> yDataPackage = new LinkedList<>();
    public Queue<Float> xDataPackage = new LinkedList<>();




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
    public float rectifier(float Percent){
        ArrayList<Float> Pivot = new ArrayList<>();

        for(float i = 0; i<= 100; i = i + 10 ){
            Pivot.add(i);
            for (float y:Pivot) {
                if(y >= Percent ){
                    Percent = y;
                    break;
                }
            }
        }
        return Percent;
    }

    public float pack(Queue<Float> dataPackage){
        float x1 = 0;
        float x2 = 0;
        float x3 = 0;
        if (!(dataPackage.isEmpty())){
            x1 = dataPackage.poll();
            x2 = dataPackage.poll();
            x3 = dataPackage.poll();
        }

        if(x1 == x2 && x2 == x3){
            return x1;
        }

        return x3;
    }


    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        yPercent = 0 - (yPercent*100);
        xPercent= xPercent*90;

        if(yPercent < 0){
            yPercent = 0 - rectifier(Math.abs(yPercent));
        }else{
            yPercent = rectifier(yPercent);
        }
        if(xPercent < 0){
            xPercent = 0 - rectifier(Math.abs(xPercent));
        }else{
            xPercent = rectifier(xPercent);
        }
        xDataPackage.offer(0.0f);
        yDataPackage.offer(0.0f);
        xDataPackage.offer(0.0f);
        yDataPackage.offer(0.0f);
        xDataPackage.offer(xPercent);
        yDataPackage.offer(yPercent);
        xPercent = pack(xDataPackage);
        yPercent = pack(yDataPackage);

        /*JoystickView joystickView = new JoystickView(Context context);
        float x1, x2, y1, y2;
        double dis;
        x1 = joystickView.getX();
        x2 = joystickView.centerX();
        y1 = joystickView.getY();
        y2 = joystickView.centerY();
        dis = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));*/


        moveMessage = String.valueOf(yPercent);
        turnMessage = String.valueOf(xPercent);
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

