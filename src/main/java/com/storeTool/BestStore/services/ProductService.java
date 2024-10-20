package com.storeTool.BestStore.services;

import com.storeTool.BestStore.controllers.ProductsController;
import com.storeTool.BestStore.converters.ProductConverter;
import com.storeTool.BestStore.dao.ProductsRepository;
import com.storeTool.BestStore.models.Product;
import com.storeTool.BestStore.models.ProductDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private static final Log LOG = LogFactory.getLog(ProductsController.class);
    @Autowired
    ProductConverter productConverter;
    @Autowired
    ProductsRepository productsRepository;

    public Product addNewProduct(ProductDto productDto) {
        MultipartFile multipartFile = productDto.getImageFile();
        Date createDate = new Date();
        String storageFileNme = createDate.getTime() + "_" + multipartFile.getOriginalFilename();
        final String fileDirectory = "public/images/";
        Path uploadPath = Paths.get(fileDirectory);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        try {
            InputStream inputStream = multipartFile.getInputStream();
            Files.copy(inputStream, Paths.get(fileDirectory+ storageFileNme), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOG.error(e);
        }
        Product product = productConverter.convert(productDto);
        if (product != null) {
            product.setCreatedAt(createDate);
            product.setImageFileName(storageFileNme);
            productsRepository.save(product);
        }
        return product;
    }

    public List<Product> findAllProducts(Sort productId) {
        return productsRepository.findAll(productId);
    }
}
