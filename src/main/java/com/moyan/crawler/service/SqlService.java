package com.moyan.crawler.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.moyan.crawler.entity.Entity;
import com.moyan.crawler.jpa.MyJPA;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class SqlService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SqlService.class);

    DruidDataSource dataSource;


    @Autowired
    private MyJPA myJPA;

    /*public SqlService(DruidDataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        insertKeyStatement = dataSource.getConnection().prepareStatement("insert into webpage values " +
                "(nextval('id_master_seq'),?,?,?,?)");
    }*/
    public void save(Entity entity) {
        //logger.info("Storing in dataBase:" + entity.getInfo());
        if ( myJPA.findById(entity.getPid()).isPresent() ){
            logger.info("updating: " + entity.getInfo());
            myJPA.saveAndFlush(entity);
        }
        else{
            logger.info("Storing in dataBase:" + entity.getInfo());
            myJPA.save(entity);
        }
    }

    public void delete(Entity entity){
        logger.info("Delete from database: " + entity.getInfo());
        if(myJPA.findById(entity.getPid()).isPresent() )
            myJPA.delete(entity);
        else logger.error("No such entity in the data base");
    }

    public void update(Entity entity){
        logger.info("updating: " + entity.getInfo());
        if(myJPA.findById(entity.getPid()).isPresent() )
           myJPA.saveAndFlush(entity);
        else logger.error("cannot update current entity");
    }

}
