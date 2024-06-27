package com.ecommerce.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.service.CategoryService;

import org.springframework.ui.Model;




@Controller
public class AminController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/admin")
    public String adminhome() {
        return "adminhome";
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }
    
    @GetMapping("/admin/categories/add")
    public String getCategoriesAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    

    @PostMapping("/admin/categories/add")
    public String postCategoriesAdd(@ModelAttribute("category") Category category) {
         categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    
}
