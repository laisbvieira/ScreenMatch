# 🎬 Screen Match

Aplicação Java com Spring Boot que permite buscar, salvar e explorar informações sobre séries e episódios usando a API do OMDb. Através de um menu interativo no terminal, o usuário pode pesquisar séries por título, ator, gênero, avaliação e muito mais!

## 💡 Funcionalidades

- 🔍 Buscar séries pela API OMDb
- 🎞️ Buscar episódios de uma série
- 📋 Visualizar lista de séries buscadas
- 🎭 Buscar séries por ator/atriz
- 🏆 Top 5 séries com melhores avaliações
- 🎯 Buscar por gênero ou trecho do título
- 🗂️ Filtrar séries por temporadas e avaliação
- ⭐ Ver os 5 episódios mais bem avaliados de uma série

## 📦 Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (em memória)
- OMDb API
- IntelliJ IDEA

## 🚀 Como executar

1. Clone o repositório:

```bash
git clone https://github.com/laisbvieira/ScreenMatch.git
cd ScreenMatch
```
2. Configure a chave da API OMDb:

3. Crie um arquivo .env com o seguinte conteúdo:

``bash
OMDB_API_KEY=sua_chave_aqui
``

4. Execute o projeto a partir da classe ScreenmatchApplication.java.

5. No terminal, siga o menu interativo.

## 🧪 Exemplo de uso

1 - Buscar séries
Digite o nome da série: Breaking Bad

