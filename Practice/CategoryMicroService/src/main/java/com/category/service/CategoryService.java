package com.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.category.dto.CategoryDTO;
import com.category.entity.Category;
import com.category.excepton.ResourceNotFoundException;
import com.category.externalservices.CategoryClient;
import com.category.externalservices.ProductMicroserviceClient;
import com.category.repo.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepo;
  private final CategoryClient categoryClient;
  private final ProductMicroserviceClient productClient;
  @Autowired
  public CategoryService(CategoryRepository categoryRepo, CategoryClient categoryClient, ProductMicroserviceClient productClient) {
	  this.productClient = productClient;
	  this.categoryRepo = categoryRepo;
	  this.categoryClient = categoryClient;
  }
  
  @Transactional
  public List<Category> saveExtenalCategories(){
	  List<CategoryDTO> list = categoryClient.getCategories();
	  List<Category> categoryList = new ArrayList<>();
	  for(CategoryDTO s : list) {
		  Category c = new Category();
		  c.setCategoryName(s.getName());
		  categoryList.add(c);
	  }
	  List<Category> dbList =  categoryRepo.saveAll(categoryList);
	  return dbList;
  }
  
  
  @Transactional
  public List<Category> saveInBulk(List<String> list){
	  List<Category> categoryList = new ArrayList<>();
	  for(String s : list) {
		  Category c = new Category();
		  c.setCategoryName(s);
		  categoryList.add(c);
	  }
	  List<Category> dbList =  categoryRepo.saveAll(categoryList);
	  return dbList;
  }
  public List<Category> fetchAll(){
	  return categoryRepo.findAll();
  }
  
  public Category fetchById(int id) {
	  return categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Resource not found | Id not found"));
  }
  
  public Category deleteById(int id) {
	Category dbCategory =   categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Resource not found | Id not found"));
    categoryRepo.delete(dbCategory);
    return dbCategory;
  }
  
  public List<Object> getProductByCategoryName(String name){
	  return productClient.getProductByCategoryName(name);
  }
}