package com.avispa.microf.model.invoice.position;

import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.util.FormatUtils;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper {

    @BeforeMapping
    default void decimalSeparator(PositionDto positionDto) {
        positionDto.setAmount(positionDto.getAmount().replace(".", ","));
        positionDto.setDiscount(positionDto.getDiscount().replace(".", ","));
    }

    @Mapping(source = "amount", target = "amount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "unitPrice", target = "unitPrice", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "discount", target = "discount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    PositionDto convertToDto(Position position);

    @Mapping(source = "amount", target = "amount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "unitPrice", target = "unitPrice", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "discount", target = "discount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    Position convertToEntity(PositionDto dto);
}
