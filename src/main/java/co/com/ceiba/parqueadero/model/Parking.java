package co.com.ceiba.parqueadero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parkings")
public class Parking {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@Column(name = "in_datetime", nullable = false)
	private LocalDate inDatetime;

	@Column(name = "out_datetime")
	private LocalDate outDatetime;

	@Column(name = "total_time")
	private String totalTime;

	@Column(name = "fare")
	private BigDecimal fare;

	@ManyToOne
	@JoinColumn(name = "license_plate")
	private Vehicle vehicle;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getInDatetime() {
		return inDatetime;
	}

	public void setInDatetime(LocalDate inDatetime) {
		this.inDatetime = inDatetime;
	}

	public LocalDate getOutDatetime() {
		return outDatetime;
	}

	public void setOutDatetime(LocalDate outDatetime) {
		this.outDatetime = outDatetime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

}
