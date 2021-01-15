package guru.sfg.beer.order.service.sm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import guru.sfg.beer.order.service.services.TestConfig;
import guru.sfg.beer.order.service.services.beer.BeerServiceImpl;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith({WireMockExtension.class})
@Import(TestConfig.class)
//@Disabled
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory stateMachineFactory;


    @Autowired
    BeerOrderRepository beerOrderRepository;

    //@MockBean
    //Action<BeerOrderStatusEnum, BeerOrderEvents> beerOrderValidateAction;

    @Autowired
    StateMachineConfig stateMachineConfig;


    @Autowired
    BeerOrderStateInterceptor interceptor;

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("sm configure")
    @Disabled
    @Test
    void configure() throws JsonProcessingException {
        //given
        BeerDto beerDto = BeerDto.builder().id(UUID.randomUUID()).upc("123456").build();
        wireMockServer.stubFor(get(BeerServiceImpl.GET_BY_UPC_PATH_1 + "123456").willReturn(okJson(objectMapper.writeValueAsString(beerDto))));

        BeerOrder beerOrder = BeerOrder.builder().build();
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        BeerOrder saved = beerOrderRepository.saveAndFlush(beerOrder);


        StateMachine<BeerOrderStatusEnum, BeerOrderEvents> sm = stateMachineFactory.getStateMachine(UUID.randomUUID());
        sm.getStateMachineAccessor().
                doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptor);
                    //we configure the machine so that before any change in the states it writes the state to DB
                    sma.resetStateMachine(new DefaultStateMachineContext(saved.getOrderStatus(), null, null, null));

                });

        sm.start();
        System.out.println("*****" + sm.getState().toString());
        //doNothing().when(beerOrderValidateAction).execute(any());

        //when
        Message msg = MessageBuilder
                .withPayload(BeerOrderEvents.VALIDATE)
                .setHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER, UUID.randomUUID())
                .build();
        sm.sendEvent(msg);

        //then
        System.out.println("*****" + sm.getState().toString());
        System.out.println("-----" + saved.getOrderStatus());

    }
}