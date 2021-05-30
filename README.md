# Mini Project: System Development

## Group 13
## Members:
1. Aditya Nair
2. Bora Kocak
3. Jiacheng Li
4. Maheli Silva
5. Najeb Albakar
6. Peiran Wei
7. Zubeen S Maruf

## [Recording of the Demo](https://drive.google.com/file/d/1DHJ9WdLHI-47YSUPtDFS1ZAgrk4xe-8m/view?usp=sharing)

In order to showcase of our final product, we have recoded a video demonstrating the full functionality of our system. We hope the video gives you a good idea of what we've been trying to achieve. Thanks for watching!

## Table of Contents
1. [What does the product contain?](#what-)
2. [Why did we need it?](#why-)
3. [What does it solve?](#what-does-it-solve-)
4. [How did we develop it?](#how-)
5. [Application User Manual](#application-user-manual)
6. [Software Architecture](#software-architecture)
7. [Technology](#technology)

## What ?
A software that allows an end user to control a smart car remotely in a less traditional way by using the GUI we created (either a mobile app or a web based controller). Besides, the smart car can detect obstacles around it and avoid it using embedded sensors for it to travel freely.

## Why ?
User story: As a car i should be able to look at the speed of my moving vehicle to keep it under speed limits.
Acceptance criteria: Live speedometer display projected directly from the car.

Scenario 1: The car owner is at a shopping mall or supermarket and has a bunch of groceries and bags in hand, the car is parked far away from the entrance and it is quite difficult to get to the car.

Scenario 2: It is pouring down with rain and the family is stranded at the entrance of their establishment they were not prepared for the sudden rain, the car is parked outside at a carpark  and they family cannot reach to the car due to the heavy rainfall.

Scenario 3: The car owner wants to check up on the car to check if everything is ok and is out of danger, a check around the surroundings would give the owner assurance their car is fine.


The main objective of this software is to utilize and demonstrate the use of a real-time based embedded system in this case a functional user interface with the smart car emulator. One of our main goals is to implement a responsible GUI which can easily control all the functionality in the smart car emulator.  

## What does it solve ?
Aid the driver with more information about surroundings located around the car. It also reduces human error and time during the  operation.For example, It helps people park the car and give suggestions when people are controlling the car. It contains autonomous driving which allows the car to park by itself.

## How ? 
We are going to solve the above mentioned problems through an agile method of working. To do so we are going to have regular group meetings in which we discuss and decide our tasks that we are going to complete till the next meeting. In addition we will also have Q&A sessions with the TA’s or teachers throughout the time we are working on the project to have regular feedback or suggestions on the product we are creating.

## Application User Manual
<p float="left">
<img src="https://github.com/DIT112-V21/group-13/blob/Peiran-Wei-README-patch/images/app%20user%20manual01.png" width="225" height="425" />
<img src="https://github.com/DIT112-V21/group-13/blob/Peiran-Wei-README-patch/images/app%20user%20manual02.png" width="225" height="425" />
</p>

1. The login page of the UI indicates to the user that an account can be registered or logined by pressing the specific button.
2. The car-view application UI indicates to the user that SMCE can be connected to via the MQTT broker.
3. Once SMCE has been connected, a user can control the car manually by using the joystick button.
4. By pressing the button 'Curise Control' and 'Stop', a user can manually require the car into curising mode or stop the car immediately.

## Software Architecture
Sequence Diagram

![Sequence diagram](https://github.com/DIT112-V21/group-13/blob/softwareArchitecture/images/Sequence%20Diagram.png)

Activity Diagram

![Activity diagram](https://github.com/DIT112-V21/group-13/blob/softwareArchitecture/images/Activity%20diagram.png)

   JoystickTest is an application where a user can login and control their car remotely. It allows the user to control the car by using MQTT as a broker. If the user is a registered user, he/she will be able to login to the program by providing their username and password, and if they are not, they will be able to create a new account. The user's information will be saved in a database once they have registered. A successful message is displayed to the user once they have registered or logged in, while an unsuccessful message is presented if the procedure fails.

  The user will be able to control the car once they have logged in. They can either move/turn the car, use cruise control, camera or stop the car. The user can move the car using the joystick as they wish. The car will be driven, and if an obstacle is encountered along the path, it will be automatically detected and avoided. 

  The camera function implemented will allow the user to see the road on which the car is traveling. If the cruise control function is enabled, the car will roam around until the function is disabled. Users can also halt the car with the stop button.

## Technology 
- C++
- Java
- SMCE
- Arduino IDE & VS Code & Android Studio
- Java FX  &  Swing
