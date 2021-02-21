package cm.packagemanager.pmanager.ws.requests.announces;

import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.ws.requests.CommonSearchDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;


public class AnnounceSearchSearchDTO extends CommonSearchDTO {

	private String departure;

	private String arrival;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@FutureOrPresent(message = "la date de depart oit être une date dans le présent ou le futur")
	private Date startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@FutureOrPresent(message = "la date retour doit être une date dans le présent ou le futur")
	private Date endDate;

	private String weigth;

	private String announceType;

	private String transport;

	private String category;

	private String price;

	private String user;

	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getWeigth() {
		return weigth;
	}

	public void setWeigth(String weigth) {
		this.weigth = weigth;
	}

	public String getAnnounceType() {
		return announceType;
	}

	public void setAnnounceType(String announceType) {
		this.announceType = announceType;
	}


	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
