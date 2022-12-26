package org.example;

import org.example.data.Asset;
import org.example.util.ExportFileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        try {
            Collection<Asset> assets = ExportFileReader.readExportFile("C:/Users/user/Downloads/export_experiment.xlsx");
            assets.forEach((Asset a) -> System.out.println(a.toString()));

        } catch (FileNotFoundException e) {
            System.out.println("Input file was not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Workbook could not be initialized!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}