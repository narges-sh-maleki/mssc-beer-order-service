package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.sm.BeerOrderStateInterceptor;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final StateMachineFactory stateMachineFactory;
    public static final String BEER_ORDER_ID_HEADER = "BEER_ORDER_ID_HEADER";
    BeerOrderStateInterceptor interceptor;
    BeerOrderMapper beerOrderMapper;

    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        buildStateMachine(beerOrder);
        BeerOrder saved = beerOrderRepository.save(beerOrder);
        sendEvent(saved, BeerOrderEvents.VALIDATE);
        return saved;
    }

    @Override
    public void processValidationResult(BeerOrder beerOrder, Boolean validationResult) {
        if (validationResult) {
            sendEvent(beerOrder, BeerOrderEvents.VALIDATION_PASSED);
            //get the object with the latest status from the DB because the interceptor has updated the status
            BeerOrder updatedBeerOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
            sendEvent(updatedBeerOrder,BeerOrderEvents.ALLOCATE);
        }
        else
            sendEvent(beerOrder,BeerOrderEvents.VALIDATION_EXCEPTION);
    }


    private void sendEvent(BeerOrder beerOrder, BeerOrderEvents event){
        StateMachine<BeerOrderStatusEnum,BeerOrderEvents> sm = buildStateMachine(beerOrder);

        //sm.sendEvent(event);
        sm.sendEvent(MessageBuilder
                .withPayload(event)
                .setHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER,beerOrder.getId())
                .build());
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEvents> buildStateMachine(BeerOrder beerOrder){

        StateMachine<BeerOrderStatusEnum,BeerOrderEvents> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
        sm.stop();
        sm.getStateMachineAccessor().
                doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext(beerOrder.getOrderStatus(),null,null,null));

                });

        sm.start();
        return sm;
    }
}
