package org.rj.api_gateway.routing;

import lombok.RequiredArgsConstructor;
import org.rj.api_gateway.filter.GatewayAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class Routes {

    private final GatewayAuthenticationFilter filter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("auth-service",
                        route -> route.path("/api/v1/auth/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://auth-service"))
                .route("user-service",
                        route -> route.path("/api/v1/profile/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://user-service"))

                .build();
    }
}
