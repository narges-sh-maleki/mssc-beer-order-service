package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.common.BeerOrderDto;
import guru.sfg.brewery.common.events.AllocateBeerOrderRequest;
import guru.sfg.brewery.common.events.AllocateBeerOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationListenerStub {
    private  final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER)
    public void listener(AllocateBeerOrderRequest request){
        log.debug("########################### Allocate Order Listener Stub");

        BeerOrderDto beerOrderDto = request.getBeerOrderDto();
        beerOrderDto.getBeerOrderLines().forEach(line -> {
            line.setQuantityAllocated(line.getOrderQuantity());
        });
        AllocateBeerOrderResult result = AllocateBeerOrderResult.builder()
                .beerOrderDto(beerOrderDto)
                .allocationError(false)
                .pendingInventory(false)
                .build();


        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT,result);

    }
}
