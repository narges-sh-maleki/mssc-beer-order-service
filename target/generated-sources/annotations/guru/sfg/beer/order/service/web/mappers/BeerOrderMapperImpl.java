package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrder.BeerOrderBuilder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.brewery.common.BeerOrderDto;
import guru.sfg.brewery.common.BeerOrderDto.BeerOrderDtoBuilder;
import guru.sfg.brewery.common.BeerOrderLineDto;
import guru.sfg.brewery.common.BeerOrderStatusEnum;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-07T11:50:41+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 14.0.1 (Oracle Corporation)"
)
@Component
public class BeerOrderMapperImpl implements BeerOrderMapper {

    @Autowired
    private DateMapper dateMapper;
    @Autowired
    private BeerOrderLineMapper beerOrderLineMapper;

    @Override
    public BeerOrderDto beerOrderToDto(BeerOrder beerOrder) {
        if ( beerOrder == null ) {
            return null;
        }

        BeerOrderDtoBuilder beerOrderDto = BeerOrderDto.builder();

        beerOrderDto.customerId( beerOrderCustomerId( beerOrder ) );
        beerOrderDto.id( beerOrder.getId() );
        if ( beerOrder.getVersion() != null ) {
            beerOrderDto.version( beerOrder.getVersion().intValue() );
        }
        beerOrderDto.createdDate( dateMapper.asOffsetDateTime( beerOrder.getCreatedDate() ) );
        beerOrderDto.lastModifiedDate( dateMapper.asOffsetDateTime( beerOrder.getLastModifiedDate() ) );
        beerOrderDto.beerOrderLines( beerOrderLineSetToBeerOrderLineDtoList( beerOrder.getBeerOrderLines() ) );
        beerOrderDto.orderStatus( beerOrderStatusEnumToBeerOrderStatusEnum( beerOrder.getOrderStatus() ) );
        beerOrderDto.orderStatusCallbackUrl( beerOrder.getOrderStatusCallbackUrl() );
        beerOrderDto.customerRef( beerOrder.getCustomerRef() );

        return beerOrderDto.build();
    }

    @Override
    public BeerOrder dtoToBeerOrder(BeerOrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        BeerOrderBuilder beerOrder = BeerOrder.builder();

        beerOrder.id( dto.getId() );
        if ( dto.getVersion() != null ) {
            beerOrder.version( dto.getVersion().longValue() );
        }
        beerOrder.createdDate( dateMapper.asTimestamp( dto.getCreatedDate() ) );
        beerOrder.lastModifiedDate( dateMapper.asTimestamp( dto.getLastModifiedDate() ) );
        beerOrder.customerRef( dto.getCustomerRef() );
        beerOrder.beerOrderLines( beerOrderLineDtoListToBeerOrderLineSet( dto.getBeerOrderLines() ) );
        beerOrder.orderStatus( beerOrderStatusEnumToBeerOrderStatusEnum1( dto.getOrderStatus() ) );
        beerOrder.orderStatusCallbackUrl( dto.getOrderStatusCallbackUrl() );

        return beerOrder.build();
    }

    private UUID beerOrderCustomerId(BeerOrder beerOrder) {
        if ( beerOrder == null ) {
            return null;
        }
        Customer customer = beerOrder.getCustomer();
        if ( customer == null ) {
            return null;
        }
        UUID id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<BeerOrderLineDto> beerOrderLineSetToBeerOrderLineDtoList(Set<BeerOrderLine> set) {
        if ( set == null ) {
            return null;
        }

        List<BeerOrderLineDto> list = new ArrayList<BeerOrderLineDto>( set.size() );
        for ( BeerOrderLine beerOrderLine : set ) {
            list.add( beerOrderLineMapper.beerOrderLineToDto( beerOrderLine ) );
        }

        return list;
    }

    protected BeerOrderStatusEnum beerOrderStatusEnumToBeerOrderStatusEnum(guru.sfg.beer.order.service.domain.BeerOrderStatusEnum beerOrderStatusEnum) {
        if ( beerOrderStatusEnum == null ) {
            return null;
        }

        BeerOrderStatusEnum beerOrderStatusEnum1;

        switch ( beerOrderStatusEnum ) {
            case NEW: beerOrderStatusEnum1 = BeerOrderStatusEnum.NEW;
            break;
            case VALIDATION_PENDING: beerOrderStatusEnum1 = BeerOrderStatusEnum.VALIDATION_PENDING;
            break;
            case VALIDATED: beerOrderStatusEnum1 = BeerOrderStatusEnum.VALIDATED;
            break;
            case VALIDATION_EXCEPTION: beerOrderStatusEnum1 = BeerOrderStatusEnum.VALIDATION_EXCEPTION;
            break;
            case ALLOCATION_PENDING: beerOrderStatusEnum1 = BeerOrderStatusEnum.ALLOCATION_PENDING;
            break;
            case ALLOCATED: beerOrderStatusEnum1 = BeerOrderStatusEnum.ALLOCATED;
            break;
            case ALLOCATION_EXCEPTION: beerOrderStatusEnum1 = BeerOrderStatusEnum.ALLOCATION_EXCEPTION;
            break;
            case PENDING_INVENTORY: beerOrderStatusEnum1 = BeerOrderStatusEnum.PENDING_INVENTORY;
            break;
            case PICKED_UP: beerOrderStatusEnum1 = BeerOrderStatusEnum.PICKED_UP;
            break;
            case DELIVERED: beerOrderStatusEnum1 = BeerOrderStatusEnum.DELIVERED;
            break;
            case CANCELLED: beerOrderStatusEnum1 = BeerOrderStatusEnum.CANCELLED;
            break;
            case DELIVERY_EXCEPTION: beerOrderStatusEnum1 = BeerOrderStatusEnum.DELIVERY_EXCEPTION;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + beerOrderStatusEnum );
        }

        return beerOrderStatusEnum1;
    }

    protected Set<BeerOrderLine> beerOrderLineDtoListToBeerOrderLineSet(List<BeerOrderLineDto> list) {
        if ( list == null ) {
            return null;
        }

        Set<BeerOrderLine> set = new HashSet<BeerOrderLine>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( BeerOrderLineDto beerOrderLineDto : list ) {
            set.add( beerOrderLineMapper.dtoToBeerOrderLine( beerOrderLineDto ) );
        }

        return set;
    }

    protected guru.sfg.beer.order.service.domain.BeerOrderStatusEnum beerOrderStatusEnumToBeerOrderStatusEnum1(BeerOrderStatusEnum beerOrderStatusEnum) {
        if ( beerOrderStatusEnum == null ) {
            return null;
        }

        guru.sfg.beer.order.service.domain.BeerOrderStatusEnum beerOrderStatusEnum1;

        switch ( beerOrderStatusEnum ) {
            case NEW: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.NEW;
            break;
            case VALIDATION_PENDING: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.VALIDATION_PENDING;
            break;
            case VALIDATED: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.VALIDATED;
            break;
            case VALIDATION_EXCEPTION: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.VALIDATION_EXCEPTION;
            break;
            case ALLOCATION_PENDING: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.ALLOCATION_PENDING;
            break;
            case ALLOCATED: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.ALLOCATED;
            break;
            case ALLOCATION_EXCEPTION: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.ALLOCATION_EXCEPTION;
            break;
            case PENDING_INVENTORY: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.PENDING_INVENTORY;
            break;
            case PICKED_UP: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.PICKED_UP;
            break;
            case DELIVERED: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.DELIVERED;
            break;
            case CANCELLED: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.CANCELLED;
            break;
            case DELIVERY_EXCEPTION: beerOrderStatusEnum1 = guru.sfg.beer.order.service.domain.BeerOrderStatusEnum.DELIVERY_EXCEPTION;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + beerOrderStatusEnum );
        }

        return beerOrderStatusEnum1;
    }
}
