package com.category.externalservices;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCTMICROSERVICE")
public interface ProductMicroserviceClient {
	@GetMapping("/category")
	List<Object> getProductByCategoryName(@RequestParam String category);

}
