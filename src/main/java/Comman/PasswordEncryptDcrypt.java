package Comman;
import java.util.Base64; 
public class PasswordEncryptDcrypt {
	 
//	    public static void main(String[] args) {  
//	         PasswordEncryptDcrypt passwordEncryptDcrypt = new PasswordEncryptDcrypt();
//	         String output = passwordEncryptDcrypt.encrypt("Abhishek123$"); 
//	         System.out.println(output);
//	         System.out.println(passwordEncryptDcrypt.decrpyt(output));
//	    }  
	       
	    public String encrypt(String password) {
	    	Base64.Encoder encoder = Base64.getUrlEncoder();  
	        password = encoder.encodeToString(password.getBytes());  
	        return password;
	    }
	    
	    public String decrpyt(String password) {
	    	if(password == null) {
	    		return "NA";
	    	}
	    	Base64.Decoder decoder = Base64.getUrlDecoder();  
	        password = new String(decoder.decode(password)); 
	        return password;
	    }
	    
	 

}
