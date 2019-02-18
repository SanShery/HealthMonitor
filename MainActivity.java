
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    static  String BROKER_URL           = "tcp://io.adafruit.com:1883";
    static  String M2MIO_USERNAME       = "akankshagupta";
    static  String M2MIO_PASSWORD_MD5   = "5e1604ab1c6b4f48824b46155654bfb1";
    static  String M2MIO_PUB_PATH       = "akankshagupta/feeds/health-monitor";
    static  String M2MIO_SUB_PATH       = "akankshagupta/feeds/health-monitor";
    static  String M2MIO_CLIENT_ID      = "IOT_Health_APp";

    //public final static String EXTRA_MESSAGE = "com.mycompany.IOT.MESSAGE";

    MQTTClient myClient;
    TextView sensor1,sensor2,sensor3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myClient = new MQTTClient();
        new Thread(myClient).start();

        sensor1 = (TextView) findViewById(R.id.textView3);
        sensor2 = (TextView)findViewById(R.id.textView5);
        sensor3 = (TextView)findViewById(R.id.textView7);
        sensor1.setEnabled(false);
        sensor2.setEnabled(false);
        sensor3.setEnabled(false);
    }

    public class MQTTClient implements MqttCallback,Runnable{
        MqttAndroidClient client;
        MqttConnectOptions connOpt;

        @Override
        public void run() {

            String clientId = M2MIO_CLIENT_ID;
            connOpt = new MqttConnectOptions();

            connOpt.setCleanSession(true);
            connOpt.setKeepAliveInterval(30);
            connOpt.setUserName(M2MIO_USERNAME);
            connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

            client = new MqttAndroidClient(getApplicationContext(), BROKER_URL, clientId);
            try {
                client.setCallback(this);

                IMqttToken token = client.connect(connOpt);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Toast.makeText(getApplicationContext(), "Connected to IOT Server", Toast.LENGTH_LONG).show();
                        //update.setEnabled(true);
                        //msg.setEnabled(true);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(getApplicationContext(), "Fail Connect to IOT Server", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }

            try {
                // wait to ensure subscribed messages are delivered
                Thread.sleep(5000);
                //myClient.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String sub_topic = M2MIO_SUB_PATH;
            int qos = 0;
            try {
                IMqttToken subToken = client.subscribe(sub_topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (Exception e1) {

            }

        }
        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String mqttMsg = new String(message.getPayload());
            String para[] = mqttMsg.split(",");
            String temp_str=para[0]+" °C";
            String humi_str=para[1]+" BPM";
            String co2_str=para[2]+" %";
            sensor1.setText(temp_str);
            sensor2.setText(humi_str);
            sensor3.setText(co2_str);

            //Toast.makeText(getApplicationContext(), "Data :" + mqttMsg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }

        void publish_msg(String payload)
        {
            String topic = M2MIO_PUB_PATH;
            byte[] encodedPayload = new byte[0];

            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                message.setRetained(true);
                IMqttToken token1 = client.publish(topic, message);
                token1.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //Toast.makeText(getApplicationContext(), "Payload Published", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Toast.makeText(getApplicationContext(), "Failed Published", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            } catch (Exception e1) {

            }
        }
    }
}
