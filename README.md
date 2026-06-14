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

## Como executar localmente

> Observacao: as propriedades locais estao em `src/main/resources/config/application-local.yml`.

### 1) Subir MongoDB (exemplo com Docker)

```bash
docker run -d --name identity-mongo \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin \
  mongo:7
```

### 2) Rodar a aplicacao

```bash
cd /home/user/Documentos/workSpace/identityAccessService
./mvnw spring-boot:run
```

### 3) Acessar documentacao

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes

Para executar os testes:

```bash
cd /home/user/Documentos/workSpace/identityAccessService
./mvnw test
```

Se houver erro de versao do Java no ambiente local, alinhe o JDK com o valor definido em `pom.xml`.

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

## Diferenciais para portfolio

- Implementacao de seguranca com JWT + Spring Security
- Controle de acesso corporativo por permissao
- Modelagem RBAC escalavel
- Estrutura de codigo orientada a manutencao
- Boas praticas de API REST e tratamento de excecoes

## Melhorias futuras (roadmap)

- Cobertura de testes unitarios e integracao para fluxos de seguranca
- Refresh token e estrategia de revogacao
- Observabilidade (logs estruturados, metricas, tracing)
- Pipeline CI/CD com validacoes de qualidade e seguranca
- Gerenciamento de secrets por ambiente

## Autor

Leandro

---

Se quiser, eu tambem posso criar uma versao **README portfolio premium** com:
1. badges (build, cobertura, stack)
2. diagramas (arquitetura e fluxo JWT)
3. sessao de desafios tecnicos e decisoes arquiteturais
4. secao "lessons learned" para destacar senioridade

