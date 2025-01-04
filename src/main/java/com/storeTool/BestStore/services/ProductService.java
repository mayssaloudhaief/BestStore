package com.storeTool.BestStore.services;

import com.storeTool.BestStore.converters.ProductConverter;
import com.storeTool.BestStore.dao.ProductsRepository;
import com.storeTool.BestStore.models.Product;
import com.storeTool.BestStore.models.ProductDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final Log LOG = LogFactory.getLog(ProductService.class);

    @Value("${file.upload.dir}")
    private String fileDirectory;

    private final ProductConverter productConverter;
    private final ProductsRepository productsRepository;

    public ProductService(ProductConverter productConverter, ProductsRepository productsRepository) {
        this.productConverter = productConverter;
        this.productsRepository = productsRepository;
    }

    private String uploadImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String storageFileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        Path uploadPath = Paths.get(fileDirectory);

        Files.createDirectories(uploadPath);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOG.error("Failed to upload image file: " + storageFileName, e);
            throw e;
        }

        return storageFileName;
    }

    @Transactional
    public Product addNewProduct(ProductDto productDto) {
        try {
            String storageFileName = uploadImage(productDto.getImageFile());
            Product product = productConverter.convert(productDto);

            if (product == null) {
                throw new IllegalArgumentException("Product conversion failed");
            }

            product.setCreatedAt(new Date());
            product.setImageFileName(storageFileName);
            return productsRepository.save(product);
        } catch (IOException e) {
            LOG.error("Error saving product: ", e);
            throw new RuntimeException("Failed to add product", e);
        }
    }

    public List<Product> findAllProducts(Sort sort) {
        return productsRepository.findAll(sort);
    }

    public Product findById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Transactional
    public void editProduct(ProductDto productDto, Product product) {
        try {
            if (!productDto.getImageFile().isEmpty()) {
                String newImageFile = uploadImage(productDto.getImageFile());
                deleteImageFile(product.getImageFileName());
                product.setImageFileName(newImageFile);
            }

            product.setProductBrand(productDto.getBrand());
            product.setProductCategory(productDto.getCategory());
            product.setProductPrice(productDto.getPrice());
            product.setProductName(productDto.getName());
            product.setProductDescription(productDto.getDescription());
            productsRepository.save(product);
        } catch (IOException e) {
            LOG.error("Error updating product", e);
            throw new RuntimeException("Failed to edit product", e);
        }
    }

    @Transactional
    public void deleteProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        try {
            productsRepository.delete(product);
            deleteImageFile(product.getImageFileName());
        } catch (IOException e) {
            LOG.error("Error deleting product image: ", e);
        }
    }

    private void deleteImageFile(String fileName) throws IOException {
        if (fileName != null && !fileName.isEmpty()) {
            Path filePath = Paths.get(fileDirectory, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }
    }
}
