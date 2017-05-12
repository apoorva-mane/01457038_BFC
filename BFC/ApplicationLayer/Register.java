package BFC;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import javax.swing.JFrame;
public class Register extends JFrame{
    JPanel p1;
    Font f1;
    JLabel l1,l2,l3,l4,l5;
    JTextField tf1,tf2,tf3,tf4,tf5;
    JButton b1,b2;
    Dimension d;
    int w,h;
    int center;
public Register(){
	super("New User Registration Screen");
    d = Toolkit.getDefaultToolkit().getScreenSize();
    w = (int)d.getWidth();
    h = (int)d.getHeight();
    center = w/30;
	setBackground(Color.white);
    p1 = new JPanel();
	p1.setBackground(Color.white);
    p1.setLayout(new MigLayout("wrap 2","","[]15[]"));
    f1 = new Font("Verdana",Font.PLAIN,14);
    p1.add(new JLabel(),"cell 0 30");
    p1.add(new JLabel(),"cell 1 30");
    
    l1 = new JLabel("Username");
    l1.setFont(f1);
    p1.add(l1,"gap left "+center);
    tf1 = new JTextField(12);
    tf1.setFont(f1);
    p1.add(tf1,"gap left 30");
    
    l2 = new JLabel("Password");
    l2.setFont(f1);
    p1.add(l2,"gap left "+center);
    tf2 = new JPasswordField(12);
    tf2.setFont(f1);
    p1.add(tf2,"gap left 30");
    
    
    l3 = new JLabel("Contact No");
    l3.setFont(f1);
    p1.add(l3,"gap left "+center);
    tf3 = new JTextField(12);
    tf3.setFont(f1);
    p1.add(tf3,"gap left 30");
    
    l4 = new JLabel("Email ID");
    l4.setFont(f1);
    p1.add(l4,"gap left "+center);
    tf4 = new JTextField(12);
    tf4.setFont(f1);
    p1.add(tf4,"gap left 30");
    
    l5 = new JLabel("Address");
    l5.setFont(f1);
    p1.add(l5,"gap left "+center);
    tf5 = new JTextField(30);
    tf5.setFont(f1);
    p1.add(tf5,"gap left 30");
    
    
    p1.add(new JLabel(""));
    b1 = new JButton("Register");
    b1.setFont(f1);
    p1.add(b1,"split 2");
    b1.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ae){
            process();
        }
    });
    
    
    b2 = new JButton("Clear");
    b2.setFont(f1);
    p1.add(b2);
    b2.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ae){
            clearFields();
        }
    });
    
    add(p1);
    
}
public void process(){
    try{
        String uname=tf1.getText();
        String pass=tf2.getText();
        String contact=tf3.getText();
        String email=tf4.getText();
        String address=tf5.getText();
        if(uname.length() <=0 || uname == null){
            JOptionPane.showMessageDialog(this,"Username must be enter");
            tf1.requestFocus();
            return;
        }
        if(pass.length() <=0 || pass == null){
            JOptionPane.showMessageDialog(this,"Password must be enter");
            tf2.requestFocus();
            return;
        }
        if(contact.length() <=0 || contact == null){
            JOptionPane.showMessageDialog(this,"Contact No must be enter");
            tf3.requestFocus();
            return;
        }
        if(contact.length() <=0 || contact == null){
            JOptionPane.showMessageDialog(this,"Contact no must be enter");
            tf3.requestFocus();
            return;
        }
		if(!validatePhoneNumber(contact.trim())){
			JOptionPane.showMessageDialog(this,"Enter valid contact no");
            tf3.requestFocus();
            return;
		}
        if(email.length() <=0 || email == null){
            JOptionPane.showMessageDialog(this,"Email id must be enter");
            tf4.requestFocus();
            return;
        }
		if(!CheckMail.checkMail(email)){
			JOptionPane.showMessageDialog(this,"Enter valid mailid");
			tf4.requestFocus();
			return;
		}
		if(address.length() <=0 || address == null){
            JOptionPane.showMessageDialog(this,"Address must be enter");
            tf5.requestFocus();
            return;
        }
        Socket socket=new Socket("localhost",1200);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
        Object req[]={"register",uname,pass,contact,email,address};
        out.writeObject(req);
        out.flush();
        Object res[]=(Object[])in.readObject();
		String msg = res[0].toString();
		if(msg.equals("Registration process completed")){
			JOptionPane.showMessageDialog(this,msg);
			setVisible(false);
		}else{
			JOptionPane.showMessageDialog(this,msg);
		}
    }catch(Exception e){
        e.printStackTrace();
    }
}   
public void clearFields(){
    tf1.setText("");
    tf2.setText("");
    tf3.setText("");
    tf4.setText("");
    tf5.setText("");
}
private static boolean validatePhoneNumber(String phoneNo){
	//validate phone numbers of format "1234567890"
    if(phoneNo.matches("\\d{10}"))
		return true;
	//validating phone number with -, . or spaces
    else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
		return true;
	//validating phone number with extension length from 3 to 5
	else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
		return true;
	//validating phone number where area code is in braces ()
    else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
		return true;
	//return false if nothing matches the input
    else
		return false;
         
}
}
