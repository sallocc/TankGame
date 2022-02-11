# TankGame
A level-based and achievement-based tank shooting game coded using swing and javafx.

# Setup
The only outside application needed is javafx. To install it, simply download the package
from [OpenJFX](http://openjfx.io). 

If using JGRASP:

Go to: Settings -> Compiler Settings -> Workspace -> JavaFX (In the second row of tabs) \n
Add: javafx.controls, javafx.media, javafx.swing. To the modules\n
Add: <path-to-javafx-folder>. To the JavaFX Home. (Careful to put the actual javafx folder and not the openjfx folder) \n
Press Ok and Run! \n

# Running
To run the game, run the command 
```
java --module-path {path-to-javafx} --add-modules=javafx.controls, javafx.swing, javafx.media TankGame
```
