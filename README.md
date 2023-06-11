# Avispa μF

Small tool for generating invoices for Avispa purposes.

## Running

Two environments are available:

- `dev` - can be run locally using IDE with `-Dspring.profiles.active=dev` property set.
  It uses H2 database and `data-h2.sql` scripts present in `resources` of each project. The schema is created
  automatically.
- `prod` - configured to be run inside Docker container, it uses PostgreSQL database

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

There is one build argument `MICROF_PATH` specifying internal path of μF. It is set to `/opt/microf` by default.

Additionally, there are several environment variables presented in the below table.

| Environment variable         | μF/ECM property              | Description                                                      |
|------------------------------|------------------------------|------------------------------------------------------------------|
| `AVISPA_ECM_FILE_STORE_PATH` | `avispa.ecm.file-store.path` | Specifies path in the image where the file store will be created |
| `AVISPA_ECM_FILE_STORE_NAME` | `avispa.ecm.file-store.name` | Specifies the name of the file store in the database             |
| `DATASOURCE_USERNAME`        | `spring.datasource.user`     | Database user                                                    |
| `DATASOURCE_PASSWORD`        | `spring.datasource.password` | Database password                                                |
| `DEBUG`                      | `-`                          | 1 to enable remote debugging capabilities                        |
| `DEBUG_PORT`                 | `-`                          | Remote debug port, `5005` by default                             |

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
2. Copy custom sql scripts to `docker/sql` from `sql` folder in `microf` project.
3. Scripts from `emc-application` and `microf-custom` are automatically copied during build.

### Actuators

Available actuator endpoints:

- `actuator/health`
- `actuator/info`

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

#### Proposed solution

1. Complete removal of DTOs
2. Dictionaries, display names and default values can be moved to the extended Type object (in form of JSON Content or
   database tables)
3. Not sure what about validation - can annotation be still used on the object level or the validation should be written
   manually?
