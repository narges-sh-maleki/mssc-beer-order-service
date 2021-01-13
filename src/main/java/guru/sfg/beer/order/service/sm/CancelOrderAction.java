package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import guru.sfg.brewery.common.events.CancelOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEvents> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEvents> context) {
        String orderId =  Objects
                .requireNonNullElseGet(context.getMessageHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER).toString(),() -> {throw new RuntimeException("order id is null");});

        jmsTemplate.convertAndSend(JmsConfig.CANCEL_ORDER, CancelOrderRequest.builder().orderId(UUID.fromString(orderId)).build());

        log.debug("Sending Request for Cancel Order: " + orderId);
    }
}
