package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.BeerOrderEvents;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component("orderValidationFailedAction")
@Slf4j
public class BeerOrderValidationFailedAction implements Action<BeerOrderStatusEnum, BeerOrderEvents> {

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEvents> context) {
        String orderId = context.getMessageHeader(BeerOrderManagerImpl.BEER_ORDER_ID_HEADER).toString();
        log.debug("Compensating Trx for:" + orderId);
    }
}
