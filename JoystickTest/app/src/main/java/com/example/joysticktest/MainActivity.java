package com.example.joysticktest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static com.example.joysticktest.JoystickView.*;


public class MainActivity extends AppCompatActivity implements JoystickListener {


    static String MQTTHOST = "tcp://broker.emqx.io:1883";
    static String USERNAME = "";
    static String PASSWORD = "";
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;
    private String topic = "/Group/13";
    private String moveTopic = "/Group/13/Move";
    private String turnTopic = "/Group/13/Turn";
    private String cruiseTopic = "/Group/13/CruiseControl";
    private String cameraTopic = "/Group/13/Camera";
    private String moveMessage;
    private String turnMessage;
    public Queue<Float> yDataPackage = new LinkedList<>();
    public Queue<Float> xDataPackage = new LinkedList<>();






    MqttAndroidClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView camera = (ImageView)findViewById(R.id.imageView4);

        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cameraView(cameraTopic, camera);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });




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

    public void cameraView(String topic,ImageView camera) throws MqttException {


        IMqttToken subToken = client.subscribe(topic, 0);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Toast.makeText(MainActivity.this, "Camera Connected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Toast.makeText(MainActivity.this, "Camera Not connected", Toast.LENGTH_SHORT).show();

            }
        });
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                final byte[] payload = message.getPayload();
                final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                for (int ci = 0; ci < colors.length; ++ci) {
                    final byte r = payload[3 * ci];
                    final byte g = payload[3 * ci + 1];
                    final byte b = payload[3 * ci + 2];
                    colors[ci] = Color.rgb(r, g, b);
                }
                bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                camera.setImageBitmap(bm);

                Log.e(topic, String.valueOf(colors.length));

                Log.e(topic, "[MQTT] Topic: " + topic + " | Message: " + message.toString());

            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

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

