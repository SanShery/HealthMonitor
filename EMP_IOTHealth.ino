#include <EMLcd.h>
EMLcd lcd(7,6,5,3,9,8);//D4,D5,D6,D7,RS,EN

volatile int BPM;                   // int that holds raw Analog in 0. updated every 2mS
volatile boolean QS = false;        // becomes true when Arduoino finds a beat.
char line1[16],line2[16];

#define TEMP_SENSOR A1

volatile int temp=0,heartbeat=0,pulse=0;

void setup() {
  
    Serial.begin(9600);
    lcd.init();
    lcd.clear();
    lcd.print_xy(0,0,"IOT Base");
    lcd.print_xy(0,1,"Health Monitor");
    Serial.println("Sensor System Started");
    delay(1000);
    setup_heart_beat();
}

void loop() {
    if (QS == true){                        // A Heartbeat Was Found,BPM and IBI have been Determined,Quantified Self "QS" true when arduino finds a heartbeat
      //send_data();
      QS = false;                       // reset the Quantified Self flag for next time    
    }

    temp=0;
    for(int i=0;i<100;i++)
    {
      temp+=(((analogRead(TEMP_SENSOR))/1023.00)*500.00);
      delayMicroseconds(50);
    }
    
    temp=temp/100;
    
    sprintf(line1,"Temp:%d BPM:%d",temp,BPM);
    sprintf(line2,"SPO2:%d ",pulse);
    lcd.clear();
    lcd.print_xy(0,0,line1);
    lcd.print_xy(0,1,line2);
    char s_str[32];
    sprintf(s_str,"WIFI,%d,%d,%d,",temp,BPM,pulse);
    Serial.println(s_str);
    delay(1000);   
}
