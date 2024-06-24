package com.nyctlc.stomprbmqchatroom.component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProperties {
    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    public String getRabbitmqPassword() {
        return rabbitmqPassword;
    }

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    public String getRabbitmqUsername() {
        return rabbitmqUsername;
    }

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    public String getRabbitmqHost() {
        return rabbitmqHost;
    }

    @Value("${spring.rabbitmq.port}")
    private String rabbitmqPort;

    public String getRabbitmqPort() {
        return rabbitmqPort;
    }

    @Value("${spring.rabbitmq.stomp.port}")
    private String rabbitmqStompPort;

    public String getRabbitmqStompPort() {
        return rabbitmqStompPort;
    }
}
