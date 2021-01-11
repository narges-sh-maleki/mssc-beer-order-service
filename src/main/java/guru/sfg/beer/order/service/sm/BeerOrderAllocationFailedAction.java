package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.common.events.AllocationFailureEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeerOrderAllocationFailedAction implements Action<BeerOrderStatusEnum, BeerOrderEvents> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEvents> context) {
        String orderId = context.getMessageHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER).toString();
        log.debug("Compensating Trx for:" + orderId);
        AllocationFailureEvent event = AllocationFailureEvent.builder().orderId(UUID.fromString(orderId)).build();
        jmsTemplate.convertAndSend(JmsConfig.FAILED_ALLOCATION_COMPENSATION_TRX,event);

        log.debug("################## Send Allocation Failure Request for:" + orderId );


    }
}
