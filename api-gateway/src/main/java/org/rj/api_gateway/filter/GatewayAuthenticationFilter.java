package org.rj.api_gateway.filter;

import lombok.RequiredArgsConstructor;
import org.rj.api_gateway.routing.RouterValidator;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@RequiredArgsConstructor
public class GatewayAuthenticationFilter implements GatewayFilter {


    private final RouterValidator routerValidator;
    private final RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (authMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            HttpHeaders headers = request.getHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            try {
               restTemplate.exchange("http://auth-service/api/v1/auth/validation", HttpMethod.POST, entity, Void.class);
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                    return onError(exchange, HttpStatus.valueOf(e.getStatusCode().value()));
                }
            }
        }
        return chain.filter(exchange);

    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
