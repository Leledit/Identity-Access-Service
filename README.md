# Identity Access Service

API REST para gerenciamento de identidade e acesso (IAM), com autenticacao via JWT e autorizacao baseada em RBAC (User, Role, Permission).

Projeto pensado para cenarios corporativos com foco em:
- Seguranca com Spring Security
- Controle de acesso granular por permissao
- Persistencia em MongoDB
- Organizacao por dominio (users, roles, permissions)

## Visao geral

O sistema permite:
- Criar e autenticar usuarios
- Gerar token JWT no login
- Gerenciar roles e permissoes
- Associar roles aos usuarios
- Proteger endpoints com `@PreAuthorize` e authorities

## Stack utilizada

- Java (conforme `pom.xml`: `java.version=25`)
- Spring Boot
- Spring Security
- JWT (`io.jsonwebtoken`)
- Spring Data MongoDB
- ModelMapper
- Lombok
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper (`./mvnw`)

## Arquitetura e organizacao

Estrutura principal do codigo em `src/main/java/com/leandro/identityAccessService`:

- `security/` autenticacao, JWT, filtros e login
- `user/` dominio de usuario (controller, service, repository, mapper)
- `roles/` dominio de papeis
- `permission/` dominio de permissoes
- `common/` configuracoes globais, excecoes e utilitarios

Padroes aplicados:
- Separacao por camadas (controller -> service -> repository)
- DTOs para trafego HTTP
- Mapeamento entidade/DTO com mapper dedicado
- Tratamento centralizado de excecoes

## Modelo de autorizacao (RBAC)

- **User** possui uma lista de `roleIds`
- **Role** possui uma lista de `permissionIds`
- **Permission** representa uma autoridade de negocio (ex.: `USER_CREATE`, `ROLE_READ_ALL`)

As permissoes sao convertidas para `GrantedAuthority` no fluxo de autenticacao, permitindo uso de:

```java
@PreAuthorize("hasAuthority('USER_CREATE')")
```

## Autenticacao JWT

Fluxo resumido:
1. Cliente envia credenciais para `POST /auth/login`
2. API valida usuario/senha com `AuthenticationManager`
3. API gera e retorna `accessToken`
4. Cliente envia token no header `Authorization: Bearer <token>`
5. Filtro JWT valida token e popula `SecurityContext`

## Endpoints principais

### Auth
- `POST /auth/login`

### User
- `POST /api/v1/user`
- `GET /api/v1/user`
- `GET /api/v1/user/{id}`
- `PATCH /api/v1/user/{id}`
- `DELETE /api/v1/user/{id}`
- `POST /api/v1/user/{id}/add-role?roleId=...`
- `DELETE /api/v1/user/{id}/remove-role?roleId=...`

### Role
- `POST /api/v1/role`
- `GET /api/v1/role`
- `GET /api/v1/role/{id}`
- `PATCH /api/v1/role/{id}`
- `DELETE /api/v1/role/{id}`
- `POST /api/v1/role/{id}/add-permission?permissionId=...`
- `DELETE /api/v1/role/{id}/remove-permission?permissionId=...`

### Permission
- `POST /api/v1/permission`
- `GET /api/v1/permission`
- `GET /api/v1/permission/{id}`
- `PATCH /api/v1/permission/{id}`
- `DELETE /api/v1/permission/{id}`


# Acessar documentacao

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Exemplo rapido de uso

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "admin@local.com",
    "password": "123456"
  }'
```

### Chamada autenticada

```bash
curl -X GET http://localhost:8080/api/v1/user \
  -H 'Authorization: Bearer SEU_TOKEN_AQUI'
```
