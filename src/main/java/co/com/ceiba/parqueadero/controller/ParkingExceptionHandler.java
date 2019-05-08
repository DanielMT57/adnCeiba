package co.com.ceiba.parqueadero.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.com.ceiba.parqueadero.dto.ErrorDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;

@ControllerAdvice
public class ParkingExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ParkingException.class })
    @ResponseBody
    public ErrorDTO parkingException(Exception exception) {
        return new ErrorDTO(exception);
    }
}
