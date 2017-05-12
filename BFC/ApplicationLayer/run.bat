set classpath=lib/miglayout-3.6-swing.jar;lib/jfreechart-1.0.13-swt.jar;lib/jfreechart-1.0.13-experimental.jar;lib/jfreechart-1.0.13.jar;lib/jcommon-1.0.16.jar;.;
javac -d . *.java
java -Xmx1000M BFC.Login
pause