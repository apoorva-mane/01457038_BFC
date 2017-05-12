package BFC;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.awt.FlowLayout;
import java.io.FileOutputStream;
import javax.swing.JTextArea;
import java.awt.Color;
import java.io.File;
public class DownloadFile extends JFrame
{
	JLabel l1;
	JButton b1;
	JComboBox c1;
	String user;
	JTextArea area;
	long start,end;
public void setUser(String user){
    this.user=user;
}
public DownloadFile(JTextArea ar){
	area = ar;
	setTitle("Download File");
	getContentPane().setBackground(Color.white);
	getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

	l1 = new JLabel("File Name");
	getContentPane().add(l1);

	c1 = new JComboBox();
	getContentPane().add(c1);

	b1 = new JButton("Download");
	getContentPane().add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			try{
				start = System.currentTimeMillis();
				String file = c1.getSelectedItem().toString();
				Socket socket=new Socket("localhost",1200);
				ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
				Object req[]={"download",file,user};
				out.writeObject(req);
				out.flush();
				Object res[]=(Object[])in.readObject();
				Object chunks[][] = (Object[][])res[0];
				FileOutputStream fout=new FileOutputStream("D:/"+file,true);
				for(int i=0;i<chunks.length;i++){
					byte b[] = (byte[])chunks[i][0];
					byte dec[] = AES.decrypt(b);
					fout.write(dec,0,dec.length);
				}
				fout.close();
				end = System.currentTimeMillis();
				area.append("Selected file downloaded in 'System D' directory\n");
				setVisible(false);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
}
public void getFileName(){
	try{
		c1.removeAllItems();
		Socket socket=new Socket("localhost",1200);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
		Object req[]={"getfilename",user};
		out.writeObject(req);
		out.flush();
		Object res[]=(Object[])in.readObject();
		String files[] = res[0].toString().split(",");
		for(int i=0;i<files.length;i++){
			c1.addItem(files[i]);
		}
		out.close();
		in.close();
		socket.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
}