package guru.sfg.beer.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import guru.sfg.beer.order.service.services.beer.BeerServiceImpl;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.UUID;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestConfiguration
public class TestConfig {
    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer server = with(wireMockConfig().port(8083));
        server.start();
        return server;
    }

    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    ObjectMapper objectMapper;

    @EventListener(classes = {ContextRefreshedEvent.class})
    public void handleMultipleEvents() throws JsonProcessingException {
        BeerDto beerDto = BeerDto.builder().id(UUID.randomUUID()).upc("123456").build();
        wireMockServer.stubFor(get(urlMatching(BeerServiceImpl.GET_BY_UPC_PATH_1 + ".*"))
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));
    }

}
