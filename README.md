# Portfolio (Domain vs Entity split)

This project demonstrates a **clean split** between:

- **Domain model** (pure Java, no JPA annotations)
- **Persistence model** (JPA entities + repositories)
- **Mapping layer** (Entity <-> Domain)
- **Query-like operations** via Spring Data **Specifications**
- **State-based tests** that assert on data returned from repositories/services.

## Why split?
JPA requires entities to be classes, but you can keep your **domain** loosely coupled by:
- exposing domain interfaces/records
- keeping `@Entity` classes in a persistence package
- mapping at service boundaries

## Run tests
```bash
mvn test
```

## Packages
- `com.example.portfolio.domain` : domain interfaces/records (no JPA)
- `com.example.portfolio.persistence` : JPA entities + repository + specs
- `com.example.portfolio.service` : service layer using repo + mapper
