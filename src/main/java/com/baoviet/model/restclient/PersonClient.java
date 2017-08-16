package com.baoviet.model.restclient;

import com.baoviet.model.Person;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${feign.name}", url = "${feign.url}")
@RequestMapping(value = "/api", consumes = "application/hal+json")
public interface PersonClient {

    @RequestMapping(method = RequestMethod.GET, path = "/people/{id}")
    Person getPerson(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.GET, path = "/people")
    PagedResources<Person> getPersons();

    @RequestMapping(method = RequestMethod.GET, path = "/people")
    PagedResources<Person> getPersons(@RequestParam("page") int page, @RequestParam("size") int size);
}
