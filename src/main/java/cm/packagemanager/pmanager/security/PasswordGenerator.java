package cm.packagemanager.pmanager.security;

import java.util.Base64;

public class PasswordGenerator {

	private static final byte[] keyValue = new byte[] { 'T', 'E', 'S', 'T' };
	
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rnd = new Random(System.currentTimeMillis());
    private static private final int LENGHT = 10;

	public static String encrypt(String strToEncrypt){

		try
		{
			strToEncrypt=strToEncrypt+"@tr@vel";
			return Base64.getEncoder().encodeToString(strToEncrypt.getBytes());
		}
		catch (Exception e)
		{
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt) {

		try {
			Base64.Decoder decoder = Base64.getDecoder();
			String[] password=new String(decoder.decode(strToDecrypt)).split("@");
			return password[0];

		} catch (Exception e) {

		}
		return null;

	}
	
	
 
    public static String generate() {
 
    	StringBuffer sb = new StringBuffer(LENGHT);
        /*StringBuilder sb = new StringBuilder(LENGHT);*/
        for (int i = 0; i < LENGHT; i++) {
            sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        }
        return sb.toString();

    }


}
