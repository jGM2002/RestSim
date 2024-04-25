/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class LicenseManager {
    private static final String LICENSES_DIRECTORY = Paths.get("src", "sources").toString();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String loadAndValidateLicense(String licenseFileName) {
        StringBuilder result = new StringBuilder();

        String filePath = Paths.get(LICENSES_DIRECTORY, licenseFileName).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 2) {
                    result.append("Invalid license file format.");
                    return result.toString();
                }

                String licenseKey = data[0];
                String expirationDateStr = data[1];

                if (isValidLicense(licenseKey, expirationDateStr)) {
                    result.append("License is valid.");
                } else {
                    result.append("License is expired or invalid.");
                }
            }
        } catch (IOException e) {
            result.append("Error reading license file: ").append(e.getMessage());
        }

        return result.toString();
    }

    private static boolean isValidLicense(String licenseKey, String expirationDateStr) {
        try {
            Date expirationDate = DATE_FORMAT.parse(expirationDateStr);
            Date currentDate = new Date();

            if (expirationDate.before(currentDate)) {
                return false; // License expired
            }

            // Check if the license key is present in the list of valid licenses
            String validLicensesFilePath = Paths.get(LICENSES_DIRECTORY, "llicenciesValides.csv").toString();
            try (BufferedReader reader = new BufferedReader(new FileReader(validLicensesFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals(licenseKey.trim())) {
                        return true; // License key found in the list of valid licenses
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading valid licenses file: " + e.getMessage());
            }

            return false; // License key not found in the list of valid licenses
        } catch (ParseException e) {
            System.err.println("Error parsing expiration date: " + e.getMessage());
            return false;
        }
    }
}
