package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product extends Item {
  
    @Override
    public String getId() {
        return super.id;
    }
    
    @Override
    public void setId(String id) {
        super.id = id;
    }
    
    public String getProductId() {
        return getId();
    }
    
    public void setProductId(String id) {
        setId(id);
    }
    
    public String getProductName() {
        return super.name;
    }
    
    public void setProductName(String name) {
        super.name = name;
    }
    
    public int getProductQuantity() {
        return super.quantity;
    }
    
    public void setProductQuantity(int quantity) {
        super.quantity = quantity;
    }
}