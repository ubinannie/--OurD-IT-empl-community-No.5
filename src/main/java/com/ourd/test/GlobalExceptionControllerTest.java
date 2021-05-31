package com.ourd.test;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionControllerTest {

	@ExceptionHandler(value={NullPointerException.class, IllegalArgumentException.class})
	public String nullIllegalArgumentException(Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>alert('"+e.getMessage()+"');");
		sb.append("location.href='/';</script>");
		return sb.toString();
	}
}