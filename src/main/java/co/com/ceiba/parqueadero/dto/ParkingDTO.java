package co.com.ceiba.parqueadero.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private LocalDateTime inDatetime;
	private LocalDateTime outDatetime;
	private BigDecimal fare;
	private String totalTime;
	private VehicleDTO vehicleDTO;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getInDatetime() {
		return inDatetime;
	}

	public void setInDatetime(LocalDateTime inDatetime) {
		this.inDatetime = inDatetime;
	}

	public LocalDateTime getOutDatetime() {
		return outDatetime;
	}

	public void setOutDatetime(LocalDateTime outDatetime) {
		this.outDatetime = outDatetime;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public VehicleDTO getVehicleDTO() {
		return vehicleDTO;
	}

	public void setVehicleDTO(VehicleDTO vehicleDTO) {
		this.vehicleDTO = vehicleDTO;
	}

}
