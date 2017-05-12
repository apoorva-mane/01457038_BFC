package BFC;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.util.Map;
import java.io.FileWriter;
public class Processor extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	DefaultTableModel dtm;
	HashMap<String,FileInfo> map;
public Processor(Socket soc,DefaultTableModel dtm,HashMap<String,FileInfo> map){
	socket = soc;
	this.dtm = dtm;
	this.map = map;
	try{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }catch(Exception e){
        e.printStackTrace();
    }
}
@Override
public void run(){
    try{
		Object input[]=(Object[])in.readObject();
		if(input != null){
			String request = (String)input[0];
			if(request.equals("register")){
				String user = (String)input[1];
				String pass = (String)input[2];
				String contact = (String)input[3];
				String email = (String)input[4];
				String address = (String)input[5];
				String inputdata[]={user,pass,contact,email,address};
				String msg = DBCon.register(inputdata);
				if(msg.equals("Registration process completed")){
					File file = new File("DummyCloud/"+user);
					if(!file.exists())
						file.mkdir();
					Object res[] = {msg};
					dtm.addRow(res);
					out.writeObject(res);
					out.flush();
				}else{
					Object res[] = {msg};
					dtm.addRow(res);
					out.writeObject(res);
					out.flush();
				}
			}
			if(request.equals("login")){
				String user = (String)input[1];
				String pass = (String)input[2];
				String inputdata[]={user,pass};
				String msg = DBCon.login(inputdata);
				if(msg.equals("success")){
					Object res[] = {msg};
					String s1[] = {user+" User successfully login"};
					dtm.addRow(s1);
					out.writeObject(res);
					out.flush();
				}else{
					String s1[] = {user+" User unsuccessfull login"};
					Object res[] = {msg};
					dtm.addRow(s1);
					out.writeObject(res);
					out.flush();
				}
			}
			if(request.equals("upload")){
				Object row[][]=(Object[][])input[1];
				String user = input[2].toString().trim();
				String fname = input[3].toString().trim();
				String sha = input[4].toString().trim();
				String size = input[5].toString().trim();
				String start_chunk_id = null;
				for(int i=0;i<row.length;i++){
					byte b[] = (byte[])row[i][0];
					String blockname = row[i][1].toString();
					if(i == 0)
						start_chunk_id = blockname;
				}
				String refrence = checkRefrence(sha);
				FileInfo fi = new FileInfo(fname,user+"_"+fname,sha,refrence,start_chunk_id,row.length,Long.parseLong(size),TFileStatus.goodcompleted_file,row);
				DBCon.addFile(user,fname,user+"_"+fname,sha,refrence,start_chunk_id,row.length,Long.parseLong(size),TFileStatus.goodcompleted_file);
				map.put(user+"_"+fname,fi);
				if(refrence.equals("none")){
					StringBuilder buffer1 = new StringBuilder();
					StringBuilder buffer2 = new StringBuilder();
					File file = new File("DummyCloud/"+user+"/"+fname);
					file.mkdir();
					for(int i=0;i<row.length;i++){
						byte b[] = (byte[])row[i][0];
						String blockname = row[i][1].toString();
						FileOutputStream fout = new FileOutputStream(file.getPath()+"/"+blockname);
						fout.write(b,0,b.length);
						fout.flush();
						fout.close();
						File ftemp = new File(file.getPath()+"/"+blockname);
						buffer1.append(fname+","+blockname+","+ftemp.length()+","+SHA.ShaSignature(b)+System.getProperty("line.separator"));
						if(i == 0)
							buffer2.append(fname+","+blockname+","+ftemp.length()+","+SHA.ShaSignature(b)+System.getProperty("line.separator"));
						if(i == (row.length-1))
							buffer2.append(fname+","+blockname+","+ftemp.length()+","+SHA.ShaSignature(b)+System.getProperty("line.separator"));
					}
					FileWriter meta_drop = new FileWriter("metadata/drop_"+fname+".txt");
					FileWriter bfc_drop = new FileWriter("metadata/bfc_"+fname+".txt");
					meta_drop.write(buffer1.toString());
					meta_drop.close();
					bfc_drop.write(buffer2.toString());
					bfc_drop.close();
				}
				Object res[] = {fname+" saved on cloud server"};
				dtm.addRow(res);
				out.writeObject(res);
				out.flush();
			}
			if(request.equals("getfilename")){
				String user = (String)input[1];
				StringBuilder sb = new StringBuilder();
				for(Map.Entry<String,FileInfo> entry : map.entrySet()){
					String key = entry.getKey();
					String arr[] = key.split("_");
					if(arr[0].equals(user))
						sb.append(arr[1]+",");
				}
				if(sb.length() > 0)
					sb.deleteCharAt(sb.length()-1);
				Object res[] = {sb.toString()};
				out.writeObject(res);
				out.flush();
			}
			if(request.equals("download")){
				String file = (String)input[1];
				String user = (String)input[2];
				Object chunks[][] = null;
				for(Map.Entry<String,FileInfo> entry : map.entrySet()){
					String key = entry.getKey();
					if(key.equals(user+"_"+file)){
						FileInfo fi = entry.getValue();
						chunks = fi.row;
						break;
					}
				}
				Object res[] = {chunks};
				out.writeObject(res);
				out.flush();
				String s1[] = {"File sent to user"};
				dtm.addRow(s1);
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
}
public String checkRefrence(String sha){
	String result = "none";
	for(Map.Entry<String,FileInfo> entry : map.entrySet()){
		String key = entry.getKey();
		FileInfo fi = entry.getValue();
		if(fi.sha.equals(sha)){
			result = key;
			break;
		}
	}
	return result;
}
}
