package ch.zhaw.mdm.energy.classification;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.ArrayDataset;
import java.util.List;

public class EnergyDatasetBuilder {

    public static ArrayDataset buildDataset(List<EnergyRecord> records, NDManager manager) {
        int numRecords = records.size();
        float[][] featureData = new float[numRecords][1]; // nur quantityMj
        float[][] labelData = new float[numRecords][2];   // fossil oder green, one-hot

        for (int i = 0; i < numRecords; i++) {
            EnergyRecord record = records.get(i);
            featureData[i][0] = record.getQuantityMj();

            if (record.isGreenEnergy()) {
                labelData[i][1] = 1.0f; // green
            } else {
                labelData[i][0] = 1.0f; // fossil
            }
        }

        NDArray features = manager.create(featureData);
        NDArray labels = manager.create(labelData);

        return new ArrayDataset.Builder()
                .setData(features)
                .optLabels(labels)
                .setSampling(32, true) // batch size 32, shuffle true
                .build();
    }
}
