package com.example.apigateway;

import com.example.apigateway.filter.AddHeaderPostFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApiGatewayApplicationTests {

    @Autowired
    private AddHeaderPostFilter addHeaderPostFilter;

    @Test
    void testAddHeaderPostFilter() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/customers").build()
        );

        GatewayFilterChain chain = filterExchange -> Mono.empty();

        addHeaderPostFilter.filter(exchange, chain).block();

        assertThat(exchange.getResponse().getHeaders().getFirst("X-System-Name"))
                .isEqualTo("Api-Gateway-System");
    }
}
