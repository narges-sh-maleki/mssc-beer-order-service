package guru.sfg.brewery.common.events;

import guru.sfg.brewery.common.BeerOrderDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocateBeerOrderRequest {
    private BeerOrderDto beerOrderDto;
}
