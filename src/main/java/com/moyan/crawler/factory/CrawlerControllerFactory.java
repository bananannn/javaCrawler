package com.moyan.crawler.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.moyan.crawler.scraper.MyCrawler;
import com.moyan.crawler.service.SqlService;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public class CrawlerControllerFactory implements CrawlController.WebCrawlerFactory {

    SqlService service;

    public CrawlerControllerFactory(SqlService service) {
        this.service = service;
    }

    @Override
    public MyCrawler newInstance(){
        return new MyCrawler(service);
    }
}

