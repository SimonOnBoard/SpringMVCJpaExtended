package com.itis.homework.jpa.services;

import com.itis.homework.jpa.orm.Car;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ReaderServiceImpl implements ReaderService {
    @Override
    public Car readCarInfo(File file) {
        Car car = new Car();
        car.setCreatedAt(LocalDateTime.now());
        for(int i = 0; i < 7; i++){
            try {
                String line = FileUtils.readLines(file).get(i);
                switch (i){
                    case 0:
                        car.setName(line.split(":")[1]);
                        break;
                    case 1:
                        car.setCost(Double.parseDouble(line.split(":")[1]));
                        break;
                    case 2:
                        car.setColor(line.split(":")[1]);
                        break;
                    case 3:
                        car.setType(line.split(":")[1]);
                        break;
                    case 4:
                        String res = line.split(":")[1].equals("c") ? "company" : "person";
                        car.setOwnerType(res);
                        break;
                    case 5:
                        car.setPathToImageDirectory(line.split(":")[1]);
                        break;
                    case 6:
                        boolean sold = Boolean.parseBoolean(line.split(":")[1]);
                        car.setSold(sold);
                        if(sold){
                            line = FileUtils.readLines(file).get(7);
                            car.setSaleDate(LocalDateTime.parse(line));
                        }
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

        }
        return car;
    }
}
