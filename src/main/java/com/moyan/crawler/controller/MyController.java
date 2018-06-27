package com.moyan.crawler.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.moyan.crawler.entity.Entity;
import com.moyan.crawler.factory.CrawlerControllerFactory;
import com.moyan.crawler.jpa.MyJPA;
import com.moyan.crawler.scraper.MyCrawler;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/webCrawler_products")
public class MyController{
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private MyJPA myJPA;


    public List<Entity> List(){
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Entity> list(){
        return myJPA.findAll();
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public Entity save(Entity entity){
        return myJPA.save(entity);
    }
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public List<Entity> delete(Entity entity){
        myJPA.delete(entity);
        return myJPA.findAll();
    }



}
