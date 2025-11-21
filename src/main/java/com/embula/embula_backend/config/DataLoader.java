package com.embula.embula_backend.config;

import com.embula.embula_backend.entity.RestaurantTable;
import com.embula.embula_backend.repository.RestaurantTableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final RestaurantTableRepository tableRepository;

    public DataLoader(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (tableRepository.count() == 0) {
            RestaurantTable table1 = new RestaurantTable();
            table1.setTableNumber("A1");
            table1.setCapacity(4);
            table1.setLocation("Indoors");
            table1.setActive(true);

            RestaurantTable table2 = new RestaurantTable();
            table2.setTableNumber("A2");
            table2.setCapacity(2);
            table2.setLocation("Indoors");
            table2.setActive(true);

            RestaurantTable table3 = new RestaurantTable();
            table3.setTableNumber("B1");
            table3.setCapacity(6);
            table3.setLocation("Patio");
            table3.setActive(true);

            RestaurantTable table4 = new RestaurantTable();
            table4.setTableNumber("C1");
            table4.setCapacity(4);
            table4.setLocation("Window");
            table4.setActive(true);

            tableRepository.saveAll(Arrays.asList(table1, table2, table3, table4));
            System.out.println("Sample restaurant tables loaded into the database.");
        }
    }
}

