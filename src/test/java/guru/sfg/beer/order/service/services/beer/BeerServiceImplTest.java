package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.bootstrap.BeerOrderBootStrap;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;


    @Disabled
    @Test
    void getBeerByUpc() {
       Optional<BeerDto> beerDto = beerService.getBeerByUpc(BeerOrderBootStrap.BEER_1_UPC);
       System.out.println(beerDto.get());
    }
}