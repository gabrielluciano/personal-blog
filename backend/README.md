<!-- Improved compatibility of voltar ao topo link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->

<a name="readme-top"></a>

<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/gabrielluciano/personal-blog">
    <img src="../frontend/src/assets/gabrielluciano-logo.png" alt="Logo" width="250">
  </a>

<h3 align="center">Personal Blog Backend</h3>

  <p align="center">
    Este é o projeto do backend do meu blog pessoal, desenvolvido em Spring Boot.
    <br />
    <a href="https://github.com/gabrielluciano/personal-blog/tree/main/backend/README.md"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://blog.gabrielluciano.com">Ver Demo</a>
    ·
    <a href="https://github.com/gabrielluciano/personal-blog/issues">Reportar Bug</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Índice</summary>
  <ol>
    <li>
      <a href="#sobre-o-projeto">Sobre o Projeto</a>
      <ul>
        <li><a href="#construído-com">Construído Com</a></li>
        <li><a href="#features">Features</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#pré-requisitos">Pré-requisitos</a></li>
        <li><a href="#setup">Setup</a></li>
      </ul>
    </li>
    <li><a href="#utilização">Utilização</a></li>
    <li><a href="#endpoints">Endpoints</a></li>
    <li><a href="#licença">Licença</a></li>
    <li><a href="#contato">Contato</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## Sobre o Projeto

Este é o projeto do backend do meu blog pessoal, desenvolvido em Spring Boot.

Trata-se de uma API construída com Spring Boot e PostgreSQL utilizando JPA, que inclui funcionalidades para criação e edição de posts, tags e gerenciamento de usuários. Além disso, inclui várias outras funcionalidades, como autenticação jwt com Spring Security, testes automatizados e deploy automático para a AWS utilizando github actions e CodeDeploy. Veja o restante desta documentação para mais detalhes!

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

### Construído Com

- [![Java][Java]][Java-url]
- [![Spring Boot][SpringBoot]][Spring-url]
- [![Postgres][Postgres]][Postgres-url]
- [![Docker][Docker]][Docker-url]
- [![Github Actions][GithubActions]][GithubActions-url]
- [![AWS][AWS]][Aws-url]
- E outros!

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

### Features

- Endpoint para criação, edição, exclusão e obtenção de Posts com suporte a paginação e ordenação.
- Endpoint para criação, edição, exclusão e obtenção de Tags com suporte a paginação e ordenação.
- Cadastro, login e alteração de roles de usuários (user, editor e admin).
- Associação entre posts e classes e autor do post. Mapeamento objeto-relacional criado com JPA. Baixe o [diagrama de classes](https://github.com/gabrielluciano/personal-blog/blob/main/class-diagram.drawio)
- Utilização de DTOs (Data Transfer Objects) para tranferência de dados com mapeamento DTO/Entidade realizado com mapstruct
- Segurança de endpoints com Spring Security. Autenticação stateless via token JWT
- Tratamento de exceções.
- Testes unitários e de integração.
- Workflow para execução automática de testes unitários e de integração em pull requests, utilizando Github Actions, disponível em [backend-tests.yml](https://github.com/gabrielluciano/personal-blog/blob/main/.github/workflows/backend-tests.yml)
- Workflow para para execução automática de testes e deploy para AWS ao realizar merges ou commits na main branch que modifiquem arquivos do backend, utilizando Github Actions, AWS CodeDeploy e Docker

  - Veja o arquivo de configuração do CodeDeploy em [appspec.yml](https://github.com/gabrielluciano/personal-blog/blob/main/appspec.yml), os scripts executados na instância EC2 durante o deploy em [aws/scripts](https://github.com/gabrielluciano/personal-blog/tree/main/aws/scripts) e o workflow do Github Actions [backend-deploy.yml](https://github.com/gabrielluciano/personal-blog/blob/main/.github/workflows/backend-deploy.yml).

  - Detalhes de como implementar o deploy estão disponíveis neste [post](https://aws.amazon.com/blogs/devops/integrating-with-github-actions-ci-cd-pipeline-to-deploy-a-web-app-to-amazon-ec2/).

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

Veja abaixo as instruções para configurar e iniciar o ambiente de desenvolvimento do projeto

### Pré-requisitos

Para executar o projeto, você deve ter o Java JDK na versão 17, o gerenciador de dependências Maven, o GIT e o banco de dados PostgreSQL instalado em sua máquina. Opcionalmente, você pode utilizar o Docker para o banco de dados, como será mostrado na sequência.

- Verificando a versão do Java

  ```sh
  java -version
  javac -version
  ```

- Verificando a versão do Maven
  ```sh
  mvn -v
  ```

### Setup

1. Clone o repositório
   ```sh
   git clone https://github.com/gabrielluciano/personal-blog.git
   ```
2. Acesse o diretório
   ```sh
   cd personal-blog/backend
   ```
3. Inicie o banco de dados utilizando docker compose
   ```sh
   docker compose -f docker-compose-db.yml up -d
   ```
4. Inicie a aplicação em modo de desenvolvimento. Caso a flag `local` não seja especificada, a aplicação utilizará as prorpriedades de produção, que incluem procurar por variáveis de ambiente e certificado SSL, o que pode gerar erro caso a aplicação não encontre esse arquivos.
   ```sh
   mvn spring-boot:run -D"spring-boot.run.profiles"=local
   ```
5. Teste o funcionamento da aplicação
   ```sh
   curl http://localhost:8080/posts
   ```

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- USAGE EXAMPLES -->

## Utilização

Para detalhes sobre como utilizar a API, veja os endpoints abaixo. Em breve estarei disponibilizando um endpoint para a documentação utilizando Swagger.

- Executar testes unitários. Dentro do diretório backend utilize o comando

  ```sh
  mvn test
  ```

- Executar testes de integração. Dentro do diretório backend utilize o comando

  ```sh
  mvn test -Pintegration-tests
  ```

- Criando o arquivo jar do projeto
  ```sh
  mvn clean package -DskipTests
  ```

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- ENDPOINTS -->

## Endpoints

Conheça a lista de endpoints disponíveis na API.

### Post

```
GET /posts[?title][?tag][?drafts]

    Returns a page of posts (Security: UNAUTHENTICATED/EDITOR)

    Return status:
        200 (OK)

    Query parameters:
        title - Matches all posts that have the value on its title
        tag - Filter posts of a specific tag
        drafts - If true returns not published posts (requires editor role). Defaults to false

    Note: Spring Boot Pageable query parameters are also allowed (check spring docs)
```

```
GET /posts/{id}

    Find a post by id and returns it (Security: UNAUTHENTICATED)

    Return status:
        200 (OK) - The post was found and returned
        404 (NOT_FOUND) - The post was not found
```

```
GET /posts/slug/{slug}

    Find a post by slug and returns it (Security: UNAUTHENTICATED)

    Return status:
        200 (OK) - The post was found and returned
        404 (NOT_FOUND) - The post was not found
```

```
POST /posts

    Creates a new post and returns it (Security: EDITOR)

    Return status:
        201 (CREATED) - Post created with success
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{id}

    Updates an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Post updated with success
        404 (NOT_FOUND) - The post was not found
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /posts/{id}

    Deletes an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Post deleted with success
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{postId}/tags/{tagId}

    Adds a new tag to an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag was successfully added to the post
        404 (NOT_FOUND) - The post or tag was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /posts/{postId}/tags/{tagId}

    Deletes a tag from an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag was successfully deleted from the post
        404 (NOT_FOUND) - The post or tag was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{postId}/publish

    Publish a post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - The command was successfuly executed
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{postId}/unpublish

    Unpublish a post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - The command was successfuly executed
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

### Tag

```
GET /tags

    Returns a page of tags (Security: UNAUTHENTICATED)

    Return status:
        200 (OK)

    Note: Spring Boot Pageable query parameters are allowed (check spring docs)
```

```
GET /tags/{id}

    Find a tag by id and returns it (Security: UNAUTHENTICATED)

    Return status:
        200 (OK) - The tag was found and returned
        404 (NOT_FOUND) - The tag was not found
```

```
POST /tags

    Creates a new tag and returns it (Security: EDITOR)

    Return status:
        201 (CREATED) - Tag created with success
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /tags/{id}

    Updates an existing tag (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag updated with success
        404 (NOT_FOUND) - The tag was not found
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /tags/{id}

    Deletes an existing tag (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag deleted with success
        404 (NOT_FOUND) - The tag was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

### User

```
POST /signup

    Allows user to create an account providing name, email and password

    Return status:
        201 (CREATED) - User created with success
        400 (BAD_REQUEST) - Request body is invalid
```

```
POST /login

    Allows user to login using its email and password returning a JWT Token

    Return status:
        200 (OK) - User successfully logged in
        401 (UNAUTHORIZED) - Wrong credentials were provided
```

```
PUT /users/{id}/roles/editor

    Grant EDITOR role to an user (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Role EDITOR was successfully granted to the user
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /users/{id}/roles/editor

    Removes EDITOR role from an user (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Role EDITOR was successfully removed from the user
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- LICENSE -->

## Licença

Distribuído sobre a licença MIT. Veja `LICENSE.txt` para mais informações.

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- CONTACT -->

## Contato

Gabriel Luciano - [/in/gabriel-lucianosouza/](https://linkedin.com/in/gabriel-lucianosouza)

Link do Projeto: [https://github.com/gabrielluciano/personal-blog](https://github.com/gabrielluciano/personal-blog)

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/gabrielluciano/personal-blog.svg?style=for-the-badge
[contributors-url]: https://github.com/gabrielluciano/personal-blog/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/gabrielluciano/personal-blog.svg?style=for-the-badge
[forks-url]: https://github.com/gabrielluciano/personal-blog/network/members
[stars-shield]: https://img.shields.io/github/stars/gabrielluciano/personal-blog.svg?style=for-the-badge
[stars-url]: https://github.com/gabrielluciano/personal-blog/stargazers
[issues-shield]: https://img.shields.io/github/issues/gabrielluciano/personal-blog.svg?style=for-the-badge
[issues-url]: https://github.com/gabrielluciano/personal-blog/issues
[license-shield]: https://img.shields.io/github/license/gabrielluciano/personal-blog.svg?style=for-the-badge
[license-url]: https://github.com/gabrielluciano/personal-blog/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/gabriel-lucianosouza
[Java]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/
[SpringBoot]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot
[Typescript]: https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white
[Typescript-url]: https://www.typescriptlang.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Postgres]: https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
[Postgres-url]: https://www.postgresql.org/
[Docker]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
[GithubActions]: https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white
[GithubActions-url]: https://github.com/features/actions
[AWS]: https://img.shields.io/badge/Amazon_AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=whit
[Aws-url]: https://aws.amazon.com/
[Vercel]: https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white
[Vercel-url]: https://vercel.com/
