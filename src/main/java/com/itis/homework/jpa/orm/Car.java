package com.itis.homework.jpa.orm;

import com.itis.homework.jpa.models.Offer;
import lombok.Data;
import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;

@Entity
@Data
public class Car {
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", catalogName='" + catalogName + '\'' +
                ", color='" + color + '\'' +
                ", cost=" + cost +
                ", type='" + type + '\'' +
                ", ownerType='" + ownerType + '\'' +
                ", pathToImageDirectory='" + pathToImageDirectory + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String catalogName;

    private String color;

    private Double cost;

    private String type;

    private String ownerType;

    private String pathToImageDirectory;

    private String path;

    private boolean sold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer")
    private Dealership dealer;


    @Transient
    private Offer offer;

    @Transient
    private File sourceFile;

    private LocalDateTime createdAt;

    private LocalDateTime saleDate;

    @PostLoad
    public void makeOffer() {
        sourceFile = new File(path);
        offer = new Offer(dealer, this, sold);
    }

    @PreUpdate
    public void checkSold() {
        if (this.isSold()) {
            this.setSaleDate(LocalDateTime.now());
        }
    }

}
