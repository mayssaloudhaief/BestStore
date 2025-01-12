package com.storeTool.BestStore.converters;

import com.storeTool.BestStore.models.Product;
import com.storeTool.BestStore.models.ProductDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<ProductDto, Product> {
    @Override
    public Product convert(ProductDto source) {
        if (source == null) return null;
        Product product = new Product();
        product.setProductPrice(source.getPrice());
        product.setProductBrand(source.getBrand());
        product.setProductName(source.getName());
        product.setProductDescription(source.getDescription());
        product.setProductCategory(source.getCategory());
        return product;
    }

    public ProductDto convertConvertToDto(Product source) {
        if (source == null) return null;
        ProductDto productDto = new ProductDto();
        productDto.setPrice(source.getProductPrice());
        productDto.setBrand(source.getProductBrand());
        productDto.setName(source.getProductName());
        productDto.setDescription(source.getProductDescription());
        productDto.setCategory(source.getProductCategory());
        return productDto;
    }


}
