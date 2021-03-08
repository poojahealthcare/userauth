package com.hca.oauth.config;

import java.util.ArrayList;
import java.util.*;

public class APIResponse {
	
	String message;
	boolean result;
	String errorMessage;
	HashMap resultObject = new HashMap();
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Object getResultObject() {
		return resultObject;
	}
	public void add(String key,Object value) {
		resultObject.put(key, value);
	}
	
	
	
	

}
