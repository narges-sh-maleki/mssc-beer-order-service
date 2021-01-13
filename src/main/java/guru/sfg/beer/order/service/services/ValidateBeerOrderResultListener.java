package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.brewery.common.events.ValidateBeerOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateBeerOrderResultListener {
    private  final BeerOrderRepository beerOrderRepository;
    private final BeerOrderManager beerOrderManager;


    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT)
    public void listenToValidateOrderResult(ValidateBeerOrderResult result) {
        log.debug("################ Validate Result Listener");
        BeerOrder beerOrder = beerOrderRepository.findById(result.getOrderId()).orElseThrow();
        beerOrderManager.processValidationResult(beerOrder,result.isValidated());


    }


   // docker run --name mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:latest -p 3306:3306
}
