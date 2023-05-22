package com.avispa.microf.model.invoice;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.payment.PaymentMapper;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.position.PositionMapper;
import com.avispa.ecm.model.base.mapper.IEntityDtoMapper;
import com.google.common.collect.MoreCollectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {PositionMapper.class, PaymentMapper.class})
public abstract class InvoiceMapper implements IEntityDtoMapper<Invoice, InvoiceDto> {
    // not required when componentModel = "spring", can be autowired
    //InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Autowired
    protected PositionMapper positionMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Mapping(target = "pdfRenditionAvailable", expression = "java(entity.isPdfRenditionAvailable())")
    public abstract InvoiceDto convertToDto(Invoice entity);

    protected String customerToId(Customer customer) {
        return customer.getId().toString();
    }

    protected Customer idToCustomer(String customerId) {
        return customerRepository.getReferenceById(UUID.fromString(customerId));
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
}