package cm.packagemanager.pmanager.ws.requests.announces;

import javax.validation.constraints.NotNull;

public class AnnounceDTO {

	@NotNull(message = "Le lieu de depart de l'annonce doit etre valorisé")
	private String departure;

	@NotNull(message = "Le lieu d'arrivée de l'annonce doit etre valorisé")
	private String arrival;

	@NotNull(message = "La date de depart de l'annonce doit etre valorisé")
	private String startDate;

	@NotNull(message = "La date d'arrivée de l'annonce doit etre valorisé")
	private String endDate;

	@NotNull(message = "La description doit etre valorisé")
	private String description;
	
	
	@NotNull(message = "Le prix au Kg doit etre valorisé")
	private String price;

	private String preniumPrice;

	private String goldPrice;

	@NotNull(message = "Le nombre de Kg disponibles doit etre valorisé")
	private String weigth;

	@NotNull(message = "Le type d'annonce doit etre valorisé")
	private String announceType;

	@NotNull(message = "La modalité de transport doit etre valorisé")
	private String transport;

	@NotNull(message = "L'id  de l'utilisateur de l'annonce doit etre valorisé")
	private Long userId;

	private String category;


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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getAnnounceType() {
		return announceType;
	}

	public void setAnnounceType(String announceType) {
		this.announceType = announceType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(String goldPrice) {
		this.goldPrice = goldPrice;
	}
}
