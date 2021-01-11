package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.common.BeerOrderDto;
import guru.sfg.brewery.common.BeerOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@ActiveProfiles("tst")
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BeerOrderServiceImplTest {

    @InjectMocks
    BeerOrderServiceImpl beerOrderService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    BeerOrderMapper beerOrderMapper;// =  Mappers.getMapper( BeerOrderMapper.class );
    @Mock
    BeerOrderManager beerOrderManager;

    Customer testCustomer;
    UUID customerId = UUID.randomUUID();


    @BeforeEach
    void setUp() {

    }

    @Test
    void placeOrderHappy() {
        //given
        testCustomer = Customer.builder().id(customerId).customerName("Test Customer").build();
        UUID beerId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

       List<BeerOrderLineDto> dtoLines = new ArrayList<>();
        dtoLines.add(BeerOrderLineDto.builder()
                .id(orderId)
                .beerId(beerId)
                .build());

        BeerOrderDto beerOrderDto = BeerOrderDto.builder()
                .id(orderId)
                .beerOrderLines(dtoLines)
                .customerId(customerId)
                .orderStatus(guru.sfg.brewery.common.BeerOrderStatusEnum.ALLOCATED)
                .build();

        Set<BeerOrderLine> lines = new HashSet<>();
        lines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .quantityAllocated(1)
                .orderQuantity(1)
                .build());

        BeerOrder beerOrder = BeerOrder.builder()
                .id(orderId)
                .beerOrderLines(lines)
                .orderStatus(BeerOrderStatusEnum.ALLOCATED)
                .build();

        BeerOrder beerOrder1 = BeerOrder.builder()
                .id(orderId)
                .beerOrderLines(lines)
                .orderStatus(BeerOrderStatusEnum.ALLOCATED)
                .build();

        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(testCustomer));
        when(beerOrderMapper.dtoToBeerOrder(any(BeerOrderDto.class))).thenReturn(beerOrder);
        when(beerOrderMapper.beerOrderToDto(any(BeerOrder.class))).thenReturn(beerOrderDto);
        when(beerOrderManager.newBeerOrder(any(BeerOrder.class))).thenReturn(beerOrder1);



        //when
        BeerOrderDto result = beerOrderService.placeOrder(customerId,beerOrderDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(guru.sfg.brewery.common.BeerOrderStatusEnum.ALLOCATED);
    }

    @Test
    void pickupOrder() {
    }
}