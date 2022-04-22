package cm.travelpost.tp.common.sms.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "sms_otp")
@NamedQueries({

		@NamedQuery(name = SmsOTPVO.FIND_BY_ID, query = "select u from SmsOTPVO u where u.id  =:id"),
		@NamedQuery(name = SmsOTPVO.OTP, query = "select u from SmsOTPVO u where u.otp=:otp "),
		@NamedQuery(name = SmsOTPVO.PHONE, query = "select u from SmsOTPVO u where u.phoneNumber like :phone "),
})
@Where(clause = FilterConstants.FILTER_WHERE_SMS_OTP_CANCELLED)
public class SmsOTPVO extends CommonVO {

	private static final long serialVersionUID = 6181438160745607760L;

	public static final String FIND_BY_ID = "cm.travelpost.tp.user.ent.vo.otp.SmsOTPVO.findById";
	public static final String OTP = "cm.travelpost.tp.user.ent.vo.otp.SmsOTPVO.findByOtp";
	public static final String PHONE = "cm.travelpost.tp.user.ent.vo.otp.SmsOTPVO.findByPhone";



	private Long id;
	private int otp;
	private String phoneNumber;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	@Basic(optional = false)
	@Column(name = "otp_code", nullable = false, unique = true)
	public int getOtp() {
		return otp;
	}

	@Basic(optional = false)
	@Column(name = "phone_number", nullable = false)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
