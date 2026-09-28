package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * CÁCH 2: Hậu xử lý (Post-Filter) sử dụng Java Code Filter.
 * Can thiệp vào Response sau khi nhận phản hồi để thêm Header "X-System-Name: Api-Gateway-System".
 */
@Component
public class AddHeaderPostFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AddHeaderPostFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Thực thi request qua chain.filter(exchange), sau đó dùng .then(Mono.fromRunnable(...))
        // để thực hiện Post-Filter can thiệp vào Response Header trước khi gửi về client.
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            if (!headers.containsKey("X-System-Name")) {
                headers.add("X-System-Name", "Api-Gateway-System");
            }
            logger.info("[Post-Filter Java Code] Response Header Added: X-System-Name = Api-Gateway-System");
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
