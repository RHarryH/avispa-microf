package com.avispa.microf.model.invoice.position;

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
    default void decimalTransform(PositionDto positionDto) {
        positionDto.setUnitPrice(FormatUtils.transformDecimal(positionDto.getUnitPrice()));
        positionDto.setQuantity(FormatUtils.transformDecimal(positionDto.getQuantity()));
        positionDto.setDiscount(FormatUtils.transformDecimal(positionDto.getDiscount()));
    }

    @Mapping(source = "quantity", target = "quantity", numberFormat = FormatUtils.QUANTITY_DECIMAL_FORMAT)
    @Mapping(source = "unitPrice", target = "unitPrice", numberFormat = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Mapping(source = "discount", target = "discount", numberFormat = FormatUtils.PERCENT_DECIMAL_FORMAT)
    PositionDto convertToDto(Position position);

    @Mapping(source = "quantity", target = "quantity", numberFormat = FormatUtils.QUANTITY_DECIMAL_FORMAT)
    @Mapping(source = "unitPrice", target = "unitPrice", numberFormat = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Mapping(source = "discount", target = "discount", numberFormat = FormatUtils.PERCENT_DECIMAL_FORMAT)
    Position convertToEntity(PositionDto dto);
}
