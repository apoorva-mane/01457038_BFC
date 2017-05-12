package BFC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
public class DBCon{
    private static Connection con;
public static Connection getCon()throws Exception {
	Class.forName("com.mysql.jdbc.Driver");
	con = DriverManager.getConnection("jdbc:mysql://localhost/BFC","root","root");
	return con;
}
public static String register(String[] input)throws Exception{
	String msg="Error in registration";
    boolean flag=false;
    con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select username from bfc_users where username='"+input[0]+"'");
    if(rs.next()){
        flag=true;
        msg = "Username already exist";
    }
    if(!flag){
		PreparedStatement stat=con.prepareStatement("insert into bfc_users values(?,?,?,?,?)");
		stat.setString(1,input[0]);
		stat.setString(2,input[1]);
		stat.setString(3,input[2]);
		stat.setString(4,input[3]);
		stat.setString(5,input[4]);
		int i=stat.executeUpdate();
		if(i > 0){
			msg = "Registration process completed";
		}
		stat.close();
    }
	rs.close();stmt.close();con.close();
    return msg;
}
public static String login(String input[])throws Exception{
    String msg="fail";
    con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select username from bfc_users where username='"+input[0]+"' && password='"+input[1]+"'");
    if(rs.next()){
        msg = "success";
    }
	rs.close();stmt.close();con.close();
    return msg;
}
public static void loadFileInfo(HashMap<String,FileInfo> map)throws Exception{
	con = getCon();
    Statement stmt=con.createStatement();
    ResultSet rs=stmt.executeQuery("select * from fileinfo");
    while(rs.next()){
		String user = rs.getString(1);
		String file = rs.getString(2);
		String refrence = rs.getString(5);
		if(!refrence.equals("none")){
			String arr[] = refrence.split("_");
			user = arr[0];
			file = arr[1];
		}
		File path = new File("DummyCloud/"+user+"/"+file);
		int chunks = rs.getInt(7);
		String ext = file.substring(file.lastIndexOf(".")+1,file.length());
		Object row[][] = new Object[chunks][2];
		for(int i=0;i<chunks;i++){
			String name = file+"_chunk"+i+"."+ext;
			File fi = new File(path.getPath()+"/"+name);
			if(fi.exists()){
				FileInputStream fin = new FileInputStream(path.getPath()+"/"+name);
				byte b[] = new byte[fin.available()];
				fin.read(b,0,b.length);
				fin.close();
				row[i][0] = b;
				row[i][1]=name;
			}
		}
		FileInfo fi = new FileInfo(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7),rs.getLong(8),TFileStatus.goodcompleted_file,row);
		map.put(rs.getString(3),fi);
	}
	rs.close();stmt.close();con.close();
}
public static String addFile(String user,String fname,String file_id,String sha,String refrence,String start_chunk_id,int num_chunk,long size,int status)throws Exception{
	String msg="Error in saving file info";
    con = getCon();
	PreparedStatement stat=con.prepareStatement("delete from fileinfo where username=? and filename=? and fileid=?");
	stat.setString(1,user);
	stat.setString(2,fname);
	stat.setString(3,file_id);
	stat.executeUpdate();
	stat.close();
   	stat=con.prepareStatement("insert into fileinfo values(?,?,?,?,?,?,?,?,?)");
	stat.setString(1,user);
	stat.setString(2,fname);
	stat.setString(3,file_id);
	stat.setString(4,sha);
	stat.setString(5,refrence);
	stat.setString(6,start_chunk_id);
	stat.setInt(7,num_chunk);
	stat.setLong(8,size);
	stat.setInt(9,status);
	int i=stat.executeUpdate();
	if(i > 0)
		msg = "File info details saved";
	stat.close();con.close();
	return msg;
}
}
