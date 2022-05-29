package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.type.TypeNameUtils;
import com.avispa.ecm.model.type.TypeRepository;
import com.avispa.microf.model.bankaccount.BankAccountDto;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import com.avispa.microf.model.customer.CommonCustomerDto;
import com.avispa.microf.model.invoice.InvoiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Locale;
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
    private final TypeRepository typeRepository;
    private final EcmObjectRepository<EcmObject> ecmObjectRepository;
    private final List<IEntityDtoMapper<? extends EcmObject, ? extends Dto>> mappers;

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
        Class<? extends EcmObject> clazz = typeRepository.findByTypeName(typeName).getClazz();
        if(!entity.getClass().isAssignableFrom(clazz)) {
            log.error("Not an object of '{}' type", typeName);
            return null;
        }

        IEntityDtoMapper<EcmObject, Dto> usedMapper = null;
        for(IEntityDtoMapper<? extends EcmObject, ? extends Dto> mapper : mappers) {
            Class<?> entityClass = getMapperEntity(mapper.getClass());
            if(entityClass.equals(clazz)) {
                usedMapper = (IEntityDtoMapper<EcmObject, Dto>) mapper;
                break;
            }
        }

        if(usedMapper == null) {
            throw new IllegalArgumentException();
        }

        Dto dto = usedMapper.convertToDto(entity);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id(typeName + "-update-modal")
                .title("Update " + typeIdentifier)
                .action(typeName + "/update/" + id)
                .size("large")
                .build();

        return getModal(entity, dto, modal);
    }

    private ModelAndView getModal(EcmObject entity, Dto dto, ModalConfiguration modal) {
        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(entity, dto, modal));
    }

    private Class<?> getMapperEntity(Class<?> mapperClass) {
        Map<TypeVariable<?>, Type> typeArgs  = TypeUtils.getTypeArguments(mapperClass, IEntityDtoMapper.class);
        TypeVariable<?> argTypeParam = IEntityDtoMapper.class.getTypeParameters()[0];
        Type argType = typeArgs.get(argTypeParam);
        return TypeUtils.getRawType(argType, null);
    }

    @Override
    public ModelAndView getCloneModal(String typeIdentifier) {
        String typeNameLowerCase = typeIdentifier.toLowerCase(Locale.ROOT);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.CLONE)
                .id(typeNameLowerCase + "-clone-modal")
                .title("Clone existing " + typeNameLowerCase)
                .action(typeNameLowerCase + "/add")
                .size("extra-large")
                .build();

        return getModal(modal, typeNameLowerCase);
    }

    protected ModelAndView getModal(ModalConfiguration modal, String typeName) {
        Class<? extends EcmObject> clazz = typeRepository.findByTypeName(typeName).getClazz();

        Class<? extends Dto> dtoClass = null;
        // TODO: read from mapper? using map<string, string> ?
        if(clazz.getSimpleName().equalsIgnoreCase("customer")) {
            dtoClass = CommonCustomerDto.class;
        } else if(clazz.getSimpleName().equalsIgnoreCase("invoice")) {
            dtoClass = InvoiceDto.class;
        } else if(clazz.getSimpleName().equalsIgnoreCase("bankaccount")) {
            dtoClass = BankAccountDto.class;
        }

        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(clazz, dtoClass, modal));
    }

    /*@Override
    public ModelAndView loadPage(int pageNumber, C context) {
        return new ModelAndView("fragments/modal :: modal-body",
                modalService.loadPage(pageNumber, context, getService()));
    }

    private final Map<String, Class<? extends Dto>> tableFieldsMap = new HashMap<>();

    @Override
    public ModelAndView loadTableTemplate(String tableName) {
        return new ModelAndView("fragments/property-page :: table-template-row",
                modalService.getTemplateRow(tableName, getObjectClass(), getDtoClass(), tableFieldsMap.get(tableName)));
    }

    private Class<T> getObjectClass() {
        return (Class<T>)getTypeFromGeneric(0);
    }

    @SuppressWarnings("unchecked")
    private Class<D> getDtoClass() {
        return (Class<D>)getTypeFromGeneric(1);
    }

    private Type getTypeFromGeneric(int pos) {
        return ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[pos];
    }

    //@Override
    // INVOICE
    protected void registerTableFields(Map<String, Class<? extends Dto>> tableFieldsMap) {
        tableFieldsMap.put("positions", PositionDto.class);
    }*/
}
