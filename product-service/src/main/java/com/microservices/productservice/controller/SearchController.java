package com.microservices.productservice.controller;

import com.microservices.productservice.dto.GenericProductDto;
import com.microservices.productservice.dto.SearchRequestDto;
import com.microservices.productservice.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.AstPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private SearchService searchService;
    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    @PostMapping
    public Page<GenericProductDto> searchProducts(@RequestBody SearchRequestDto requestDto){
        List<GenericProductDto> searchResponse = searchService.searchProducts(requestDto.getTitle(),
                requestDto.getPageNumber(),
                requestDto.getPageSize(),
                requestDto.getSortParams());
        log.info("Got the items from controller");
        Page<GenericProductDto> page = new PageImpl<>(searchResponse);
        return page;
    }
}
