package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.dto.DtoRepository;
import com.avispa.microf.model.base.dto.IDto;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import com.avispa.microf.util.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/modal")
@RequiredArgsConstructor
@Slf4j
public class ModalController implements IModalController {

    private final ModalService modalService;
    private final TypeService typeService;
    private final DtoRepository dtoRepository;
    private final EcmObjectRepository<EcmObject> ecmObjectRepository;
    private final List<IEntityDtoMapper<? extends EcmObject, ? extends IDto>> mappers;

    @Override
    public ModelAndView getAddModal(String typeIdentifier) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id(typeIdentifier + "-add-modal")
                .title("Add new " + typeName)
                .action(typeIdentifier + "/add")
                .size("large")
                .build();

        return getModal(modal, typeName);
    }

    @Override
    public ModelAndView getUpdateModal(String typeIdentifier, UUID id) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        EcmObject entity = ecmObjectRepository.findById(id).orElseThrow();
        Class<? extends EcmObject> entityClass = typeService.getType(typeName).getEntityClass();
        if(!entity.getClass().isAssignableFrom(entityClass)) {
            throw new IllegalStateException(String.format("Not an object of '%s' type", typeName));
        }

        IDto dto = convertToDto(entity);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id(typeIdentifier + "-update-modal")
                .title("Update " + typeName)
                .action(typeIdentifier + "/update/" + id)
                .size("large")
                .build();

        return getModal(modal, entity, dto);
    }

    @SuppressWarnings("unchecked")
    private IDto convertToDto(EcmObject entity) {
        IEntityDtoMapper<EcmObject,IDto> foundMapper =
                (IEntityDtoMapper<EcmObject, IDto>) mappers.stream().filter(m -> {
            Class<?> foundEntityClass = getMapperEntity(m.getClass());
            return entity.getClass().equals(foundEntityClass);
        }).findFirst().orElseThrow();

        return foundMapper.convertToDto(entity);
    }

    private Class<?> getMapperEntity(Class<?> mapperClass) {
        Map<TypeVariable<?>, Type> typeArgs  = TypeUtils.getTypeArguments(mapperClass,  IEntityDtoMapper.class);
        TypeVariable<?> argTypeParam =  IEntityDtoMapper.class.getTypeParameters()[0];
        Type argType = typeArgs.get(argTypeParam);
        return TypeUtils.getRawType(argType, null);
    }

    private ModelAndView getModal(ModalConfiguration modal, EcmObject entity, IDto dto) {
        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(entity, dto, modal));
    }

    @Override
    public ModelAndView getCloneModal(String typeIdentifier) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.CLONE)
                .id(typeIdentifier + "-clone-modal")
                .title("Clone existing " + typeName)
                .action(typeIdentifier + "/add")
                .size("extra-large")
                .build();

        return getModal(modal, typeName);
    }

    protected ModelAndView getModal(ModalConfiguration modal, String typeName) {
        // TODO: include discriminator? add tests
        Dto dto = dtoRepository.findByTypeNameAndDiscriminatorIsNull(typeName).orElseThrow();

        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(dto.getType().getEntityClass(), dto.getDtoClass(), modal));
    }

    /*@Override
    public ModelAndView loadPage(int pageNumber, C context) {
        return new ModelAndView("fragments/modal :: modal-body",
                modalService.loadPage(pageNumber, context, getService()));
    }*/

    @Override
    public ModelAndView loadTableTemplate(String typeIdentifier, String tableName) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        // TODO: include discriminator? add tests
        Dto dto = dtoRepository.findByTypeNameAndDiscriminatorIsNull(typeName).orElseThrow();
        Class<? extends IDto> dtoClass = dto.getDtoClass();

        return new ModelAndView("fragments/property-page :: table-template-row",
                modalService.getTemplateRow(tableName, dto.getType().getEntityClass(), dtoClass));
    }
}
