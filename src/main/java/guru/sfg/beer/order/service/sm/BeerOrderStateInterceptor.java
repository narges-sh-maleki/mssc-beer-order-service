package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class BeerOrderStateInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEvents> {
    private  final BeerOrderRepository beerOrderRepository;



    @Override
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEvents> state,
                               Message<BeerOrderEvents> message,
                               Transition<BeerOrderStatusEnum, BeerOrderEvents> transition,
                               StateMachine<BeerOrderStatusEnum, BeerOrderEvents> stateMachine) {
/*

        Optional.ofNullable(message).
                ifPresent(msg->{
                    Optional.ofNullable(msg.getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER,""))
                    .ifPresent(beerId->{
                       BeerOrder beerOrder = beerOrderRepository.findById(UUID.fromString(beerId.toString())).get();
                       beerOrder.setOrderStatus(state.getId());
                       beerOrderRepository.save(beerOrder);
                    });
        });
*/



        log.debug("Pre-State Change");
//        Optional.ofNullable(message).map(msg->Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER, " ")))
//                .ifPresent(orderId -> {
//                    log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());
//
//                    BeerOrder beerOrder = beerOrderRepository.getOne(UUID.fromString(orderId));
//                    beerOrder.setOrderStatus(state.getId());
//                    beerOrderRepository.saveAndFlush(beerOrder);
//                });
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER, " ")))
                .ifPresent(orderId -> {
                    log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());

                    BeerOrder beerOrder = beerOrderRepository.getOne(UUID.fromString(orderId));
                    beerOrder.setOrderStatus(state.getId());
                    beerOrderRepository.saveAndFlush(beerOrder);
                });
    }
}
