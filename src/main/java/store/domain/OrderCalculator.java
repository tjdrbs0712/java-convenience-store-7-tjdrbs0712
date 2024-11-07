package store.domain;

import store.validation.ProductValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderCalculator {
    private final List<Product> products;
    private List<Product> purchaseProducts = new ArrayList<>();

    public OrderCalculator(List<Product> products){
        this.products = products;
    }

    public void addPurchaseProduct(Map<String, Integer> productDetails){
        for (String productName : productDetails.keySet()) {
            ProductValidator.validateProductContains(products, productName);

            purchaseProducts = products.stream()
                    .filter(product -> product.getName().equals(productName))
                    .toList();
        }
    }

    public void asd(Map<String, Integer> productDetails){

//        List<String> productNames = new ArrayList<>(productDetails.keySet());
//        List<Integer> prices = new ArrayList<>(productDetails.values());

//        for(int i=0; i<productNames.size(); i++){
//            int finalI = i;
//            purchaseProducts = products.stream()
//                    .filter(product -> product.getName().equals(productNames.get(finalI)))
//                    .toList();
//            if(purchaseProducts.size() >= 2){
//                for(Product product : purchaseProducts){
//                    if(product.getPromotion() != null){
//
//                    }
//                }
//            }
//        }

//        for(String productName : productDetails.keySet()){
//            purchaseProducts = products.stream()
//                    .filter(product -> product.getName().equals(productName))
//                    .toList();
//            if(purchaseProducts.size() >= 2){
//                for(Product product : purchaseProducts){
//                    if(product.getPromotion() != null){
//
//                    }
//                }
//            }
//        }
    }

}
