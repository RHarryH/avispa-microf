package com.avispa.microf.model.invoice;

import com.avispa.microf.model.base.mapper.IExtendedEntityDtoMapper;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.payment.PaymentMapper;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.position.PositionMapper;
import com.google.common.collect.MoreCollectors;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {PositionMapper.class, PaymentMapper.class})
public abstract class InvoiceMapper implements IExtendedEntityDtoMapper<Invoice, InvoiceDto, InvoiceDto> {
    // not required when componentModel = "spring", can be autowired
    //InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Autowired
    protected PositionMapper positionMapper;

    @Autowired
    private CustomerRepository customerRepository;

    protected String customerToId(Customer customer) {
        return customer.getId().toString();
    }

    protected Customer idToCustomer(String customerId) {
        return Hibernate.unproxy(customerRepository.getById(UUID.fromString(customerId)), Customer.class);
    }

    protected void updatePositionsFromPositionsDto(List<PositionDto> positionDtos, @MappingTarget List<Position> positions) {
        if ( positionDtos == null ) {
            return;
        }

        for (int i = 0; i < positionDtos.size(); i++) {
            PositionDto positionDto = positionDtos.get(i);
            Optional<Position> position = positions.stream()
                    .filter(p -> p.getId().equals(positionDto.getId()))
                    .collect(MoreCollectors.toOptional());

            if(position.isPresent()) {
                positionMapper.updateEntityFromDto(positionDto, position.get());
            } else {
                positions.add(i, positionMapper.convertToEntity(positionDto));
            }
        }
    }

    @Mapping(target = "hasPdfRendition", expression = "java(entity.hasPdfRendition())")
    public abstract InvoiceDto convertToCommonDto(Invoice entity);
}