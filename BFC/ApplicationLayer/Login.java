package BFC;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
public class Login extends JFrame
{
	JLabel l1,l2,l3,l4;
	JTextField tf1,tf2;
	JButton b1,b2,b3;
	Font f1,f2;
	JPanel p1,p2,p3,p4,p5,p6;
	ImageIcon icon;
public Login(){
	super("Login Screen");
	p1 = new JPanel();
	p1.setBackground(Color.black);
	f1 = new Font("Monospaced",Font.BOLD,22);
	l1 = new JLabel("<html><body><center>User Login Screen</center></body></html>");
	l1.setForeground(new Color(125,54,2));
	l1.setFont(f1);
	p1.add(l1);
	p1.setBackground(new Color(140,150,180));

	p2 = new JPanel();
	p2.setBackground(Color.black);
	icon = new ImageIcon("img/vista.jpg");
	JLabel label = new JLabel(icon);
	p2.add(label);
	
	p3 = new JPanel();
	p3.setLayout(new BorderLayout());

	p4 = new JPanel();
	f2 = new Font("Verdana",Font.PLAIN,14);
	l3 = new JLabel("Username");
	l3.setFont(f2);
	p4.add(l3);

	tf1 = new JTextField(15);
	tf1.setFont(f2);
	p4.add(tf1);

	p5 = new JPanel();
	l4 = new JLabel("Password");
	l4.setFont(f2);
	p5.add(l4);

	tf2 = new JPasswordField(15);
	tf2.setFont(f2);
	p5.add(tf2);

	p6 = new JPanel();
	b1 = new JButton("Login");
	b1.setFont(f2);
	b1.setBackground(new Color(51, 51, 51));
	p6.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			login();
        }
	});

	b2 = new JButton("Reset");
	b2.setFont(f2);
	b2.setBackground(new java.awt.Color(51, 51, 51));
	p6.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			reset();
        }
	});
	b3 = new JButton("New User");
	b3.setFont(f2);
	b3.setBackground(new java.awt.Color(51, 51, 51));
	p6.add(b3);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			Register register = new Register();
			register.pack();
			register.setVisible(true);
			register.setLocationRelativeTo(null);
        }
	});
	p3.add(p4,BorderLayout.NORTH);
	p3.add(p5,BorderLayout.CENTER);
	p3.add(p6,BorderLayout.SOUTH);
	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);
	getContentPane().add(p3,BorderLayout.SOUTH);
}
public static void main(String a[])throws Exception{
	 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	 Login screen = new Login();
	 screen.setVisible(true);
	 screen.pack();
	 screen.setLocationRelativeTo(null);
}
public void reset(){
	tf1.setText("");
	tf2.setText("");
}
public void login(){
	try{
		String user = tf1.getText();
		String pass = tf2.getText();
		if(user == null || user.trim().length() <= 0){
			JOptionPane.showMessageDialog(this,"Username must be enter");
			tf1.requestFocus();
			return;
		}
		if(pass == null || pass.trim().length() <= 0){
			JOptionPane.showMessageDialog(this,"Password must be enter");
			tf2.requestFocus();
			return;
		}
		Socket socket=new Socket("localhost",1200);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
        Object req[]={"login",user,pass};
        out.writeObject(req);
        out.flush();
        Object res[]=(Object[])in.readObject();
		String msg = res[0].toString();
		if(msg.equals("success")){
			setVisible(false);
			UserScreen us = new UserScreen(this,user);
			us.setVisible(true);
			us.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}else{
			JOptionPane.showMessageDialog(this,"invalid login");
		}
	}catch(Exception e){
        e.printStackTrace();
    }
		
}
}