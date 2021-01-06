package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BeerOrderManagerImplTest {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Mock
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Test
    @Disabled
    void testStateMachine() {

        BeerOrder beerOrder = BeerOrder.builder()
                .id(UUID.randomUUID())
                .build();
//        Mockito.when(beerOrderRepository.save(beerOrder)).thenReturn(beerOrder);
        beerOrderManager.newBeerOrder(beerOrder);
        assertEquals(BeerOrderStatusEnum.NEW, beerOrder.getOrderStatus());
    }
}