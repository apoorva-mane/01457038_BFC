package BFC;
import java.security.MessageDigest;
public class SHA{
public static String ShaSignature(byte chunk[]){
	StringBuilder sb = new StringBuilder();
	try{
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(chunk);
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
    }catch(Exception e){
		e.printStackTrace();
	}
	return sb.toString();
}
}