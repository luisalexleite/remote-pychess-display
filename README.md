<p align="center">
  <img src="https://github.com/luisalexleite/remote-pychess-display/blob/main/img/logo.png" />
</p>

## Display System

Remote PyChess is an plataform to the users that want an private system to play chess game with their friends and/or coworkers.

The Display System is divided in two interfaces:



### Game Initializer

The Game Initializer is the environment that displays before a chess game has started on the platform.

Features:

* Diferent Openings Info

* Next Game Info



### Game Display

The Game Display is the environment that is displayed when a game is ocurring.

Features:

* Timer

* Live Opening Info and Move History

* Check and Last Movement Markdown

* Game Result Display

### System Requiments

#### Game Initializer

|  Requirement  |  Minimum  |  Recomended  |
|:-------------:|:---------:|:------------:|
|PHP|7.0|>= 8.0|
|Browser|N/A|Tested on Chrome and Firefox|

#### Game Display

|  Requirement  |  Minimum  |  Recomended  |
|:-------------:|:---------:|:------------:|
|Python|3.0|>= 3.7|
|RAM|2 GB|>= 4 GB|
|CPU|ARM Cortex-A53|>= Intel Atom Z2580|


### Instalation Instructions to Debian (>=10) Based Systems

##### Before you start:

* You must have an [Firebase](https://console.firebase.google.com/) project created to host the platform.

* Game Display has to be configured before Game Initializer

#### Game Initializer

* Change main.js file and input firebase configure settings on file (instrutions on file)

* Start an PHP dedicated server on the root folder of the platform - php -S localhost:port

#### Game Display

* Download all the Python Libraries needed (requirements.txt coming soon)

* Download JSON key (to Firebase access - instrutions on remotepychess.py file) and move it to an folder named "cred" (if doesn't exists, create one) on root folder

* Change remotepychess.py file and input firebase configure settings on file (instrutions on file)

### To be implemented ❌

* requirements.txt

* Instalation Client
