package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.common.events.AllocateBeerOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocateBeerOrderResultListener {

    private final BeerOrderManager beerOrderManager;
    private  final  BeerOrderMapper beerOrderMapper;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESULT)
    public void allocateOrderResultListener(AllocateBeerOrderResult allocateBeerOrderResult){

        log.debug("################ Allocate Result Listener");

        beerOrderManager.processAllocationResult(beerOrderMapper.dtoToBeerOrder( allocateBeerOrderResult.getBeerOrderDto()),
                allocateBeerOrderResult.getAllocationError(),
                allocateBeerOrderResult.getPendingInventory());



    }
}
