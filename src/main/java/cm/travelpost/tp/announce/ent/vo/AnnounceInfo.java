package cm.travelpost.tp.announce.ent.vo;


import cm.travelpost.tp.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AnnounceInfo implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;


    private Long id;
    private String departure;
    private String arrival;
    private String warning;


    @JsonFormat(pattern = DateUtils.STD_PATTERN)
    private Date startDate;

    @JsonFormat(pattern = DateUtils.STD_PATTERN)
    private Date endDate;
    private String transport;
    private BigDecimal price;

    private BigDecimal goldPrice;
    private BigDecimal preniumPrice;
    private BigDecimal weight;
    private BigDecimal remainWeight;
    private Set<CategoryVO> categories = new HashSet<>();
    private Integer countReservation;

    public AnnounceInfo(AnnounceVO announce) {
        this.id = announce.getId();
        this.departure = announce.getDeparture();
        this.arrival = announce.getArrival();
        this.startDate = announce.getStartDate();
        this.endDate = announce.getEndDate();
        this.transport = announce.getTransport().toValue();
        this.price = announce.getPrice();
        this.goldPrice = announce.getGoldPrice();
        this.preniumPrice = announce.getPreniumPrice();
        this.weight = announce.getWeight();
        this.remainWeight = announce.getRemainWeight();
        this.countReservation = announce.getCountReservation();
        this.warning=announce.getWarning();
        this.categories.addAll(announce.getCategories());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(BigDecimal goldPrice) {
        this.goldPrice = goldPrice;
    }

    public BigDecimal getPreniumPrice() {
        return preniumPrice;
    }

    public void setPreniumPrice(BigDecimal preniumPrice) {
        this.preniumPrice = preniumPrice;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getRemainWeight() {
        return remainWeight;
    }

    public void setRemainWeight(BigDecimal remainWeight) {
        this.remainWeight = remainWeight;
    }

    public Integer getCountReservation() {
        return countReservation;
    }

    public void setCountReservation(Integer countReservation) {
        this.countReservation = countReservation;
    }
    public String getWarning() {  return warning; }

    public void setWarning(String warning) {  this.warning = warning; }
}
