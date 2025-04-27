package ch.zhaw.mdm.energy.classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EnergyDatasetLoader {

    public static List<EnergyRecord> loadDataset(String csvPath) throws IOException {
        List<EnergyRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(Paths.get(csvPath).toFile()))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String country = parts[0].trim();
                String commodity = parts[1].trim();
                String transactionType = parts[2].trim();
                float quantityMj = Float.parseFloat(parts[3]);
                boolean isGreenEnergy = Boolean.parseBoolean(parts[4].trim());

                records.add(new EnergyRecord(country, commodity, transactionType, quantityMj, isGreenEnergy));
            }
        }

        return records;
    }
}
