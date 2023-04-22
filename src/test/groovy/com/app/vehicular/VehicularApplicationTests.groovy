package com.app.vehicular

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation)
class VehicularApplicationTests {

    @Test
    @Order(1)
    void fileExists() {
        def file = new File("src/main/resources/data/people.xlsx")
        assert file.exists()
    }

    @Test
    @Order(2)
    void fileHasData() {
        def file = new File("src/main/resources/data/people.xlsx")
        assert file.length() > 0
    }

    @Test
    @Order(3)
    void canReadFirstRow() {
        def file = new File("src/main/resources/data/people.xlsx")
        def workbook = new XSSFWorkbook(file)
        def sheet = workbook.getSheetAt(0)
        def row = sheet.getRow(0)

        assert row.getCell(0).stringCellValue == "first_name"
        assert row.getCell(1).stringCellValue == "last_name"
        assert row.getCell(2).stringCellValue == "email"
        assert row.getCell(3).stringCellValue == "age"
        assert row.getCell(4).stringCellValue == "gender"
        assert row.getCell(5).stringCellValue == "title"
        assert row.getCell(6).stringCellValue == "username"
    }

    @Test
    @Order(4)
    void canReadSecondRow() {
        def file = new File("src/main/resources/data/people.xlsx")
        def workbook = new XSSFWorkbook(file)
        def sheet = workbook.getSheetAt(0)
        def row = sheet.getRow(1)

        assert row.getCell(0).stringCellValue == "Sara"
        assert row.getCell(1).stringCellValue == "Kelly"
        assert row.getCell(2).stringCellValue == "sara76@rocketmail.com"
        assert row.getCell(3).numericCellValue == 20
        assert row.getCell(4).stringCellValue == "F"
        assert row.getCell(5).stringCellValue == "Ms."
        assert row.getCell(6).stringCellValue == "woodenwheel"
    }
}
