# HealthMonitor
#The project focuses on the usage of sensor technology to record the health conditions of people.
IOT Based Patient Health Tracking System
usage of sensor technology to record the health conditions of people. The best way to understand this is with an example. 
A general doctor with no such technology can check the patients’ health only when the patient visits him in the clinic. 
Now, with this technology the doctor has a complete record of the patient whether at home, office etc. 
and hence he has prescribe any medication much better.

1. The Arduino board has the LED, LCD, Wi-Fi, Heartbeat sensor, temperature sensor, capacitors, switch attached to it. 
On connecting it to a power the board starts to work. 
2. The LED light displays color which means that the system is working. 
Then the LCD starts with values of room temperature and a random value for BPM. 
3. On holding the heartbeat sensor and the temperature sensor in between our hands we get the value of the heartbeat and temperature of our own body. These values are then displayed on the LCD screen.
I have configured the LCD to display the heartbeat, temperature and oxygen level values. 
4. The PCP is prepared using the Eagle Software. The system also has a Wi-Fi module connected to it. 
5. This Wi-Fi module is used to send the details of the Temperature, Heartbeat and Oxygen level to an android app where the users friends, 
family or doctor are able to monitor it or detect any case of discrepancies.  
6. The code is written in Arduino and uploaded to all the sensors and to the Wi-Fi module. 
The system I have currently built is used for getting the sensor values for the temperature and heartbeat for any person.
The system has a Wi-Fi module connected to it. This module sends the data from LCD screen to the android app.
7. The Hardware has sensors for heartbeat detection and temperature detection. 
The hardware uses Wi-Fi ESP8266 for internet connection and transferring the sensor data to the server adafruit. 
The MQTT protocol is a light weight protocol that the project uses for transferring the sensor data. 
MQTT has subscribe and publish methodologies. MQTT here has two clients: hardware and android app.
The android app code is modified for working of MQTT protocol and attaching to the server. 
For connection we need adafruit login id, key, topic, server name, port.
8. The publish methodology is used for transferring the data from hardware sensor to server adafruit where the values are stored. 
Android App uses the subscribe method. It subscribes for feeds of the topic Health Monitor and it gets the real time values for the person
using the hardware sensor from the app. The app does not store values.
9. The code for the Wi-Fi module is configured so as to send the data to the Adafruit server where it is stored and displayed in both
tabular and graphical form.
