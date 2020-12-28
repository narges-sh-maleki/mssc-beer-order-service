package guru.sfg.brewery.common;

import org.junit.jupiter.api.Test;

import java.util.UUID;


class BeerOrderDtoTest {


    BeerOrderDto beerOrderDto;

    @Test
    void name() {
        beerOrderDto = BeerOrderDto.builder().id(UUID.randomUUID()).build();

        System.out.println(beerOrderDto.toString());
    }
}