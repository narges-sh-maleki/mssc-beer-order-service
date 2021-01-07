package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.common.events.ValidateBeerOrderRequest;
import guru.sfg.brewery.common.events.ValidateBeerOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListenerStub {
    private  final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER)
    public void listener(ValidateBeerOrderRequest request){
        log.debug("########################### Validate Order Listener Stub");

        UUID orderId = request.getBeerOrderDto().getId();
        ValidateBeerOrderResult result = ValidateBeerOrderResult.builder()
                .orderId(orderId)
                .validated(true).build();


        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT,result);
    }
}
