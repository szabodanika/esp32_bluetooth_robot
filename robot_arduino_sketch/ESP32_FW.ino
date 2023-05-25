// Import for bluetooth serial communication
// #include "BluetoothSerial.h"

// Import for JSON support (for serial comm. protocol)
#include <Arduino_JSON.h>

// Import for Accelerometer
// #include <Adafruit_ISM330DHCX.h>

#define REPORT_DELTA_MS 1000

// report object that is sent to client every N milliseconds
struct RobotReportData {
  long timestamp;
  float x, y, z;
  float temperature;
};

// object that is received client triggered by user input
struct RobotControlData {
  long timestamp;
  boolean led;
  // -1 -> turn left
  // 0 -> straight
  // 1 -> turn right
  int direction;
  // -1 -> reverse
  // 0 -> stationary
  // 1 -> forward/drive
  int velocity; 
};

float* getAcceleration() {

  static float values[3];

  values[0] = 0.0f;
  values[1] = 0.0f;
  values[2] = 9.7f;

  return values;
}

float getTemperature() {

  return 21.5f;
}

// Function to create a RobotReportData object from a JSON string
RobotReportData createRobotReportData() {
  RobotReportData data = {
    0,
    getAcceleration()[0],
    getAcceleration()[1],
    getAcceleration()[2],
    getTemperature(),
  };
  return data;
}

// Function to write a RobotReportData object as a JSON string to the serial monitor
String writeRobotReportDataToJson() {

  RobotReportData data = createRobotReportData();
  
  // Create a JSONVar object and set its properties using the values from the input struct
  JSONVar jsonObj;
  jsonObj["timestamp"] = data.timestamp;
  jsonObj["x"] = data.x;
  jsonObj["y"] = data.y;
  jsonObj["z"] = data.z;
  jsonObj["temperature"] = data.temperature;

  // Convert the JSONVar object to a string using JSON.stringify()
  String jsonString = JSON.stringify(jsonObj);

  return jsonString;
}

// Function to create a RobotControlData object from a JSON string
RobotControlData parseRobotControlData(String jsonString) {
  // Parse the JSON string into a JSONVar object
  JSONVar jsonObj = JSON.parse(jsonString);

  // Extract the values of the properties from the JSONVar object
  RobotControlData data = {
    jsonObj["timestamp"],
    jsonObj["led"],
    jsonObj["direction"],
    jsonObj["velocity"]
  };

  return data;
}

void handleRobotControlData(String jsonString) {
  // Parse the JSON string into a RobotReportData object
  RobotControlData data = parseRobotControlData(jsonString);

  Serial.println(jsonString);
  Serial.println(data.led);

  // pinmode only for arduino
  pinMode(LED_BUILTIN, OUTPUT);

  if (data.led) {
    digitalWrite(LED_BUILTIN, HIGH);
  } else {
    digitalWrite(LED_BUILTIN, LOW);
  }
}

void setup() {
  // initialize serial communication at 9600 bits per second
  Serial.begin(9600); 
  // while until Serial is available
  while (!Serial);
}

void loop() {
  // initialize lastTime variable to 0
  static unsigned long lastTime = 0; 
  // get current time in milliseconds
  unsigned long currentTime = millis();
  
  // check if N milliseconds have passed
  if (currentTime - lastTime >= REPORT_DELTA_MS) { 
    // update lastTime variable
    lastTime = currentTime;
    // write new robot report data on serial
    Serial.println(writeRobotReportDataToJson()); 
  }
  
  // check if there is serial input
  
  if (Serial.available() > 0) { 
    // read the input until a newline character is received
    String input = Serial.readStringUntil('\n'); 
    // handle incoming control data
    handleRobotControlData(input);
  } else {
    Serial.println("waiting for control data");
  }

  delay(250);
}

