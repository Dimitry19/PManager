package cm.packagemanager.pmanager.common.mail.ent.vo;

import javax.persistence.*;

@Entity(name = "ContactUSVO")
@Table(name="CONTACT_US", schema = "PUBLIC")
public class ContactUSVO extends EmailVO{


	private long id;
	private String pseudo;

	public ContactUSVO(){
		super();
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "PSEUDO_SENDER")
	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
}
