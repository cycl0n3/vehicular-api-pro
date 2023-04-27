package com.app.orderapi.runner;

import com.app.orderapi.model.User;
import com.app.orderapi.security.WebSecurityConfig;
import com.app.orderapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userService.findAllUsers().isEmpty()) {
            return;
        }

        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });

        /*try {
            File file = new File("src/main/resources/data/people.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int numberOfRows = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < numberOfRows; i++) {
                XSSFRow row = sheet.getRow(i);

                String first_name = row.getCell(0).getStringCellValue();
                String last_name = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();
                int age = (int) row.getCell(3).getNumericCellValue();
                // String gender = row.getCell(4).getStringCellValue();
                String title = row.getCell(5).getStringCellValue();
                String username = row.getCell(6).getStringCellValue();

                User user = new User(username, "1234", first_name + " " + last_name, email, WebSecurityConfig.USER);

                user.setAge(age);
                user.setTitle(title);
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                userService.saveUser(user);
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        log.info("Database initialized");
    }

    private static final List<User> USERS = Arrays.asList(

        new User("admin", "admin", "Admin", "admin@mycompany.com", WebSecurityConfig.ADMIN, null, "Mr.", 0),
        new User("user", "user", "User", "user@mycompany.com", WebSecurityConfig.USER, null, "Mr.", 0)
    );
}
