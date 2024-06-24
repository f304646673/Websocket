package com.nyctlc.stomprbmqchatroom.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.nyctlc.stomprbmqchatroom.component.RabbitMQProperties;
import com.nyctlc.stomprbmqchatroom.component.UserChannel;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @Autowired
    private UserChannel userChannel;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/handshark/{uid}").addInterceptors(handshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    System.out.println("STOMP Connection Closed");
                    String uid = (String) accessor.getSessionAttributes().get("uid");
                    userChannel.removeChannel(uid);
                } else if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    System.out.println("STOMP Connection Established");
                    String uid = (String) accessor.getSessionAttributes().get("uid");
                    userChannel.addChannel(uid, channel);
                }
                return message;
            }
        });
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

    private HandshakeInterceptor handshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    String path = request.getURI().getPath();
                    String prefix = "/handshark/";
                    String uid = path.substring(path.indexOf(prefix) + prefix.length());
                    if (uid.isEmpty()) {
                        return false;
                    }

                    if (uid.contains("/")) {
                        return false;
                    }

                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    HttpSession session = servletRequest.getServletRequest().getSession();
                    attributes.put("sessionId", session.getId());
                    attributes.put("uid", uid);
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, @Nullable Exception exception) {
            }
        };
    }
}