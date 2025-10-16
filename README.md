# Web Scrapper de Notícias

Um App capaz de requisitar o endpoint da <b>Infomoney</b> e fazer um Scrapping de informações de cada notícia retornada

# Funcionamento

1) Faz uma requisição POST para o endpoint:

```bash
  https://www.infomoney.com.br/wp-json/infomoney/v1/cards
```
enviando um corpo ````NewsCardsRequest```` com parâmetros básicos para obter os cards de notícias.

<hr>

2) Recebe uma lista de artigos ````NewsCardsResponse```` e limita a 12 primeiros resultados.

<hr>

3) Para cada notícia, realiza o scraping da página usando Jsoup, extraindo:

- Título

- Subtítulo

- Autor

- Data de publicação

- Conteúdo

<hr>

4) Todos os dados são exibidos nos logs da aplicação.

<hr>

# Tecnologias utilizadas

- Java 21
- Spring boot 3
- JSoup(Web Scrapping)
- RestTemplate(Requisições HTTP)
- Docker (Containerização para Build & Run)

# Estrutura Principal

- ```ScrapeNews:``` Serviço principal que executa o scraping

- ```NewsCardsRequest:``` DTO de envio para a API do InfoMoney

- ```NewsCardsResponse:``` DTO de resposta da API

- ```InfomoneyRequestException:``` Exceção personalizada para falhas de requisição para API Externa.

- ```InvalidRequestException:``` Exceção personalizada para falhas nos endpoints internos da aplicação.

- ```NewsScrapeController:``` Controller com endpoints para a execução das funcionalidades.

# Como executar o App

### Caso você tenha docker no seu computador:

- Clone o projeto.
- Siga até a pasta raiz do projeto.
- Abra seu terminal e execute: ```docker build -t scrapping-app -f docker/Dockerfile .``` para criar a imagem do build.
- Em seguida execute: ```docker run -d -p 8080:8080 --name scrapping-app-container scrapping-app``` para subir o app.
- App disponível na porta ```8080```.

### Caso não tenha docker:

- Clone o projeto.
- Abra o projeto na sua IDE de preferência.
- Execute o app a partir da classe main ```KnewinApplication.class```
- App disponível na porta ```8080```.

# Como utilizar o App

Caso o app já esteja funcional em sua máquina na porta ```8080```, você pode executar os seguintes comandos:

```bash
curl --request GET \
  --url http://localhost:8080/news/cards
```
Comando responsável por fazer o scrapping das 12 últimas notícias do site Infomoney e printar os dados no console da aplicação

````bash
curl --request GET \
  --url 'http://localhost:8080/news/scrape?url='
````
Comando responsável por fazer o scrapping de uma notícia em específico enviando como parâmetro a URL da mesma.

# Observações

- O scraping depende da estrutura atual do site InfoMoney — se os seletores CSS mudarem, será necessário ajustar os seletores usados no Jsoup.

- O método atual apenas imprime os resultados, mas pode ser facilmente adaptado para salvar em banco de dados ou exportar para outro formato.

- Caso não queira executar cURLs para requisições web, é possível utilizar a collection insomnia ````scrapping-app-collection.yaml```` para importar os endpoints.