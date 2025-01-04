package com.storeTool.BestStore.controllers;

import com.storeTool.BestStore.converters.ProductConverter;
import com.storeTool.BestStore.models.Product;
import com.storeTool.BestStore.models.ProductDto;
import com.storeTool.BestStore.services.ProductService;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private static final Log LOG = LogFactory.getLog(ProductsController.class);

    private final ProductService productService;
    private final ProductConverter productConverter;

    public ProductsController(ProductService productService, ProductConverter productConverter) {
        this.productService = productService;
        this.productConverter = productConverter;
    }

    @GetMapping({"", "/"})
    public String showProductsList(Model model) {
        List<Product> products = productService.findAllProducts(Sort.by(Sort.Direction.DESC, "productId"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("productDto", new ProductDto());
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

        productService.addNewProduct(productDto);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }

        ProductDto productDto = productConverter.convertConvertToDto(product);
        model.addAttribute("product", product);
        model.addAttribute("productDto", productDto);
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String editProduct(
            @RequestParam Long id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult bindingResult,
            Model model
    ) {
        Product product = productService.findById(id);

        if (product == null) {
            return "redirect:/products";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            return "products/EditProduct";
        }

        productService.editProduct(productDto, product);
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id) {
        Product product = productService.findById(id);

        if (product == null) {
            LOG.warn("Attempted to delete non-existent product with ID: " + id);
            return "redirect:/products";
        }

        productService.deleteProduct(product);
        return "redirect:/products";
    }
}
