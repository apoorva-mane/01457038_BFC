package BFC;
import java.io.Serializable; 
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
public class AES {
public static byte[] generateKey()throws Exception{
	File file = new File("key.txt");
	if(!file.exists()){
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		SecretKey key = keyGen.generateKey();
		byte keys[] = key.getEncoded();
		FileOutputStream out = new FileOutputStream("key.txt");
		out.write(keys,0,keys.length);
		out.close();
	}
	FileInputStream fin = new FileInputStream("key.txt");
	byte[] key = new byte[fin.available()];
	fin.read(key,0,key.length);
	fin.close();
	return key;
}

public static byte[] encrypt(byte[] unencrypted){
	byte[] ciphertext = null;
	try{
		byte keys[] = generateKey();
		SecretKey key = new SecretKeySpec(keys,0,keys.length,"AES");
		Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, key);
		ciphertext = aes.doFinal(unencrypted);
	}catch(Exception e){
		e.printStackTrace();
	}
	return ciphertext;
}
public static byte[] decrypt(byte[] encrypted){
	byte[] decrypt = null;
	try{
		byte keys[] = generateKey();
		SecretKey key = new SecretKeySpec(keys,0,keys.length,"AES");
		Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, key);
		decrypt = aes.doFinal(encrypted);
	}catch(Exception e){
		e.printStackTrace();  
	}
	return decrypt;  
}
}
 