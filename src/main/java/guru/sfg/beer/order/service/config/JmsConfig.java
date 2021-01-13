package guru.sfg.beer.order.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public final static String VALIDATE_ORDER = "validate-order";
    public static final String VALIDATE_ORDER_RESULT = "validate-order-result";
    public static final String ALLOCATE_ORDER_RESULT = "allocate-order-result";
    public final static String ALLOCATE_ORDER = "allocate-order";
    public static final String FAILED_ALLOCATION_COMPENSATION_TRX = "FAILED_ALLOCATION_COMPENSATION_TRX";
    public static final String CANCEL_ORDER = "cancel-order";

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper){

        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName("_type");
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }
}
