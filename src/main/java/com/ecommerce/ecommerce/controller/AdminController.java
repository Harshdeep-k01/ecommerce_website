package com.ecommerce.ecommerce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.ecommerce.dto.ProductDTO;
import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.service.CategoryService;
import com.ecommerce.ecommerce.service.ProductService;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
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

    @GetMapping("/admin/categories/delete/{id}") 
    public String deleteCategory(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }
    
    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if(category.isPresent()){
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        }else{
            return "404";
        }
        
    }

    //product-section
    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String productAddGet(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO, 
                             @RequestParam("productImage") MultipartFile file, 
                             @RequestParam("imgName") String imgName) throws IOException {
    Product product = new Product();
    product.setId(productDTO.getId());
    product.setName(productDTO.getName());
    product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
    product.setPrice(productDTO.getPrice());
    product.setWeight(productDTO.getWeight());
    product.setDescription(productDTO.getDescription());

    String imageUUId;
        if (!file.isEmpty()) {
            imageUUId = file.getOriginalFilename();

            // Ensure the upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path fileNameAndPath = uploadPath.resolve(imageUUId);

            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                // Return an error page or message here
                return "redirect:/admin/products?error";
            }
        } else {
            imageUUId = imgName;
        }

        product.setImageName(imageUUId);
        productService.addProduct(product);

        return "redirect:/admin/products";
}

    

    //delete
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    //update
    @GetMapping("/admin/products/update/{id}")
public String updateProduct(@PathVariable long id, Model model) {
    Optional<Product> optionalProduct = productService.getProductById(id);
    if (!optionalProduct.isPresent()) {
        return "404"; // Or handle not found scenario
    }
    Product product = optionalProduct.get();
    ProductDTO productDTO = new ProductDTO();
    productDTO.setId(product.getId());
    // Map other fields from Product to ProductDTO
    model.addAttribute("productDTO", productDTO);
    model.addAttribute("categories", categoryService.getAllCategory());
    return "productsAdd"; // Return your update form view
}

    @PostMapping("/admin/products/update")
    public String postUpdateProduct(@ModelAttribute("productDTO") ProductDTO productDTO, 
                                    @RequestParam("productImage") MultipartFile file, 
                                    @RequestParam("imgName") String imgName) throws IOException {
        // Handling product update with file upload
        String imageUUId = null;
if (!file.isEmpty()) {
    imageUUId = file.getOriginalFilename();

    // Ensure the upload directory exists
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
    }

    // Save the file to the upload directory
    Path fileNameAndPath = uploadPath.resolve(imageUUId);
    Files.write(fileNameAndPath, file.getBytes());
} else {
    imageUUId = imgName; // Use existing image name if no new file is uploaded
}

        return "redirect:/admin/products";
    }
    
    

    
}
