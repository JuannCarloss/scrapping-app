package com.scrap.knewin.service;

import com.scrap.knewin.dto.NewsCardsRequest;
import com.scrap.knewin.dto.NewsCardsResponse;
import com.scrap.knewin.exceptions.InfomoneyRequestException;
import com.scrap.knewin.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ScrapeNews {

    private static final String NEWS_CARDS_URL = "https://www.infomoney.com.br/wp-json/infomoney/v1/cards";
    private final RestTemplate restTemplate;

    public ScrapeNews(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getNewsCards() throws IOException {

        NewsCardsRequest newsCardsRequest = NewsCardsRequest
                                            .builder()
                                            .post_id(2565621L)
                                            .categories(List.of(1))
                                            .tags(List.of())
                                            .build();

        ResponseEntity<List<NewsCardsResponse>> response = restTemplate.exchange(
                NEWS_CARDS_URL,
                HttpMethod.POST,
                new HttpEntity<>(newsCardsRequest),
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() != HttpStatusCode.valueOf(200)){
            log.error("Erro ao fazer requisição de método: POST - Para API Infomoney - Status Code: {}", response.getStatusCode().value());
            throw new InfomoneyRequestException("Erro ao requisitar a API Infomoney");
        }

        List<NewsCardsResponse> cardsResponse = Objects.requireNonNull(response.getBody()).stream().limit(12).toList();

        for (NewsCardsResponse card : cardsResponse){
            scrapeArticle(card.getPost_permalink());
        }
    }

    public void scrapeArticle(String url) throws IOException {

        if (url == null || url.isEmpty()){
            throw new InvalidRequestException("O Parâmetro URL não pode ser nulo");
        }

        Document doc;

        try {

            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();

        } catch (UnknownHostException unknownHostException){
            log.error("URL de requisição não reconhecida! URL: {}", url);
            throw new InfomoneyRequestException("Erro ao efetuar requisição de scrapping para Infomoney");
        }

        Element titleElement = doc.selectFirst("h1, .entry-title");
        String title = titleElement != null ? titleElement.text().trim() : "Nenhum título encontrado";

        Element subtitleElement = doc.selectFirst(".text-lg.font-medium.tracking-tight");
        String subtitle = subtitleElement != null ? subtitleElement.text().trim() : "Nenhum subtítulo encontrado";

        Element authorElement = doc.selectFirst(".text-base.font-semibold.tracking-tight");
        String author = authorElement != null ? authorElement.text().trim() : "Nenhum autor encontrado";

        Element timeElement = doc.selectFirst("time, .entry-date, .meta .date");
        String publishedAt = timeElement != null ? timeElement.text().trim() : "Nenhuma data encontrada";

        Elements contentParagraphs = doc.select("article");
        StringBuilder contentBuilder = new StringBuilder();
        for (Element p : contentParagraphs) {
            contentBuilder.append(p.text());
        }
        String content = contentBuilder.toString().trim();

        log.info("URL: {}", url);
        log.info("Título: {}", title);
        log.info("Subtítulo: {}", subtitle);
        log.info("Autor: {}", author);
        log.info("Data de publicação: {}", publishedAt);
        log.info("Conteúdo: {}", content);
        System.out.println("\n");
    }
}
