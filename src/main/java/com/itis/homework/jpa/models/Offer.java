package com.itis.homework.jpa.models;

import com.itis.homework.jpa.orm.Car;
import com.itis.homework.jpa.orm.Dealership;
import lombok.Data;

@Data
public class Offer {
    //Should be a real offer with custom cool implementation but now)
    public Offer(Dealership dealer, Car car, boolean sold) {
        this.description = dealer.toString() + "\n" + "offer: " + car.toString();
        this.price = car.getCost();
        this.sold = sold;
    }
    private Double price;
    private String description;
    private boolean sold;

    @Override
    public String toString() {
        return "Offer{" +
                "price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
