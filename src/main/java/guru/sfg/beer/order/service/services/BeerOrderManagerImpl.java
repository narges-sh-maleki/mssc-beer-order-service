package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.sm.BeerOrderStateInterceptor;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final StateMachineFactory stateMachineFactory;
    public static final String BEER_ORDER_ID_HEADER = "BEER_ORDER_ID_HEADER";
    private final BeerOrderStateInterceptor interceptor;
    private final BeerOrderMapper beerOrderMapper;

    @Override

    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder saved = beerOrderRepository.save(beerOrder);
        boolean result = sendEvent(saved, BeerOrderEvents.VALIDATE);
        if (!result)
            throw new RuntimeException("Event not accepted");
        return saved;
    }

    @Override
    public void processValidationResult(BeerOrder beerOrder, Boolean validationResult) {
        BeerOrder updatedBeerOrder1 = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
        if (validationResult) {

            sendEvent(updatedBeerOrder1, BeerOrderEvents.VALIDATION_PASSED);
            //get the object with the latest status from the DB because the interceptor has updated the status
            BeerOrder updatedBeerOrder2 = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
            sendEvent(updatedBeerOrder2,BeerOrderEvents.ALLOCATE);
        }
        else
            sendEvent(updatedBeerOrder1,BeerOrderEvents.VALIDATION_EXCEPTION);
    }

    @Override
    public void processAllocationResult(BeerOrder beerOrder, Boolean allocationError, Boolean pendingInventory) {
       //TODO: get from db
        BeerOrder updatedBeerOrder1 = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();

        if (allocationError) {
            sendEvent(updatedBeerOrder1, BeerOrderEvents.ALLOCATION_FAILED);
        }
        else if (pendingInventory) {
            sendEvent(updatedBeerOrder1, BeerOrderEvents.ALLOCATION_NO_INVENTORY);
        }
        else {
            sendEvent(updatedBeerOrder1, BeerOrderEvents.ALLOCATION_SUCCESS);
            updateAllocateQuantity(updatedBeerOrder1);
        }

    }
    private void updateAllocateQuantity(BeerOrder beerOrder){
        BeerOrder foundBeerOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();

        //complexity: order of n^2
        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
            foundBeerOrder.getBeerOrderLines().forEach( beerOrderLine1 ->{
                if (beerOrderLine.getQuantityAllocated() != beerOrderLine1.getQuantityAllocated())
                    beerOrderLine.setQuantityAllocated(beerOrderLine1.getQuantityAllocated());
            });
        });
/*
        //TODO: implement the hashcode and equal in the object
        //complexity :order of n
        beerOrder.getBeerOrderLines().forEach(beerOrderLine ->{
            //add operation in the Set, finds the equal element and if it exists it overrides it.
            foundBeerOrder.getBeerOrderLines().add(beerOrderLine);
        });

 */
    }

        public boolean sendEvent(BeerOrder beerOrder, BeerOrderEvents event){

        log.debug("############### Send Event: " + event.name().toString());
        StateMachine<BeerOrderStatusEnum,BeerOrderEvents> sm = buildStateMachine(beerOrder);

        //sm.sendEvent(event);
        return sm.sendEvent(MessageBuilder
                .withPayload(event)
                .setHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER,beerOrder.getId())
                .build());
    }

    /***
     * it retrieves the latest state from DB and sets it to the StateMachine and returns it.
     * it also configures the State Machine to write the state to the DB before every change in the state
     * @param beerOrder
     * @return StateMachine
     */
    private StateMachine<BeerOrderStatusEnum, BeerOrderEvents> buildStateMachine(BeerOrder beerOrder){

        StateMachine<BeerOrderStatusEnum,BeerOrderEvents> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
        //BeerOrder foundBeerOrder =  beerOrderRepository.findById(beerOrder.getId()).orElseGet(()->{throw new RuntimeException("not found");});


        sm.stop();
        sm.getStateMachineAccessor().
                doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptor);
                    //we configure the machine so that before any change in the states it writes the state to DB
                    sma.resetStateMachine(new DefaultStateMachineContext(beerOrder.getOrderStatus(),null,null,null));

                });

        sm.start();
        return sm;
    }
}
