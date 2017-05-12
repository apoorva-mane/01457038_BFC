package BFC;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
public class UserScreen extends JFrame{
	JButton b1,b2,b3,b4,b5;
	JPanel p1,p2;
	Font f1;
	JTextArea area;
	JScrollPane jsp;
	Login login;
	String user;
	JFileChooser chooser;
	RandomAccessFile random;
	int tot_blocks;
	DownloadFile df;
	long start,end;
public UserScreen(Login log,String usr){
	super("User Screen");
	login = log;
	user = usr;
	p1 = new JPanel();
	f1 = new Font("Monospaced",Font.BOLD,16);
	chooser = new JFileChooser();
	b1 = new JButton("Upload File");
	b1.setFont(f1);
	p1.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			int option = chooser.showOpenDialog(UserScreen.this);
			if(option == JFileChooser.APPROVE_OPTION){
				File file = chooser.getSelectedFile();
				start = System.currentTimeMillis();
				upload(file);
				end = System.currentTimeMillis();
			}
		}
	});
	
	b2 = new JButton("Download File");
	b2.setFont(f1);
	p1.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			df = new DownloadFile(area);
			df.setUser(user);
			df.setSize(300,100);
			df.setVisible(true);
			df.setLocationRelativeTo(null);
			df.getFileName();
		}
	});

	b4 = new JButton("Upload/Download Time");
	b4.setFont(f1);
	p1.add(b4);
	b4.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			UploadChart chart1 = new UploadChart("Upload/Download Time Chart",start,end,df.start,df.end);
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	b5 = new JButton("Space Complexity Chart");
	b5.setFont(f1);
	p1.add(b5);
	b5.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			ArrayList<String> list = new ArrayList<String>();
			try{
				BufferedReader br = new BufferedReader(new FileReader("space.txt"));
				String line = null;
				while((line = br.readLine())!=null){
					list.add(line.trim());
				}
				br.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			SpaceChart chart1 = new SpaceChart("Space Complexity Chart",list);
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	b3 = new JButton("Logout");
	b3.setFont(f1);
	p1.add(b3);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			setVisible(false);
			login.setVisible(true);
		}
	});

	p2 = new JPanel();
	p2.setLayout(new BorderLayout());
	area = new JTextArea();
	area.setFont(f1);
	area.setEditable(false);
	jsp = new JScrollPane(area);
	p2.add(jsp,BorderLayout.CENTER);

	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);

}
public long getChunkSize(File file){
	long length = file.length();
	tot_blocks=0;
	long size = 0;
	if(length >= 1000){
		size = length/10;
		tot_blocks = 10;
	}
	if(length < 1000 && length > 500){
		size = length/5;
		tot_blocks = 5;
	}
	if(length < 500 && length > 1){
		size = length/3;
		tot_blocks = 3;
	}
	return size;
}
public void upload(File file){
	try{
		FileWriter fw = new FileWriter("space.txt",true);
		fw.write(file.getName()+","+(file.length())+System.getProperty("line.separator"));
		fw.close();
		FileInputStream fin = new FileInputStream(file);
		byte file_data[] = new byte[fin.available()];
		fin.read(file_data,0,file_data.length);
		fin.close();
		byte encrypt[] = AES.encrypt(file_data);
		String sha = SHA.ShaSignature(encrypt);
		long chunk_size = getChunkSize(file);
		random = new RandomAccessFile(file,"r");
		Object row[][] = createChunks(chunk_size,file.getName());
		random.close();
		Socket socket=new Socket("localhost",1200);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
		Object req[]={"upload",row,user,file.getName(),sha,Long.toString(file.length())};
		out.writeObject(req);
		out.flush();
		Object res[]=(Object[])in.readObject();
		String server_res = res[0].toString();
		area.append(server_res+"\n");
		out.close();
		in.close();
		socket.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
public Object[][] createChunks(long chunks_size,String name){
	area.append("Total chunks "+tot_blocks+" With chunk size "+chunks_size+"\n");
	Object row[][]=new Object[tot_blocks][2];
	try{
		String ext = name.substring(name.lastIndexOf(".")+1,name.length());
		for(int i=0;i<tot_blocks;i++){
			byte b[]=new byte[(int)chunks_size];
			random.read(b);
			random.seek(random.getFilePointer());
			row[i][0]=AES.encrypt(b);
			row[i][1]=name+"_chunk"+i+"."+ext;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return row;
}
}