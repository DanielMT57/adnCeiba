package co.com.ceiba.parqueadero.util;

import co.com.ceiba.parqueadero.exception.ParkingException;

public final class Validation {

	private Validation() {

	}

	public static void checkNullOrEmpty(String data, String paramName) throws ParkingException {
		if (data == null || "".equals(data)) {
			throw new ParkingException("El par\u00E1metro " + paramName + "es nulo o vac\u00EDo");
		}
	}

	public static void checkNull(Object obj, String paramName) throws ParkingException {
		if (obj == null) {
			throw new ParkingException("El par\u00E1metro " + paramName + "es nulo");
		}
	}
}
