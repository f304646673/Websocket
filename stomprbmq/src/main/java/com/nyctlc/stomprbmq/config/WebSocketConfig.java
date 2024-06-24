package com.nyctlc.stomprbmq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.nyctlc.stomprbmq.component.RabbitMQProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/handshake");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/send");
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost(rabbitMQProperties.getRabbitmqHost())
                .setRelayPort(Integer.parseInt(rabbitMQProperties.getRabbitmqStompPort()))
                .setClientLogin(rabbitMQProperties.getRabbitmqUsername())
                .setClientPasscode(rabbitMQProperties.getRabbitmqPassword())
                .setSystemLogin(rabbitMQProperties.getRabbitmqUsername())
                .setSystemPasscode(rabbitMQProperties.getRabbitmqPassword());
    }
}