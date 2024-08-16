package com.weshopify.platform.exceptions;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BrandsExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<Object> handleAPIError(APIException apiException){
		ApiExceptionsPayload userexception = ApiExceptionsPayload.builder().message(apiException.getErrorMsg())
				.statusCode(apiException.getErrorCode()).timeStamp(new Date()).build();
		
		return ResponseEntity.badRequest().body(userexception);
	}
	/*
	 * @Override protected ResponseEntity<Object>
	 * handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders
	 * headers, HttpStatusCode status, WebRequest request) {
	 * 
	 * List<ApiExceptionsPayload> errorList = new ArrayList<>();
	 * 
	 * ex.getBindingResult().getFieldErrors().stream().forEach(error -> {
	 * ApiExceptionsPayload userexception =
	 * ApiExceptionsPayload.builder().message(error.getDefaultMessage())
	 * .statusCode(status.value()).timeStamp(new Date()).build();
	 * 
	 * errorList.add(userexception); });
	 * 
	 * UserExceptionList validationMessages =
	 * UserExceptionList.builder().messages(errorList).build(); return
	 * ResponseEntity.badRequest().body(validationMessages); }
	 */

}
