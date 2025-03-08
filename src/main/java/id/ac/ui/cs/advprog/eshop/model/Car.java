package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Car extends Item {
    private String carColor;
    
    public String getCarColor() {
        return carColor;
    }
    public String getCarId() {
        return super.id;
    }
    
    public void setCarId(String id) {
        super.id = id;
    }
    
    public void setCarColor(String color) {
        carColor = color;
    }

    public String getCarName() {
        return super.name;
    }
    
    public void setCarName(String name) {
        super.name = name;
    }
    
    public int getCarQuantity() {
        return super.quantity;
    }
    
    public void setCarQuantity(int quantity) {
        super.quantity = quantity;
    }
}