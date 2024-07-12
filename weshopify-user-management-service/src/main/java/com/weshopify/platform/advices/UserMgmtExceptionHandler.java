package com.weshopify.platform.advices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.weshopify.platform.exceptions.APIException;
import com.weshopify.platform.validations.UserException;
import com.weshopify.platform.validations.UserExceptionList;

@RestControllerAdvice
public class UserMgmtExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<Object> handleAPIError(APIException apiException){
		UserException userexception = UserException.builder().message(apiException.getErrorMsg())
				.statusCode(apiException.getErrorCode()).timeStamp(new Date()).build();
		
		return ResponseEntity.badRequest().body(userexception);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<UserException> errorList = new ArrayList<>();
		
		ex.getBindingResult().getFieldErrors().stream().forEach(error -> {
			UserException userexception = UserException.builder().message(error.getDefaultMessage())
					.statusCode(status.value()).timeStamp(new Date()).build();
			
			errorList.add(userexception);
		});
		
		UserExceptionList validationMessages = UserExceptionList.builder().messages(errorList).build();
		return ResponseEntity.badRequest().body(validationMessages);
	}

}
