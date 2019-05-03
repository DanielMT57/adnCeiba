package co.com.ceiba.parqueadero.dto;

public class ErrorDTO {

	private String message;

	public ErrorDTO(String message) {
		super();
		this.message = message;
	}
	
	public ErrorDTO(Exception ex) {
		this.message = ex.getMessage();
	}

	public ErrorDTO() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
