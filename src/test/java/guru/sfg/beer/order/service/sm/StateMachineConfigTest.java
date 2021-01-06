package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.UUID;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    private  StateMachineFactory stateMachineFactory;


    @Autowired
    BeerOrderRepository beerOrderRepository;

    //@MockBean
    //Action<BeerOrderStatusEnum, BeerOrderEvents> beerOrderValidateAction;

    @Autowired
    StateMachineConfig stateMachineConfig;


    @Autowired
     BeerOrderStateInterceptor interceptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void configure() {
        //given

        BeerOrder beerOrder = BeerOrder.builder().build();
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        BeerOrder saved =  beerOrderRepository.saveAndFlush(beerOrder);


        StateMachine<BeerOrderStatusEnum, BeerOrderEvents> sm = stateMachineFactory.getStateMachine(UUID.randomUUID());
        sm.getStateMachineAccessor().
                doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptor);
                    //we configure the machine so that before any change in the states it writes the state to DB
                    sma.resetStateMachine(new DefaultStateMachineContext(saved.getOrderStatus(),null,null,null));

                });

        sm.start();
        System.out.println("*****" + sm.getState().toString());
        //doNothing().when(beerOrderValidateAction).execute(any());

        //when
        Message msg = MessageBuilder
                .withPayload(BeerOrderEvents.VALIDATE)
                .setHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER,UUID.randomUUID())
                .build();
        sm.sendEvent(msg);

        //then
        System.out.println("*****" + sm.getState().toString());
        System.out.println("-----" +saved.getOrderStatus());

    }
}