package com.itis.homework.jpa.repositories;

import com.itis.homework.jpa.orm.Car;
import com.itis.homework.jpa.orm.Dealership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findAllByDealerAndCreatedAtBetween(Dealership dealership, LocalDateTime start, LocalDateTime end);
}
