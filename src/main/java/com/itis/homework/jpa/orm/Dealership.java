package com.itis.homework.jpa.orm;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Dealership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "dealer", fetch = FetchType.LAZY)
    @Where(clause = "type like '%car%' and sold = false")
    private List<Car> carList;

    @OneToMany(mappedBy = "dealer", fetch = FetchType.LAZY)
    @Where(clause = "type like '%truck%' and sold = false")
    private List<Car> truckList;

    @OneToMany(mappedBy = "dealer", fetch = FetchType.LAZY)
    private List<Car> cars;

    @Override
    public String toString() {
        return "Dealership{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
