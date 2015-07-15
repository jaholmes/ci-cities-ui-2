package com.example.cities.client;

import com.example.cities.client.model.PagedCities;

import feign.Param;
import feign.RequestLine;

import org.springframework.stereotype.Repository;

import javax.inject.Named;

@Repository
public interface CityRepository {
    @RequestLine("GET /cities?page={page}&size={size}")
    public PagedCities findAll(@Param("page") Integer page, @Param("size") Integer size);

    @RequestLine("GET /cities/search/nameContains?q={name}&page={page}&size={size}")
    public PagedCities findByNameContains(@Param("name") String name, @Param("page") Integer page, @Param("size") Integer size);
}
