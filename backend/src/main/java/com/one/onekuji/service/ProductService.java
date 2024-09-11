package com.one.onekuji.service;

import com.one.onekuji.eenum.PrizeCategory;
import com.one.onekuji.eenum.ProductType;
import com.one.onekuji.model.Product;
import com.one.onekuji.repository.PrizeNumberMapper;
import com.one.onekuji.repository.ProductDetailRepository;
import com.one.onekuji.repository.ProductRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.ProductReq;
import com.one.onekuji.response.ProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrizeNumberMapper prizeNumberMapper;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public List<ProductRes> getAllProductByType(ProductType productType) {
        return productRepository.getAllProductByType(productType);
    }

    public List<ProductRes> getOneKuJiType(PrizeCategory type) {
        return productRepository.getOneKuJiType(type);
    }

    public ProductRes createProduct(ProductReq productReq) {
        try {
            Product product = new Product();
            convertReqToEntity(productReq, product);
            productRepository.insertProduct(product);
            return convertEntityToRes(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProductRes> getAllProduct() {
        List<Product> products = productRepository.selectAllProducts();
        return products.stream().map(this::convertEntityToRes).collect(Collectors.toList());
    }

    public ProductRes getProductById(Long id) {
        Product product = productRepository.selectProductById(id);
        return product != null ? convertEntityToRes(product) : null;
    }

    public ProductRes updateProduct(Long id, ProductReq productReq) {
        Product product = productRepository.selectProductById(id);
        if (product != null) {
            convertReqToEntity(productReq, product);
            productRepository.updateProduct(product);
            return convertEntityToRes(product);
        }
        return null;
    }

    public boolean deleteProduct(Long id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            return false;
        }
        prizeNumberMapper.deleteProductById(Math.toIntExact(id));
        productDetailRepository.deleteProductDetailByProductId(Math.toIntExact(id));
        productRepository.deleteProduct(id);

        return true;
    }

    private void convertReqToEntity(ProductReq req, Product product) {
        product.setProductName(req.getProductName());
        product.setDescription(escapeTextForHtml(req.getDescription()));
        product.setPrice(BigDecimal.valueOf(req.getPrice()));
        product.setSliverPrice(req.getSliverPrice());
        product.setStockQuantity(0);
        product.setImageUrls(req.getImageUrls());
        product.setProductType(req.getProductType());
        product.setPrizeCategory(req.getPrizeCategory());
        product.setStatus(req.getStatus());
        product.setBonusPrice(req.getBonusPrice());
        product.setHeight(req.getHeight());
        product.setLength(req.getLength());
        product.setWidth(req.getWidth());
        product.setSpecification(escapeTextForHtml(req.getSpecification()));
        product.setSize(req.getHeight().multiply(req.getWidth()).multiply(req.getLength()));
    }


    private ProductRes convertEntityToRes(Product product) {
        return new ProductRes(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getSliverPrice(),
                product.getStockQuantity(),
                product.getImageUrls(),
                product.getProductType(),
                product.getPrizeCategory(),
                product.getStatus(),
                product.getBonusPrice(),
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                product.getSpecification(),
                product.getSize()
        );
    }

    private String escapeTextForHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("\n", "<br/>")
                .replace("\r", "");
    }


}
