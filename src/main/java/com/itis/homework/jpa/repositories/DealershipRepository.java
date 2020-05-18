package com.itis.homework.jpa.repositories;

import com.itis.homework.jpa.dto.SaleInformationDto;
import com.itis.homework.jpa.orm.Dealership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface DealershipRepository extends JpaRepository<Dealership, Long> {
    //language=jpql
    @Query("select new com.itis.homework.jpa.dto.SaleInformationDto(dealer.name, sum(car.cost), count(car)) from Dealership dealer left join dealer.cars " +
            "as car where dealer.id = :dealerId and car.type like %:type% and car.sold = true and car.createdAt between :start and :end group by dealer.id")
    Optional<SaleInformationDto> getCurrentSailInfo(@Param("dealerId") Long dealerId, @Param("type") String type,
                                                    @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
