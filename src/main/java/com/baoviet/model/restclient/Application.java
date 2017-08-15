package com.baoviet.model.restclient;

import com.baoviet.model.Person;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.hal.Jackson2HalModule;

import java.net.URI;

import static org.springframework.hateoas.client.Hop.rel;

@EnableAutoConfiguration
@EnableFeignClients
@PropertySource("application.properties")
public class Application {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jackson2HalModule());

        return new ResponseEntityDecoder(new JacksonDecoder(mapper));
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        PersonClient client = context.getBean(PersonClient.class);
        System.out.println(client.getPersons());
        System.out.println(client.getPerson(1));


        Traverson traverson = new Traverson(new URI("http://127.0.0.1:7101/baoviet-webapp/api"), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<Resources<Person>> resourceParameterizedTypeReferences =
                new ParameterizedTypeReference<Resources<Person>>() {};

        Resources<Person> itemResources = traverson.//
                follow(rel("people")).//
                toObject(resourceParameterizedTypeReferences);
        System.out.println(itemResources.getContent());

        traverson = new Traverson(new URI("http://127.0.0.1:7101/baoviet-webapp/api/people/9"), MediaTypes.HAL_JSON);
        Person p = traverson.follow(rel("self")).toObject(Person.class);
        System.out.println(p);

    }
}
