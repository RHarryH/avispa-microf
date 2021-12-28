package com.avispa.microf.model.invoice.position;

import com.avispa.microf.util.FormatUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper {
    @Mapping(source = "amount", target = "amount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "unitPrice", target = "unitPrice", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "discount", target = "discount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    PositionDto convertToDto(Position position);

    @Mapping(source = "amount", target = "amount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "unitPrice", target = "unitPrice", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    @Mapping(source = "discount", target = "discount", numberFormat = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    Position convertToEntity(PositionDto dto);
}
