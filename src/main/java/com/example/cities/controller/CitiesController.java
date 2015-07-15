package com.example.cities.controller;

import java.util.Calendar;

import com.example.cities.client.CityRepository;
import com.example.cities.client.model.PagedCities;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableCircuitBreaker
@RequestMapping("/cities")
public class CitiesController {
    private CityRepository repository;

    @Autowired
    public CitiesController(CityRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PagedCities list(Pageable pageable) {
        return repository.findAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public PagedCities search(@RequestParam("name") String name, Pageable pageable) {
        return repository.findByNameContains(name, pageable.getPageNumber(), pageable.getPageSize());
    }
    
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public String version() {
        return System.getenv("VERSION");
    }
    
    @Autowired
    private FlakeyService service;

    @RequestMapping("/cbtest")
    public String hello() {
        return service.hello();
    }

    @Component
    public static class FlakeyService {

        @HystrixCommand(fallbackMethod="goodbye")
        public String hello() {
            if (Calendar.getInstance().get(Calendar.MINUTE) % 2 == 0) {
                throw new RuntimeException();
            }
            return "hello!";
        }

        String goodbye() {
            return "goodbye.";
        }

    }
}
