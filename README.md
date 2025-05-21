# DevCompass Back-End
Facens - Engenharia de Software - 2025/S1

## Padrões implementados
Devido à proposta do projeto—cuja funcionalidade é, majoritariamente, de compilar dados em relatórios, criar e visualizar vagas—sua funcionalidade é, em grande parte, apenas de CRUD e, portanto, não faria bom uso, considerando o escopo do projeto e as funcionalidades planejadas, de nenhum design pattern visto em aula; A modelagem desenvolvida, no entanto, não deixa de aplicar princípios SOLID e orientação a objetos corretamente—com exceção de um caso onde não foi possível aderir ao princípio da responsabilidade única (verificação de permissões de usuário em classes de serviço).

## Setup
Setup do projeto e inclusão de dependências iniciais foi realizado utilizando a ferramenta [Spring Initializr](https://start.spring.io).
![Configurações utilizadas no Spring Initializr](/img/initializr_setup.png)

## Referências
Esta secção contem artigos e páginas que foram úteis durante o desenvolvimento desse projeto.
- [Baeldung - Spring Boot With H2 Database](https://www.baeldung.com/spring-boot-h2-database)
- [Baeldung - Hibernate One to Many Annotation Tutorial](https://www.baeldung.com/hibernate-one-to-many)
- [Baeldung - Many-To-Many Relationship in JPA](https://www.baeldung.com/jpa-many-to-many)
- [SpringDoc (Swagger)](https://springdoc.org/)
- [Jetbrains Forum - Tópico 'Lombok not workin with Intellij'](https://intellij-support.jetbrains.com/hc/en-us/community/posts/23064675521682-Lombok-not-workin-with-Intellij)