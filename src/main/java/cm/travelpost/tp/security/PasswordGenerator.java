package cm.travelpost.tp.security;


import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Random;

public class PasswordGenerator {
    private static Logger log = (Logger) LoggerFactory.getLogger(PasswordGenerator.class);
    private PasswordGenerator(){
        log.info("Utility class");
    }


    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rnd = new Random(System.currentTimeMillis());
    private static final int LENGHT = 10;

    public static String encrypt(String strToEncrypt) {

        try {
            strToEncrypt = strToEncrypt + "@tr@vel";
            return Base64.getEncoder().encodeToString(strToEncrypt.getBytes());
        } catch (Exception e) {
            log.error("Error while encrypting {}: " , e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {

        try {
            Base64.Decoder decoder = Base64.getDecoder();
            String[] password = new String(decoder.decode(strToDecrypt)).split("@");
            return password[0];

        } catch (Exception e) {
            log.error("Error while decrypting {}: " , e);
        }
        return null;

    }
    public static String generate() {
       StringBuffer sb = new StringBuffer(LENGHT);
        for (int i = 0; i < LENGHT; i++) {
            sb.append(ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
