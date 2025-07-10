@echo off
cd /d "%~dp0"
javac -d build/classes -cp "lib/*" src/oop_motorph/*.java
java -cp "build/classes;lib/jcalendar-1.4.jar;lib/mysql-connector-j-8.0.33.jar" oop_motorph.OOP_MotorPH
pause
 