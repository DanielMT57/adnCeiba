package co.com.ceiba.parqueadero.util;

import java.util.regex.Pattern;

import co.com.ceiba.parqueadero.exception.ParkingException;

public class Validation {

	private Validation() {

	}

	public static void checkNullOrEmpty(String data, String paramName) throws ParkingException {
		if (data == null || data.equals("")) {
			throw new ParkingException("El par\u00E1metro " + paramName + "es nulo o vac\u00EDo");
		}
	}

	public static void checkNull(Object obj, String paramName) throws ParkingException {
		if (obj == null) {
			throw new ParkingException("El par\u00E1metro " + paramName + "es nulo");
		}
	}
}
