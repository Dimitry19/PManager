package cm.packagemanager.pmanager.ws.responses;

public class ReservationsByUserResponse {

	PaginateResponse reservations;
	PaginateResponse receivedReservations;

	public ReservationsByUserResponse() {
	}

	public PaginateResponse getReservations() {
		return reservations;
	}

	public PaginateResponse getReceivedReservations() {
		return receivedReservations;
	}

	public void setReservations(PaginateResponse reservations) {
		this.reservations = reservations;
	}

	public void setReceivedReservations(PaginateResponse receivedReservations) {
		this.receivedReservations = receivedReservations;
	}
}
