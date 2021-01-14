package guru.sfg.beer.order.service.services;

import guru.sfg.brewery.common.CustomerDto;
import guru.sfg.brewery.common.CustomerPagedList;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CustomerService {

    CustomerPagedList listCustomers(Pageable pageable);

    CustomerDto getCustomer(UUID customerId);
}
