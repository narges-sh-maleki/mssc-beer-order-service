package guru.sfg.brewery.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderRequest {
    private  BeerOrderDto beerOrderDto;
}
