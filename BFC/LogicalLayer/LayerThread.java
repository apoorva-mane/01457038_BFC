package BFC;
public class LayerThread extends Thread
{
	LogicalLayer server;
public LayerThread(LogicalLayer server){
	this.server=server;
	start();
}
public void run(){
	server.start();
}
}