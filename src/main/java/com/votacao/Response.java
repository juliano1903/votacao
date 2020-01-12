package com.votacao;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Response<T> {
	
	private T data;
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY) 
	private List<String> errors;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getErrors() {
		if (this.errors == null) {
			errors = new ArrayList<>();
		}
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
