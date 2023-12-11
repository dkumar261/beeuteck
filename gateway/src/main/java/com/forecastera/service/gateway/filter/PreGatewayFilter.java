package com.forecastera.service.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.gateway.constants.GatewayConstants;
import com.forecastera.service.gateway.model.ErrorResponse;
import com.forecastera.service.gateway.service.UserValidationService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PreGatewayFilter extends AbstractGatewayFilterFactory<PreGatewayFilter.Config> {

	private final ObjectMapper mapper;

	@Autowired
	private UserValidationService userValidationService;

	/**
	 * This is initialization the super class.
	 */
	public PreGatewayFilter() {
		super(Config.class);
		this.mapper = new ObjectMapper();
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {

			ServerHttpResponse response = exchange.getResponse();
			String path = exchange.getRequest().getURI().getPath();
			if (path.equalsIgnoreCase(GatewayConstants.LOGIN_URL)) {
				return chain.filter(exchange);
			}

			else {
				ServerHttpRequest request = exchange.getRequest();
				HttpHeaders headers = request.getHeaders();

				String authHeader = headers.getFirst(GatewayConstants.ACCESS_TO_TOKEN);
				String username = headers.getFirst(GatewayConstants.USER_NAME);
				ErrorResponse errorResponse = userValidationService.accessTokenValidation(username, authHeader);
				if (errorResponse == null) {
					return chain.filter(exchange);
				}

				DataBuffer dataBuffer = populateResponse(errorResponse, response);
				return response.writeWith(Mono.just(dataBuffer));
			}
		};
	}

	private DataBuffer populateResponse(ErrorResponse errorResponse, ServerHttpResponse response) {
		DataBuffer buffer = null;
		try {
			byte[] writeValueAsBytes = mapper.writeValueAsBytes(errorResponse);
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			buffer = response.bufferFactory().wrap(writeValueAsBytes);
		} catch (JsonProcessingException e) {
			log.error("Error while processing buffer data ");
		}
		return buffer;
	}

	public static class Config {
		// Put the configuration properties
	}
}
