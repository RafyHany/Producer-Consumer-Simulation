package producer.producerConsumer.services.models;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private String name ;
    private double x ;
    private double y ;
    private String color ;
    private String text ;
    private String id ;
    public Element(){}
    public Element(ElementDto dto){
        this.name = dto.name ;
        this.x = dto.x ;
        this.y = dto.y ;
        this.color = dto.color ;
        this.id = dto.id ;
        this.text = dto.text ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    private List<Product> products = new ArrayList<>() ;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
