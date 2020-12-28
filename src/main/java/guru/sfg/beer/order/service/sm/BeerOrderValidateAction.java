package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.common.ValidateBeerOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BeerOrderValidateAction implements Action<BeerOrderStatusEnum, BeerOrderEvents> {
    private final BeerOrderRepository beerOrderRepository;
    private final JmsTemplate jmsTemplate;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEvents> context) {
        String beerOrderId =  context.getMessage().getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER,"").toString();
        BeerOrder beerOrder = beerOrderRepository.findById(UUID.fromString(beerOrderId)).orElseThrow(()->  new RuntimeException(""));
        ValidateBeerOrderRequest validateBeerOrderRequest = ValidateBeerOrderRequest.builder()
                .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder)).build();
        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER,validateBeerOrderRequest);
    }
}
