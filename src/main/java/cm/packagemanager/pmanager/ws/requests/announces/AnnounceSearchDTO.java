package cm.packagemanager.pmanager.ws.requests.announces;

import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.ws.requests.CommonSearchDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;


public class AnnounceSearchDTO extends CommonSearchDTO {

    private String departure;

    private String arrival;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DateUtils.STD_PATTERN)
    @JsonFormat(pattern = DateUtils.STD_PATTERN)
    private long startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = DateUtils.STD_PATTERN)
    @JsonFormat(pattern = DateUtils.STD_PATTERN)
    private long endDate;

    private String weigth;

    private String announceType;

    private String transport;

    private String category;

    private BigDecimal price;

    private String user;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
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
