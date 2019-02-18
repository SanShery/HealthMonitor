#include <ESP8266WiFi.h>
#include <PubSubClient.h>

#define AIO_USER_ID   "EM_Home_Wifi"
#define AIO_SERVER    "io.adafruit.com"
#define AIO_USERNAME  "akankshagupta"
#define AIO_KEY       "5e1604ab1c6b4f48824b46155654bfb1"
#define AIO_PUB_PATH  "akankshagupta/feeds/health-monitor"
#define AIO_SUB_PATH  "akankshagupta/feeds/health-monitor"

const char* ssid = "IOT ACCESS";
const char* password = "1234567890";

WiFiClient espClient;
PubSubClient client(espClient);

String DisplayAddress(IPAddress address)
{
    return String(address[0]) + "." + String(address[1]) + "." + 
           String(address[2]) + "." + String(address[3]);
}

void setup_wifi() {
    delay(10);
    //Serial.println("Connecting ...");
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        //Serial.print(".");
    }
    
    //Serial.println("WiFi connected");
    delay(2000);
    String msg = "IP address: " + DisplayAddress(WiFi.localIP()) ;
    //Serial.println(msg);
    delay(2000);
}

void callback(char* topic, byte* payload, unsigned int length) {
    char res[64];
    int i=0;
    //print("[");
    //print(topic);
    //print("] ");
    for (i = 0; i < length; i++) {
        char c=(char)payload[i];
        res[i]=c;
    }
    i++;
    res[i]=0;
    Serial.println(res);
}

void upload_details(void)
{
    if (Serial.available()) {
      char response[128]={0};
      int i=0;
      delay(200);
      while (Serial.available()) 
      {
          response[i++] = Serial.read(); // read the next character.
      }
      response[i]='\0';

      if(strstr(response,"WIFI,"))
      {
          if(client.publish(AIO_PUB_PATH,response+5))
          {
            //Serial.println("Updated Successfully");
          }
          else{
            //Serial.println("Failed to Updated");
          }
      }
      else if(strstr(response,"CONF:"))
      {
          //Serial.println("Started is Config");  
      }
    }
    else{
      return;
    }
    Serial.flush();  
}

void reconnect() {
    while (!client.connected()) {
        //Serial.println("Connecting To MQTT Server");
        if (client.connect(AIO_USER_ID,AIO_USERNAME,AIO_KEY)) {
            //Serial.println("Connected To MQTT Server ");
            client.subscribe(AIO_SUB_PATH);
        }else {
            //Serial.println("Fail to Connect MQTT Server");
            delay(3000);
        }
    }
}

void setup() {
    Serial.begin(9600);
    //Serial.println();
    //Serial.println("Starting ESP8266");
    setup_wifi();
    delay(3000);
    client.setServer(AIO_SERVER, 1883);
    client.setCallback(callback);
}

void loop() {
    if (!client.connected()) {
      reconnect();
    }
    upload_details();
    client.loop(); 
    delay(50);}
