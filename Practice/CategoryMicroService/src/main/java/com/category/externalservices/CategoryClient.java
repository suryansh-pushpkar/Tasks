package com.category.externalservices;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.category.dto.CategoryDTO;

@FeignClient(name = "category-service", url = "https://dummyjson.com/products/categories")
public interface CategoryClient {
	@GetMapping
	List<CategoryDTO> getCategories();
}
