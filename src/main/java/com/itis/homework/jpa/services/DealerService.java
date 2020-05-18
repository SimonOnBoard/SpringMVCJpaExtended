package com.itis.homework.jpa.services;

import com.itis.homework.jpa.dto.SaleInformationDto;

public interface DealerService {
    void init(Long id);

    SaleInformationDto getInformation(Long userId, String type, String start, String end);

    void createDataSet(Long id, String format, String start, String end);

    void createActualCatalog(Long id, String format, String type);

    void makeDeal(Long carId, Double price);
}
