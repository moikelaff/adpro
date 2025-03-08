package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Item {
    protected String id;
    protected String name;
    protected int quantity;
}