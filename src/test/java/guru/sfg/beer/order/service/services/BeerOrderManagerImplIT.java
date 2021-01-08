package guru.sfg.beer.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.services.beer.BeerServiceImpl;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@SpringBootTest()
@ExtendWith(WireMockExtension.class)

public class BeerOrderManagerImplIT {
    @Autowired
    BeerOrderManagerImpl beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    UUID beerId =UUID.randomUUID();
    UUID orderId =UUID.randomUUID();

    Customer testCustomer;

    BeerOrder beerOrder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WireMockServer wireMockServer;


    @TestConfiguration
    public static class testConfig{
        @Bean
         public  WireMockServer wireMockServer(){
            WireMockServer server = with(wireMockConfig().port(8083));
            server.start();
            return server;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        testCustomer = Customer.builder().customerName("Test Customer").build();
        customerRepository.save(testCustomer);

    }

    private BeerOrder createBeerOrder(){
        beerOrder = BeerOrder.builder().id(orderId)
                .build();
        Set<BeerOrderLine> beerOrderLines = new HashSet<>();
        beerOrderLines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc("123456")
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());

        beerOrder.setBeerOrderLines(beerOrderLines);

        return beerOrder;
    }


    @Test
    void testNewToValidationPending() throws JsonProcessingException {
        //given
        BeerOrder beerOrder = createBeerOrder();

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("123456").build();
        wireMockServer.stubFor(get(BeerServiceImpl.GET_BY_UPC_PATH_1+ "123456").willReturn(okJson(objectMapper.writeValueAsString(beerDto))));


        //when
        BeerOrder resultBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //then
        BeerOrder foundOrder = beerOrderRepository.findById(resultBeerOrder.getId()).orElseThrow();
        assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.VALIDATION_PENDING);


    }

    @Test
    void testNewToAllocated() throws JsonProcessingException, InterruptedException {
        //given
        BeerOrder beerOrder = createBeerOrder();

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("123456").build();
        wireMockServer.stubFor(get(BeerServiceImpl.GET_BY_UPC_PATH_1+ "123456").willReturn(okJson(objectMapper.writeValueAsString(beerDto))));


        //when
        BeerOrder resultBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //Thread.sleep(10000);
        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
            assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.ALLOCATED);
        });

        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
            foundOrder.getBeerOrderLines().forEach(line -> {
                assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
            });
        });

        //then
        BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
        assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.ALLOCATED);
        foundOrder.getBeerOrderLines().forEach(line -> {
            assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
        });

    }


    @Test
    void testNewToPickedup() throws JsonProcessingException, InterruptedException {
        //given
        BeerOrder beerOrder = createBeerOrder();

        BeerDto beerDto = BeerDto.builder().id(beerId).upc("123456").build();
        wireMockServer.stubFor(get(BeerServiceImpl.GET_BY_UPC_PATH_1+ "123456").willReturn(okJson(objectMapper.writeValueAsString(beerDto))));


        //when
        BeerOrder resultBeerOrder = beerOrderManager.newBeerOrder(beerOrder);



        //Thread.sleep(10000);
        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
            assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.ALLOCATED);
        });

        beerOrderManager.pickupOrder(resultBeerOrder);


        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
            assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.PICKED_UP);
        });


        //then
        BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElseThrow();
        assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.PICKED_UP);


    }


}
