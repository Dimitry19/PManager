package cm.packagemanager.pmanager.utils;


import cm.packagemanager.pmanager.security.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe permettant de créer des mots de passe hachés pour des besoins de tests
 *et d'initialisation de la base de données
 *
 */
public class PasswordEncoder {

	private static BCryptPasswordEncoder bCryptPasswordEncoder;

	public static void main(String[] args) {
		/*bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String toDecrypt="$2a$10$LWwcozXhlpodDoSgeZSD7emHVIibpJGq.KZCSDO1JYUvJmsYxLYi6";
		String password ="password2";
		String encodedPassword = bCryptPasswordEncoder.encode(password);
		System.out.println("Mot de passe en clair : "+password);
		System.out.println("Mot de passe haché : "+encodedPassword);
		//Pour vérifier que le mot de passe haché correspond bien au mot de passe initial, il utiliser la méthode bCryptPasswordEncoder.matches(x, y)
		System.out.println("Le mot de passe est bien haché : "+bCryptPasswordEncoder.matches(password, encodedPassword));
		System.out.println("Decrypt : "+toDecrypt);*/

		String e =PasswordGenerator.encrypt("password2");
		System.out.println("encrypt : "+e);
		String d =PasswordGenerator.decrypt(e);
		System.out.println("decrypt : "+d);

	}

}