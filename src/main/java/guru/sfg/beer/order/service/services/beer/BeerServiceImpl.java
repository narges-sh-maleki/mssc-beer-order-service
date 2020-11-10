package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import guru.sfg.beer.order.service.web.controllers.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service

public class BeerServiceImpl implements BeerService {

    @Value("${sfg.beerServiceHost}")
    private final String beerServiceHost;

    private final static String GET_BY_UPC_PATH = "/api/v1/beer/beerUpc/{upc}";

    private final RestTemplate restTemplate;

    public BeerServiceImpl(@Value("${sfg.beerServiceHost}") String beerServiceHost, RestTemplateBuilder restTemplateBuilder) {
        this.beerServiceHost = beerServiceHost;
        this.restTemplate = restTemplateBuilder.build();
    }


    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {

        String url = beerServiceHost + GET_BY_UPC_PATH;
       return   Optional.of(restTemplate.getForObject(url,BeerDto.class,upc));


    }
}
