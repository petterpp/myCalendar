/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.config.AbstractMongoConfiguration;
import com.mongodb.Mongo;
 
/**
 * Spring MongoDB configuration file
 * 
 */
@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
 
    @Override
    public @Bean Mongo mongo() throws Exception {

        return new Mongo("localhost");
    }

    @Override
    public @Bean MongoTemplate mongoTemplate() throws Exception {

        return new MongoTemplate(mongo(),"Calendar","user");
    }
 
}
