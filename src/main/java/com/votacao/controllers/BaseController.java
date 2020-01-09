package com.votacao.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.votacao.Response;

public class BaseController<T> {

	public ResponseEntity<Response<T>> geraBadRequestResponse(Response<T>  response, BindingResult result) {
		result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(response);
	}
	
	public URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();
	}
}
