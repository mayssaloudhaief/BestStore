package com.storeTool.BestStore.controllers;

import com.storeTool.BestStore.models.Product;
import com.storeTool.BestStore.models.ProductDto;
import com.storeTool.BestStore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping(value = "/products")
public class ProductsController {
    @Autowired
    ProductService productService;

    @GetMapping({"", "/"})
    public String showProductsList(Model model) {
        List<Product> products = productService.findAllProducts(Sort.by(Sort.Direction.DESC, "productId"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult bindingResult) {
        if (productDto.getImageFile().isEmpty()) {
            bindingResult.addError(new FieldError("productDto", "imageFile", "The image file is required"));
        }
        if (bindingResult.hasErrors()) {
            return "products/CreateProduct";
        }
        this.productService.addNewProduct(productDto);
        return "redirect:/products";
    }
}
