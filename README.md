![Status](https://github.com/RHarryH/avispa-microf/actions/workflows/main.yml/badge.svg) ![Coverage](.github/badges/jacoco.svg)  [![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

# Avispa μF

An invoice generating software built on top of Avispa ECM. It works with Polish invoices only
however can be recustomized for own needs.

## Running

Two environments are available:

- `dev` - can be run locally using IDE with `-Dspring.profiles.active=dev` property set.
  It uses H2 database and `data-h2.sql` scripts present in `resources` of each project. The schema is created
  automatically.
- `prod` - configured to be run inside Docker container, it uses PostgreSQL database

### ECM Client properties

| Property name                                 | Description                                                                                                                                                                                                      |
|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `avispa.ecm.client.name`                      | ECM Client application name, this is the default name of the application set in `spring.application.name`                                                                                                        |
| `avispa.ecm.client.short-name`                | Shortened name of the ECM Client application                                                                                                                                                                     |
| `avispa.ecm.client.description`               | Brief description of the application presented in `meta` tag                                                                                                                                                     |
| `avispa.ecm.client.configuration.application` | Name of the application configuration, which will be picked by the ECM Client. If not specified `avispa.ecm.client.name`, `avispa.ecm.client.short-name` and `avispa.ecm.client.description` values will be used |
| `avispa.ecm.client.configuration.layout`      | Name of the layout configuration, which will be picked by the ECM Client                                                                                                                                         |
| `avispa.ecm.client.configuration.menu`        | Name of the default menu configuration, which will be picked by the ECM Client                                                                                                                                   |

In the final idea it should be the context responsibility to retrieve UI configurations. But because context operates
now only on objects, `configuration` properties were introduced. With contexts it would use groups definitions but it
requires
security implementation.

### ECM Client configuration

ECM Client provides an extension of ECM configuration zip file. It introduces following configurations:

- `ecm_application` for defining more detailed information about the application like short name and description. If not
  defined then `avispa.ecm.client.name`, `avispa.ecm.client.short-name` and `avispa.ecm.client.description` will be used
  as defaults printed in the UI (please note `version` bean will always use `spring.application.name`)
- `ecm_layout` for defining how widgets will be placed on the application screen
- `ecm_menu` for defining the menus visible on the navigation bar
- `ecm_list_widget` for defining what data type and which columns should be visible on the widget, this configuration is
  later used in `ecm_layout`

### ECM Client names hierarchy

Both ECM and ECM Client contains their own properties used to provide technical name of the
application (`avispa.ecm.name` and `avispa.ecm.client.name` respectively). They are
both used as `version` beans presenting version of each component on the UI, so they can be considered as _component
name_. On the other hand, they are also used in each component for the value of `spring.application.name` property,
which should be considered as _top component name_ (ECM Client or customization name if overrides the property).

Business name of the application, presented on the UI as description, brand name or website title is stored in the zip
configuration as `ecm_application` element. For the time being the name of applied configuration should be provided
in `avispa.ecm.client.configuration.application` property. If it is not defined
then `avispa.ecm.client.name`, `avispa.ecm.client.short-name` and `avispa.ecm.client.description` will be used to
construct default basic application configuration.

### μF properties file

The application will search for `config/microf.properties` file. Below table presents the μF specific properties, which
can appear in the properties file.

| Property name                     | Description                                                                                                                    |
|-----------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `microf.data.init-script`         | Used only using `dev` profile. Contains location to the custom init script loading initial customers lists, bank accounts etc. |
| `microf.issuer-name`              | Name of the issues printed on the generated invoice                                                                            |
| `microf.invoice.counter-strategy` | Strategy of invoices numbering. Possible values are: `monthCounterStrategy` and `continuousCounterStrategy`                    |

### Docker

To build μF Docker image you can use following command:

```shell
cd microf-custom
docker build -t avispa/microf:latest -t avispa/microf:2.0.0 .
```

The application should be available on port `8080`.

However, it is more appropriate to use Docker Compose file, which will build Docker image and
start μF container alongside with PostgreSQL database. It is required to provide environment file.
Create it based on the `docker/.prod.template.env`.

```shell
cd docker
docker compose --env-file=.prod.env up -d
```

To always rebuild μF image use below command. Ensure the related container does not exist.

```shell
docker compose --env-file=.prod.env up -d --no-deps --build microf
```

#### Image details

Image contains built-in LibreOffice with only Liberation fonts family supported to minimize the
image's size. LibreOffice is required by `ecm` to perform conversion of documents to `pdf`.

There are two build arguments. `MICROF_DIR` specifies internal path of μF. It is set to `/opt/microf` by default.
`EXTRACTED_DIR` specifies location of extracted final jar. By default, it is `target/extracted` relative path.

Additionally, there are several environment variables presented in the below table.

| Environment variable         | μF/ECM property              | Description                                                      |
|------------------------------|------------------------------|------------------------------------------------------------------|
| `AVISPA_ECM_FILE_STORE_PATH` | `avispa.ecm.file-store.path` | Specifies path in the image where the file store will be created |
| `AVISPA_ECM_FILE_STORE_NAME` | `avispa.ecm.file-store.name` | Specifies the name of the file store in the database             |
| `DATASOURCE_USERNAME`        | `spring.datasource.user`     | Database user                                                    |
| `DATASOURCE_PASSWORD`        | `spring.datasource.password` | Database password                                                |
| `REMOTE_DEBUG`               | `-`                          | 1 to enable remote debugging capabilities                        |
| `REMOTE_DEBUG_PORT`          | `-`                          | Remote debug port, `5005` by default                             |

For more explanation about the ECM properties please check `README.md` of `ecm` project.

#### Compose details

Below table presents environment variables, which should be set up in `.prod.env` file.

| Environment variable | Description                                                    |
|----------------------|----------------------------------------------------------------|
| `POSTGRESQL_VERSION` | Version of PostgreSQL image                                    |
| `DATABASE_USER`      | Database user, mapped to `DATASOURCE_USERNAME`                 |
| `DATABASE_PASSWORD`  | Database password, mapped to `DATASOURCE_PASSWORD`             |
| `FILE_STORE_PATH`    | Mapped to `AVISPA_ECM_FILE_STORE_PATH` and `repository` volume |
| `FILE_STORE_NAME`    | Mapped to `AVISPA_ECM_FILE_STORE_NAME`                         |
| `WORKSPACE_PATH`     | Root location on the host where all volumes will be mounted    |

The default structure looks like below:

1. Projects root path is the `WORKSPACE_PATH`
2. `config` folder contains `microf.properties` properties file (see `microf.docker.template.properties`) and
   `microf-configuration.zip` customization configuration (it is automatically packed and copied by `microf-custom`
   module)
3. `docker` folder contains environment file and `docker-compose.yml`. It is also the root path for PostgreSQL data
   volume (`data`),
   logs volume (`logs`), repository/file-store volume (`repository`) and initial setup SQL scripts (`sql`).

All SQL scripts required to correctly set up application should be gathered the `docker/sql` folder. To do that follow
below
instructions:

1. Copy sql scripts to `docker/sql` from `sql` folder in `ecm` project.
2. PostgreSQL scripts from `sql` folder (`microf` module) are automatically copied during build.
3. PostgreSQL scripts from `emc-application` and `microf-custom` modules are automatically copied during build.

### Actuators

Available actuator endpoints:

- `actuator/health`
- `actuator/info`

## Extension to types

Types logic from Avispa ECM is extended introducing concept of _validation types_. In fact, they are a typical DTOs
which separates model from the user preventing from malicious manipulation of data, which is not intended to be changed.
Validation types are additionally a validation layer by leveraging [JSR-303](https://beanvalidation.org/1.0/spec/)
specification and a source of default values for objects.

Validation types are not required if the type is not supposed to be configured via UI. Otherwise, a several steps are 
required to fully register type. Please note that the proposed "framework", which will be described below does not 
anticipate the read of the data as it is implemented by appropriate widgets with the use of Property Pages. Therefore, 
by default only add, update and delete operations are supported, but it can be easily extended.

### Adding validation type to existing type

Validation type is represented by a class implementing `Dto` interface. It can contain any fields we need from the
original object or custom ones, which can be later remapped to the actual object.

Validation type has to be registered in dedicated `DTO_OBJECT` table and mapped to specific type. Multiple validation
types can be assigned to single type. The validation type, which will be used for mapping will be determined by
discriminator value set on the type and provided in `DTO_OBJECT.DISCRIMINATOR` column.

### Registering custom type into ECM Client

In order to register type in ECM Client following classes has to be created:

- **Repository** extending or implementing `EcmObjectRepository` interface. It can contain additional custom methods. It is a typical [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/) repository.
- **Mapper** which implements `EntityDtoMapper` interface in order to define mapping of properties between object and validation object. It is recommended to use [MapStruct](https://mapstruct.org/) library.
- **Service** which extends `BaseEcmService` class and is used to implement business logic
- **Controller** extending `SimpleEcmController` class to create custom REST endpoints

### Multiple validation types

As was already mentioned, it is possible to use multiple validation types for single type with use of discriminator
feature. Apart from annotating type with `@TypeDiscriminator` annotation and registering validation types in
`DTO_OBJECT` table it is required to create separate mappers for each validation type.

In addition, there is a different set of classes, which should be used for controller and mapper.

For controller please use `MultiTypeEcmController` as a base, which in addition specifies so-called **Common Dto**.
Common Dto represents a validation type with all properties available to all subtypes. It is used for example in
property pages. It should be registered in `DTO_OBJECT` with null discriminator.

For mapper it is required to use `MultiTypeEntityDtoMapper` and provide an implementation of
`MasterMultiTypeEntityDtoMapper`, which registers mappers with use of `registerMappers` method to map discriminator
values to concrete mappers.

When creating multiple validation types it is recommended to follow below instructions:

- Create base validation type implementing `Dto` interface containing all shared properties between all subtypes
- For each subtype create classes implementing `SubtypeDetailDto` containing only subtype specific properties (detail
  validation type)
- Each subtype validation type should extend base validation type and implement `MultiTypeDto`. It should contain only
  one field named `detail` of detail validation type. The field should be annotated with `@JsonUnwrapped` annotation.
- Common validation type should just extend base validation type and include all details annotated with `@JsonUnwrapped`
  annotation.

## Payments logic

1. Deadline is always required to provide no matter if paid amount is equal to gross value of whole invoice. This value
   is calculated only on the backend side.
2. Bank account is not required when cash payment method is provided
3. Paid amount date is available only if paid amount is greater than 0.00
4. If paid amount is greater than the gross value then we have an excess payment what will be noted on the invoice

## Known issues

### General

1. When a widget fails to load, reloading always reloads only properties-widget as it is hard-coded
2. Despite there is an option to set a property page size, it is ignored and modal always use extra-large size
3. Error handling in general requires rework

### Problems with DTOs

#### Description

Initially the usage of Data Transfer Objects was a good idea. It was a good separation
between what is in the database and what we want to display in the UI. It was also separating
validation, display names and dictionaries from the object definition.

However when the tool started to become more generic application they got very
troublesome. Below are some restrictions and things needed to be aware of
when working with DTOs in MicroF.

1. DTO must be registered in DtoObject table in the database.
2. Each DTO requires additional Mapper class.
3. When the object supports Multi Types (or Subtypes) it requires additional DTO instances for each subtype and
   dedicated mapper.
4. Primitives can't be used in DTO as they are initialized with default values and null can't be assigned to them.
   Object wrappers has to be used instead.
5. Default values defined on DTO level will be inserted to the database even if we don't want to (for instance the field
   is invisible on the Property Page and wasn't sent to the server by frontend).
6. Overall additional work needed to introduce new object
