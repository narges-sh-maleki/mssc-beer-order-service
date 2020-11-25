package guru.sfg.beer.order.service.web.mappers;


import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.services.beer.BeerService;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import guru.sfg.beer.order.service.web.controllers.NotFoundException;
import guru.sfg.brewery.common.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BeerOrderLineDecorator implements BeerOrderLineMapper {

    @Autowired
    private  BeerOrderLineMapper beerOrderLineMapper;
    @Autowired
    private  BeerService beerService;




    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        BeerOrderLineDto lineDto = beerOrderLineMapper.beerOrderLineToDto(line);
        Optional<BeerDto> beerDto = beerService.getBeerByUpc(line.getUpc());
        beerDto.ifPresentOrElse (dto->{
            lineDto.setBeerName(dto.getBeerName());
            lineDto.setBeerStyle(dto.getBeerStyle());
            lineDto.setPrice(dto.getPrice());
            lineDto.setBeerId(dto.getId());
        },
                ()->{throw new NotFoundException();
        });

        return lineDto;
    }

    @Override
    public BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto) {
        return beerOrderLineMapper.dtoToBeerOrderLine(dto);
    }
}
