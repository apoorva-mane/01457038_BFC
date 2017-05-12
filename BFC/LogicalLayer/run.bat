set classpath=lib/mysql-connector-java-5.1.6-bin.jar;lib/jfreechart-1.0.13-swt.jar;lib/jfreechart-1.0.13-experimental.jar;lib/jfreechart-1.0.13.jar;lib/jcommon-1.0.16.jar;.;
javac -d . *.java
java -Xmx1000M BFC.LogicalLayer
pause