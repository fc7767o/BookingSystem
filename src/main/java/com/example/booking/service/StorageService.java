package com.example.booking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    private final ObjectMapper mapper;
    private static final String DATA_DIR = "data/";

    public StorageService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Создана папка: " + dir.getAbsolutePath());
        }
    }

    public <T> List<T> loadList(String fileName, Class<T> clazz) {
        File file = new File(DATA_DIR + fileName);
        if (!file.exists()) {
            System.out.println("Файл не найден, создаём пустой: " + fileName);
            return new ArrayList<>();
        }

        try {
            List<T> data = mapper.readValue(file,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
            System.out.println("Загружено из " + fileName + ": " + data.size() + " записей");
            return data;
        } catch (IOException e) {
            System.err.println("Ошибка загрузки " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public <T> void saveList(String fileName, List<T> data) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(DATA_DIR + fileName), data);
            System.out.println("Сохранено в " + fileName + ": " + data.size() + " записей");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения " + fileName + ": " + e.getMessage());
        }
    }
}