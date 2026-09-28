# Microservice Ss05-Ex04: Hậu xử lý với Post-Filter (Add Header)

## 1. Mục tiêu
- **Kiến thức**: Can thiệp và xử lý Response trước khi trả về cho Client bằng Post-Filter trong Spring Cloud Gateway.
- **Kỹ năng**: Thêm thông tin `X-System-Name: Api-Gateway-System` vào Response Header.

## 2. Các phương pháp cài đặt (2 cách)

### Cách 1: Cấu hình qua file `application.properties`
Sử dụng bộ lọc sẵn có `AddResponseHeader` của Spring Cloud Gateway trong file `application.properties`:
```properties
spring.cloud.gateway.default-filters[0]=AddResponseHeader=X-System-Name, Api-Gateway-System
```
Hoặc cấu hình theo từng Route cụ thể:
```properties
spring.cloud.gateway.routes[0].id=customer-service-route
spring.cloud.gateway.routes[0].uri=https://httpbin.org
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v1/customers/**
spring.cloud.gateway.routes[0].filters[0]=RewritePath=/api/v1/customers/(?<segment>.*), /${segment}
spring.cloud.gateway.routes[0].filters[1]=AddResponseHeader=X-System-Name, Api-Gateway-System
```

### Cách 2: Cấu hình qua Java Code (`AddHeaderPostFilter`)
Sử dụng `GlobalFilter` kết hợp phương thức `.then(Mono.fromRunnable(...))` để xử lý Post-Filter trên Response:
```java
@Component
public class AddHeaderPostFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            if (!headers.containsKey("X-System-Name")) {
                headers.add("X-System-Name", "Api-Gateway-System");
            }
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

## 3. Kiểm tra kết quả
1. Thao tác chạy dự án:
   ```bash
   mvn spring-boot:run
   ```
2. Gọi API qua Postman hoặc cURL:
   `GET http://localhost:8080/api/v1/customers/get`
3. Kiểm tra tab **Headers** của kết quả trả về, thấy xuất hiện:
   `X-System-Name: Api-Gateway-System`
