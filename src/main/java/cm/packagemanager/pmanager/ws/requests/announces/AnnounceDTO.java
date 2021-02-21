package cm.packagemanager.pmanager.ws.requests.announces;

import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.util.Date;

public class AnnounceDTO {

	@NotNull(message = "Le lieu de depart de l'annonce doit etre valorisé")
	@NotBlank(message = "Le lieu de depart de l'annonce doit etre valorisé")
	private String departure;

	@NotNull(message = "Le lieu d'arrivée de l'annonce doit etre valorisé")
	private String arrival;

	@NotNull(message = "La description doit etre valorisé")
	private String description;

	@NotNull(message = "Le prix au Kg doit etre valorisé")
	@PositiveOrZero(message = "Le prix au Kg doit etre valorisé")
	private String price;

	private String preniumPrice;

	private String goldPrice;

	@NotNull(message = "Le nombre de Kg disponibles doit etre positif")
	@Positive(message = "Le nombre de Kg disponibles doit etre positif")
	private String weigth;

	@NotNull(message = "Le type d'annonce doit etre valorisé")
	@Enumerated(EnumType.STRING)
	private AnnounceType announceType;

	@NotNull(message = "La modalité de transport doit etre valorisé")
	@Enumerated(EnumType.STRING)
	private TransportEnum transport;

	@NotNull(message = "L'id  de l'utilisateur de l'annonce doit etre valorisé")
	private Long userId;

	private String category;

	@NotNull(message = "La date de depart de l'annonce doit etre valorisé")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@FutureOrPresent(message = "la date de depart doit être une date dans le présent ou le futur")
	private Date startDate;


	@NotNull(message = "La date de retour de l'annonce doit etre valorisé")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
	@FutureOrPresent(message = "la date retour doit être une date dans le présent ou le futur")
	private Date endDate;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPreniumPrice() {
		return preniumPrice;
	}

	public void setPreniumPrice(String preniumPrice) {
		this.preniumPrice = preniumPrice;
	}

	public String getWeigth() {
		return weigth;
	}

	public void setWeigth(String weigth) {
		this.weigth = weigth;
	}

	public AnnounceType getAnnounceType() {
		return announceType;
	}

	public void setAnnounceType(AnnounceType announceType) {
		this.announceType = announceType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public TransportEnum getTransport() {
		return transport;
	}

	public void setTransport(TransportEnum transport) {
		this.transport = transport;
	}

	public String getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(String goldPrice) {
		this.goldPrice = goldPrice;
	}
}
