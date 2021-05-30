# group-13

# What ?
A software that allows an end user to control a smart car remotely in a less traditional way by using the GUI we created mobile app based controller. Besides, the smart car can detect obstacles around it and avoid it using embedded sensors for it to travel freely.

# Why ?
The main objective of this software is to utilize and demonstrate the use of a real-time based embedded system in this case a functional user interface with the smart car emulator. One of our main goals is to implement a responsible GUI which can easily control all the functionality in the smart car emulator.  

# What does it solve ?
Aid the driver with more information about surroundings located around the car. It also reduces human error and time during the  operation.For example, It helps people park the car and give suggestions when people are controlling the car. It contains autonomous driving which allows the car to park by itself.

# How ? 
We are going to solve the above mentioned problems through an agile method of working. To do so we are going to have regular group meetings in which we discuss and decide our tasks that we are going to complete till the next meeting. In addition we will also have Q&A sessions with the TA’s or teachers throughout the time we are working on the project to have regular feedback or suggestions on the product we are creating.

## Software architecture
Sequence Diagram
![Sequence diagram](https://github.com/DIT112-V21/group-13/blob/softwareArchitecture/images/Sequence%20Diagram.png)

Activity Diagram
![Activity diagram](https://github.com/DIT112-V21/group-13/blob/softwareArchitecture/images/Activity%20diagram.png)

   JoystickTest is an application where a user can login and control their car remotely. It allows the user to control the car by using MQTT as a broker. If the user is a registered user, he/she will be able to login to the program by providing their username and password, and if they are not, they will be able to create a new account. The user's information will be saved in a database once they have registered. A successful message is displayed to the user once they have registered or logged in, while an unsuccessful message is presented if the procedure fails.

  The user will be able to control the car once they have logged in. They can either move/turn the car, use cruise control, camera or stop the car. The user can move the car using the joystick as they wish. The car will be driven, and if an obstacle is encountered along the path, it will be automatically detected and avoided. 

  The camera function implemented will allow the user to see the road on which the car is traveling. If the cruise control function is enabled, the car will roam around until the function is disabled. Users can also halt the car with the stop button.

# Technology 
- C++
- Arduino IDE & VS Code & IntelliJ
- Java FX  &  Swing
- Java
