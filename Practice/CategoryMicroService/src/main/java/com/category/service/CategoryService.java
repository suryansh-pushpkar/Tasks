package com.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.category.entity.Category;
import com.category.excepton.ResourceNotFoundException;
import com.category.repo.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepo;
  public CategoryService(CategoryRepository categoryRepo) {
	  this.categoryRepo = categoryRepo;
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
}