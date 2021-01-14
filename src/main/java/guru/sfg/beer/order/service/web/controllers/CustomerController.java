package guru.sfg.beer.order.service.web.controllers;

import guru.sfg.beer.order.service.services.CustomerService;
import guru.sfg.brewery.common.CustomerDto;
import guru.sfg.brewery.common.CustomerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "25";

    private final CustomerService customerService;


    @GetMapping
    public CustomerPagedList listCustomers(@RequestParam(value = "pageNumber",required = false,defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                           @RequestParam(value = "pageSize",required = false,defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize ){


        return customerService.listCustomers(PageRequest.of(pageNumber,pageSize));
    }

    @GetMapping("{customerId}")
    public CustomerDto getCustomer(@PathVariable("customerId") UUID customerId){
        return  customerService.getCustomer( customerId);

    }

}
