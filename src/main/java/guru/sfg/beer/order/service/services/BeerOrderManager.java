package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager  {
    BeerOrder newBeerOrder(BeerOrder beerOrder);
    void processValidationResult(BeerOrder beerOrder,Boolean validationResult);
    void processAllocationResult(BeerOrder beerOrder, Boolean allocationError, Boolean pendingInventory);
    void pickupOrder(BeerOrder beerOrder);
    void cancelOrder(BeerOrder beerOrder);
}
