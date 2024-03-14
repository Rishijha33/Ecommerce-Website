package com.microservices.productservice.service;


import com.microservices.productservice.dto.GenericProductDto;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.model.SortParam;
import com.microservices.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.naming.ldap.PagedResultsControl;
import java.util.List;
@Slf4j
@Service
public class SearchService {
    private ProductRepository productRepository;
    public SearchService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<GenericProductDto> searchProducts(String query, int pageNumber, int pageSize, List<SortParam> sortParams){
//        Sort sort = Sort.by("name").ascending()
//                .and(Sort.by("price").ascending());

        // using the list of sort params to create our sort object
        Sort sort = null;
        Pageable pageRequest = null;
        // Checking for null conditions
        if(sortParams != null){
            // Sorting the initial param

            if(sortParams.get(0).getSortType().equals("ASC")){
                sort.by(sortParams.get(0).getSortParamName()).ascending();
            }
            else {
                sort.by(sortParams.get(0).getSortParamName()).descending();
            }

            // Using a loop to parse and check using the sortParams object to store the property and sorting order - ASC/DESC
            for(int i = 1;i<sortParams.size();i++){
                if(sortParams.get(i).getSortType().equals("ASC")){
                    sort.and(sort.by(sortParams.get(i).getSortParamName()).ascending());
                }
                else {
                    sort.and(sort.by(sortParams.get(i).getSortParamName()).descending());
                }
            }

            // Passing the sort object to pageRequest
            pageRequest = PageRequest.of(pageNumber-1, pageSize, sort);
        }

        else {
            // If no filter/sorting required the use this pageRequest object
            pageRequest = PageRequest.of(pageNumber-1, pageSize);
        }



        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(query, pageRequest);
        log.info(query+" "+pageNumber+" "+pageSize);
        System.out.println(products);
        List<GenericProductDto> genericProductDtos = products.stream().map(this::mapToGenericProductDto).toList();
        System.out.println(genericProductDtos);
        log.info("Got the items from service");
        return genericProductDtos;
    }

    public GenericProductDto mapToGenericProductDto(Product product){
        GenericProductDto genericProductDto = new GenericProductDto();
        genericProductDto.setId(product.getId());
        genericProductDto.setName(product.getName());
        genericProductDto.setPrice(product.getPrice());
        genericProductDto.setDescription(product.getDescription());

        return genericProductDto;
    }

}
