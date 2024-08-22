//package com.one.onekuji.service;
//
//import com.one.onekuji.eenum.PrizeCategory;
//import com.one.onekuji.eenum.ProductStatus;
//import com.one.onekuji.eenum.ProductType;
//import com.one.onekuji.model.Product;
//import com.one.onekuji.repository.PrizeNumberMapper;
//import com.one.onekuji.repository.ProductDetailRepository;
//import com.one.onekuji.repository.ProductRepository;
//import com.one.onekuji.repository.UserRepository;
//import com.one.onekuji.request.ProductReq;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PrizeNumberMapper prizeNumberMapper;
//
//    @Autowired
//    private ProductDetailRepository productDetailRepository;
//
//    public List<Product> getAllProduct() {
//        return productRepository.getAllProduct();
//    }
//
//    public Product getProductById(Integer productId) {
//        return productRepository.getProductById(productId);
//    }
//
//    public String createProduct(ProductReq productReq) {
//        try {
//            Product product = new Product();
//            Date date = new Date();
//            product.setProductName(productReq.getProductName());
//            product.setDescription(productReq.getDescription());
//            product.setPrice(Double.valueOf(productReq.getPrice()));
//            product.setStockQuantity(productReq.getStockQuantity());
//            product.setSoldQuantity(productReq.getStockQuantity());
//            product.setImageUrl(productReq.getImageUrl());
//            product.setStartDate(productReq.getStartDate());
//            product.setEndDate(productReq.getEndDate());
//            product.setCreatedAt(date);
//            product.setProductType(productReq.getProductType());
//            if(productReq.getPrizeCategory() != null){
//                product.setPrizeCategory(productReq.getPrizeCategory());
//            }
//            product.setStatus(String.valueOf(ProductStatus.NOT_AVAILABLE_YET));
//            productRepository.createProduct(product);
//            return "1";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "0";
//        }
//    }
//
//    public String updateProduct(Integer productId, ProductReq productReq) {
//        try {
//            Product product = productRepository.getProductById(Math.toIntExact(productReq.getProductId()));
////            User user = userRepository.getUserById(Math.toIntExact(productReq.getUserId()));
//
//            product.setProductName(productReq.getProductName());
//            product.setDescription(productReq.getDescription());
//            product.setPrice(Double.valueOf(productReq.getPrice()));
//            product.setStockQuantity(productReq.getStockQuantity());
//            product.setSoldQuantity(productReq.getStockQuantity());
//            product.setImageUrl(productReq.getImageUrl());
//            product.setStartDate(productReq.getStartDate());
//            product.setEndDate(productReq.getEndDate());
//            product.setUpdatedAt(new Date());
////            product.setUpdateUser(user.getNickname());
//            product.setProductType(productReq.getProductType());
//            if(productReq.getPrizeCategory() != null){
//                product.setPrizeCategory(productReq.getPrizeCategory());
//            }
//            product.setStatus(productReq.getStatus());
//
//            productRepository.updateProduct(product);
//            return "1";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "0";
//        }
//    }
//
//    public String deleteProduct(Integer productId) {
//        try {
//            prizeNumberMapper.deleteProductById(productId);
//            productDetailRepository.deleteProductDetailByProductId(productId);
//            productRepository.deleteProduct(productId);
//            return "1";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "0";
//        }
//    }
//
//    public List<Product> getAllProductByType(ProductType productType) {
//        return productRepository.getAllProductByType(productType);
//    }
//
//    public List<Product> getOneKuJiType(PrizeCategory type) {
//        return productRepository.getOneKuJiType(type);
//    }
//}
