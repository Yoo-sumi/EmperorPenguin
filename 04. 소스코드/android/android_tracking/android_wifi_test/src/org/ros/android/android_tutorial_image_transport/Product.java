package org.ros.android.android_tutorial_image_transport;

public class Product {
    private int id;
    private String name;
    private int price;
    private int amount;
    private int total;

    public Product(int id, String name, int price, int amount, int total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
