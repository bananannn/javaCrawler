package com.moyan.crawler.jpa;

import com.moyan.crawler.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface MyJPA extends
    JpaRepository<Entity, Long>,
    JpaSpecificationExecutor<Entity>,
    Serializable
{


}
