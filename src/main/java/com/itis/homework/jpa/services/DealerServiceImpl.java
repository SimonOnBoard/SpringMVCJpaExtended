package com.itis.homework.jpa.services;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itis.homework.jpa.dto.SaleInformationDto;
import com.itis.homework.jpa.orm.Car;
import com.itis.homework.jpa.orm.Dealership;
import com.itis.homework.jpa.repositories.CarRepository;
import com.itis.homework.jpa.repositories.DealershipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;

@Component
@Slf4j
public class DealerServiceImpl implements DealerService {

    private final static String FILES_PATH = "/Users/mainuser/Desktop/files";
    private final static String DATASET_FILES_PATH = "/Users/mainuser/Desktop/dataset";
    private final static String CATALOG_FILES_PATH = "/Users/mainuser/Desktop/catalog";

    @Autowired
    private DealershipRepository dealershipRepository;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private CarRepository carRepository;

    @Override
    public void init(Long id) {
        Dealership dealership = dealershipRepository.findById(id).orElseThrow(IllegalStateException::new);
        try (Stream<Path> filesPaths = Files.walk(Paths.get(FILES_PATH))) {
            filesPaths.filter(filePath -> filePath.toFile().isFile()).forEach(
                    filePath -> {
                        File file = filePath.toFile();
                        Car car = readerService.readCarInfo(file);
                        car.setDealer(dealership);
                        car.setPath(filePath.toString());
                        carRepository.save(car);
                    }
            );
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public SaleInformationDto getInformation(Long dealerId, String type, String start, String end) {
        Pair<LocalDateTime, LocalDateTime> pair = getCurrentDates(start, end);
        return dealershipRepository.getCurrentSailInfo(dealerId, type, pair.getFirst(), pair.getSecond()).orElse(new SaleInformationDto(dealerId +"", 0d, 0L));
    }

    private Pair<LocalDateTime, LocalDateTime> getCurrentDates(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Long time = (formatter.parse(start).getTime());
            Long time1 = (formatter.parse(end).getTime());
            LocalDateTime realStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(time),
                    TimeZone.getDefault().toZoneId());
            LocalDateTime realEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(time1),
                    TimeZone.getDefault().toZoneId());
            return Pair.of(realStart, realEnd);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void createDataSet(Long id, String format, String start, String end) {
        try {
            Pair<LocalDateTime, LocalDateTime> pair = getCurrentDates(start, end);
            List<Car> cars = carRepository.findAllByDealerAndCreatedAtBetween(dealershipRepository.findById(id).orElseThrow(IllegalAccessError::new), pair.getFirst(), pair.getSecond());
            switch (format) {
                case "pdf":
                    makePfdDataset(cars, new com.itextpdf.text.Document(), start, end, id);
                    break;
                default:
                    throw new IllegalArgumentException("Method not found");
            }
        } catch (DocumentException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Transactional
    @Override
    public void createActualCatalog(Long id, String format, String type) {
        String name = CATALOG_FILES_PATH + File.separator + LocalDateTime.now() + "-" + id + "." + format;
        Dealership dealership = dealershipRepository.findById(id).orElseThrow(IllegalAccessError::new);
        List<Car> toPrint;
        switch (type) {
            case "car":
                toPrint = dealership.getCarList();
                break;
            case "truck":
                toPrint = dealership.getTruckList();
                break;
            default:
                throw new IllegalStateException("No option found");
        }
        try {
            switch (format) {
                case "pdf":
                    makePfdCatalog(toPrint, new com.itextpdf.text.Document(), name);
                    break;
                default:
                    throw new IllegalArgumentException("Method not found");
            }
        } catch (FileNotFoundException | DocumentException e) {
            throw new IllegalStateException(e);
        }

    }

    @Transactional
    @Override
    public void makeDeal(Long carId, Double price) {
        Car car = carRepository.findById(carId).orElseThrow(IllegalStateException::new);
        car.setSold(true);
        car.getOffer().setPrice(price);
    }

    private void makePfdCatalog(List<Car> toPrint, com.itextpdf.text.Document document, String name) throws FileNotFoundException, DocumentException {
        PdfWriter.getInstance(document, new FileOutputStream(name));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk;
        DottedLineSeparator separator = new DottedLineSeparator();
        separator.setPercentage(59500f / 523f);
        Chunk linebreak = new Chunk(separator);
        for (Car car : toPrint) {
            car.setCatalogName(name);
            chunk = new Chunk(car.getOffer().toString(), font);
            document.add(new Paragraph(chunk));
            document.add(linebreak);
        }
        document.close();
    }

    private void makePfdDataset(List<Car> cars, com.itextpdf.text.Document document, String start, String end, Long id) throws IOException, DocumentException {
        PdfWriter.getInstance(document, new FileOutputStream(DATASET_FILES_PATH + File.separator + start + "-" + end + "-" + id + ".pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk;
        DottedLineSeparator separator = new DottedLineSeparator();
        separator.setPercentage(59500f / 523f);
        Chunk linebreak = new Chunk(separator);
        for (Car car : cars) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(car.getSourceFile()));
            String line = bufferedReader.readLine();
            while (line != null) {
                chunk = new Chunk(line, font);
                document.add(new Paragraph(chunk));
                line = bufferedReader.readLine();
            }
            document.add(linebreak);
        }
        document.close();
    }
}
