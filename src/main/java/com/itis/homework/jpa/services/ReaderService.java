package com.itis.homework.jpa.services;

import com.itis.homework.jpa.orm.Car;

import java.io.File;

public interface ReaderService {
    Car readCarInfo(File file);
}
