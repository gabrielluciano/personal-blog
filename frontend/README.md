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

<h3 align="center">Personal Blog Frontend</h3>

  <p align="center">
    Este é o projeto do frontend do meu blog pessoal, desenvolvido em Angular!
    <br />
    <a href="https://github.com/gabrielluciano/personal-blog/tree/main/frontend/README.md"><strong>Explore the docs »</strong></a>
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
    <li><a href="#testes">Testes</a></li>
    <li><a href="#linting-e-code-formatting">Linting e code formatting</a></li>
    <li><a href="#build">Build</a></li>
    <li><a href="#licença">Licença</a></li>
    <li><a href="#contato">Contato</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## Sobre o Projeto

Este é o projeto do backend do meu blog pessoal, desenvolvido em Angular.

Esta aplicação é responsável por consumir a API do backend e fazer a interface com o usuário. Entre as principais funcionalidades estão a exibição de posts recentes e tags, paginação, criação, edição, publicação e despublicação de exclusão de posts, login, code highlighting e outros. Veja a seção <a href="#features">Features</a> e o restante da documentação para mais detalhes.

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

### Construído Com

- [![Angular][Angular.io]][Angular-url]
- [![Typescript][Typescript]][Typescript-url]
- [![Github Actions][GithubActions]][GithubActions-url]
- [![Vercel][Vercel]][Vercel-url]
- E outros!

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

### Features

- Página de posts recentes com paginação e possibilidade de se alterar número de páginas
- Página de posts de tag específica
- Criação, edição, exclusão, publicação e despublicação de posts
- Suporte a markdown na criação de posts
- Code highlighting para snippets de código
- Formulário de login
- Acesso baseado em roles de usuário
- Server-side rendering utilizando Angular Universal
- Serverless function para deploy na Vercel utilizando SSR
- Testes unitários
- Workflow do Github Actions para execução de testes automtizados, build da aplicação e deploy de preview e produção na Vercel

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

Veja abaixo as instruções para configurar e iniciar o ambiente de desenvolvimento do projeto

### Pré-requisitos

Para executar o projeto, você deve ter o Node JS na versão v16 ou v18 instalado e o Angular CLI na versão 16. Certifique-se também de ter o npm instalado.

- Verificando a versão do Node

  ```sh
  node -v
  ```

- Verificando a versão do npm

  ```sh
  npm -v
  ```

- Verificando a versão do Angular CLI
  ```sh
  ng version
  ```

### Setup

1. Clone o repositório

   ```sh
   git clone https://github.com/gabrielluciano/personal-blog.git
   ```

2. Acesse o diretório

   ```sh
   cd personal-blog/frontend
   ```

3. Instale as dependências

   ```sh
   npm install
   ```

4. Se necessário, edite a url do frontend em siteUrl e do backend em apiUrl e angularJwtAllowedDomains no arquivo `src/environments/environment.development.ts`

   ```typescript
   export const environment = {
     siteUrl: 'http://localhost:4200/',
     apiUrl: 'http://localhost:8080/',
     angularJwtAllowedDomains: ['localhost:8080'],
   };
   ```

5. Inicie a aplicação em modo de desenvolvimento

    ```sh
    ng serve
    ```

6. (Opcional) Inicie a aplicação em modo de desenvolvimento utilizando Server-Side Rendering com Angular Universal

   ```
   npm run dev:ssr
   ```

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- TESTES -->

## Testes

- Para executar os testes unitários com visualização dos testes via browser utilize o comando abaixo

  ```sh
  ng test
  ```

- Para executar os testes unitários uma única vez utilizando um browser headless, utilize o comando abaixo. Esta versão é útil para pipelines de CI

  ```sh
  npm run test
  ```

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- LINTING E CODE FORMATTING -->

## Linting e code formatting

- Para executar uma verificação do código utilizando eslint, utilize o comando

  ```sh
  ng lint
  ```

- Para formatar o código automaticamente para o padrão do projeto utilizando o Prettier, utilize o comando

  ```sh
  npx prettier --write .
  ```

- Se preferir executar apenas uma verificação da formatação do código, utilize a flag `--check`

  ```sh
  npx prettier --check .
  ```

<p align="right">(<a href="#readme-top">voltar ao topo</a>)</p>

<!-- BUILD -->

## Build

- Para gerar o build do projeto em modo de produção, primeiro certifique-se de editar a URL do frontend e backend no arquivo `src/environments/environment.ts` de maneira similar ao que foi realizado no setup.

- Para buildar o projeto utilize o comando 

  ```sh
  ng build
  ```

- Para buildar a versão com SSR utilize 

  ```sh
  npm run build:ssr
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
