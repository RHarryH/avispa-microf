# Avispa μF

Small tool for generating invoices for Avispa purposes

## Payments logic

1. Deadline is always required to provide no matter if paid amount is equal to gross value of whole invoice. This value is calculated only on the backend side.
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

1. DTO and Common DTO must be registered in DtoObject table in the database.
2. Each DTO requires additional Mapper class.
3. When the object supports Multi Types (or Subtypes) it requires additional DTO instances for each subtype and dedicated mapper.
4. To work correctly with List Widget, Common DTO has been introduced being the almost exact map of fields in the object.
5. When Multi Types are not used then DTO and Common DTO are the same types.
6. Primitives can't be used in DTO as they are initialized with default values and null can't be assigned to them. Object wrappers has to be used instead.
7. Default values defined on DTO level will be inserted to the database even if we don't want to (for instance the field is invisible on the Property Page and wasn't sent to the server by frontend).
8. Overall additional work needed to introduce new object

#### Proposed solution
1. Complete removal of DTOs
2. Dictionaries, display names and default values can be moved to the extended Type object (in form of JSON Content or database tables)
3. Not sure what about validation - can annotation be still used on the object level or the validation should be written manually?
