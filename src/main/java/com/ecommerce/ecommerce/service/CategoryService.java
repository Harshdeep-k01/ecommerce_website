package com.ecommerce.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repo;
    public List<Category> getAllCategory(){
        return repo.findAll();
    }

    public void addCategory(Category category){
        repo.save(category);
    }
}
