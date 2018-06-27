package com.moyan.crawler.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.moyan.crawler.factory.CrawlerControllerFactory;
import com.moyan.crawler.scraper.MyCrawler;
import com.moyan.crawler.service.SqlService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    // TODO
    @Autowired
    private SqlService service;

    @Override
    public void run(String ... args) throws Exception{
        String[] crawlDomains = {"https://guangdiu.com/", "https://guangdiu.com/cate.php?k=makeup",
                "https://guangdiu.com/cate.php?k=electrical"};
        String crawlerFolder = "com/moyan/crawler/resources/crawlerstorage";
        int numberOfCrawlers = 4;

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlerFolder);

        /*
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(1000);

        /*
         * You can set the maximum crawl depth here. The default value is -1 for
         * unlimited depth
         */
        config.setMaxDepthOfCrawling(2);

        /*
         * You can set the maximum number of pages to crawl. The default value
         * is -1 for unlimited number of pages
         */
        config.setMaxPagesToFetch(1000);

        /**
         * Do you want crawler4j to crawl also binary data ?
         * example: the contents of pdf, or the metadata of images etc
         */
        config.setIncludeBinaryContentInCrawling(false);

        /*
         * This config parameter can be used to set your crawl to be resumable
         * (meaning that you can resume the crawl from a previously
         * interrupted/crashed crawl). Note: if you enable resuming feature and
         * want to start a fresh crawl, you need to delete the contents of
         * rootFolder manually.
         */
        config.setResumableCrawling(false);
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages         */
        for(String domain: crawlDomains){
            controller.addSeed(domain);
        }

        MyCrawler.configure(crawlDomains, crawlerFolder);
        CrawlerControllerFactory factory = new CrawlerControllerFactory(service);
        controller.startNonBlocking(factory, numberOfCrawlers);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        //controller.start(MyCrawler.class, numberOfCrawlers);




    }

}
