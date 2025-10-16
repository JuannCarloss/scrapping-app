package com.scrap.knewin.resources;

import com.scrap.knewin.service.ScrapeNews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsScrapeController {

    private final ScrapeNews scrapeNews;

    public NewsScrapeController(ScrapeNews scrapeNews) {
        this.scrapeNews = scrapeNews;
    }

    @GetMapping("/cards")
    @ResponseStatus(HttpStatus.OK)
    public void getNewsCards() throws IOException {
        log.info("Iniciando scrapping de notícias");
        scrapeNews.getNewsCards();
        log.info("Finalizando scrapping de notícias");
    }

    @GetMapping("/scrape")
    @ResponseStatus(HttpStatus.OK)
    public void scrapeNews(@RequestParam("url")String url) throws IOException {
        scrapeNews.scrapeArticle(url);
    }
}
