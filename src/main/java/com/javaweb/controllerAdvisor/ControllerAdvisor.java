package com.javaweb.controllerAdvisor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.javaweb.customException.FieldRequiredException;
import com.javaweb.model.ErrorResponseDTO;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
	//Với các lỗi nào thì hàm sử lý hàm nào? ArithmeticException : Toán học
	@ExceptionHandler(ArithmeticException.class)
	//ArithmeticException ex --> getMessage --> set error 
	public ResponseEntity<Object> handleArithmetucException(ArithmeticException ex, WebRequest request) {
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		errorResponseDTO.setError(ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("Số nguyên không chia được cho số 0");
		errorResponseDTO.setDetail(details);
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(FieldRequiredException.class)
	public ResponseEntity<Object> handleFieldRequireException(FieldRequiredException ex, WebRequest request){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		errorResponseDTO.setError(ex.getMessage());
		List<String> details = new ArrayList<String>();
		details.add("Check Check name or number because we can null");
		errorResponseDTO.setDetail(details);
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_GATEWAY);
	}
}
