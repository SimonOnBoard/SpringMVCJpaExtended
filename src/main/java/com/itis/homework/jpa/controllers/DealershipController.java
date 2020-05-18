
package com.itis.homework.jpa.controllers;

import com.itis.homework.jpa.dto.SaleInformationDto;
import com.itis.homework.jpa.services.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DealershipController {

    @Autowired
    private DealerService dealerFileService;

    @GetMapping("/files/init/{dealer-id}")
    public ResponseEntity<?> init(@PathVariable("dealer-id") Long id) {
        dealerFileService.init(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dataset/{dealer-id}/{format}/{start}/{end}")
    public ResponseEntity<?> convert(@PathVariable(value = "dealer-id", required = false) Long id, @PathVariable(value = "format", required = false) String format,
                                     @PathVariable(value = "start", required = false) String start, @PathVariable(value = "end", required = false) String end) {
        dealerFileService.createDataSet(id, format, start, end);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/createCatalog/{dealer-id}/{car-type}/{format}")
    public ResponseEntity<?> convertPngByUser(@PathVariable(value = "dealer-id", required = false) Long id, @PathVariable(value = "format", required = false) String format,
                                              @PathVariable(value = "car-type", required = false) String type) {
        dealerFileService.createActualCatalog(id, format, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getInfo/{dealer-id}/{car-type}/{start}/{end}")
    public ResponseEntity<SaleInformationDto> getInformation(@PathVariable(value = "car-type", required = false) String type, @PathVariable(value = "dealer-id", required = false) Long id,
                                                             @PathVariable(value = "start", required = false) String start, @PathVariable(value = "end", required = false) String end) {
        SaleInformationDto result = dealerFileService.getInformation(id, type, start, end);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sell/{car-id}/{price}")
    public ResponseEntity<?> sellCar(@PathVariable(value = "car-id", required = false) Long carId,
                                     @PathVariable(value = "price", required = false) Double price) {
        dealerFileService.makeDeal(carId, price);
        return ResponseEntity.ok().build();
    }

}
