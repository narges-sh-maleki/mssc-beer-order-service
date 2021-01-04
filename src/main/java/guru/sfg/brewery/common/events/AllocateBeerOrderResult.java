package guru.sfg.brewery.common.events;

import guru.sfg.brewery.common.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateBeerOrderResult {
    private Boolean allocationError = false;
    private Boolean pendingInventory = false;
    private BeerOrderDto beerOrderDto;

}

