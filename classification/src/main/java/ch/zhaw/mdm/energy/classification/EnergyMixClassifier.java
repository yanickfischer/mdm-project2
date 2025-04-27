package ch.zhaw.mdm.energy.classification;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.translate.TranslateException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EnergyMixClassifier {

    private static final String[] LABELS = { "green", "neutral", "fossil" };

    public static ArrayDataset loadDataset(NDManager manager, String csvPath) throws IOException, TranslateException {
        List<float[]> features = new ArrayList<>();
        List<float[]> labels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(Paths.get(csvPath).toFile()))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                float renewable = Float.parseFloat(parts[1]);
                float coal = Float.parseFloat(parts[2]);
                float gas = Float.parseFloat(parts[3]);
                String labelStr = parts[4].trim();

                float[] feature = new float[] { renewable, coal, gas };
                float[] label = new float[LABELS.length];

                for (int i = 0; i < LABELS.length; i++) {
                    if (LABELS[i].equalsIgnoreCase(labelStr)) {
                        label[i] = 1.0f;
                        break;
                    }
                }

                features.add(feature);
                labels.add(label);
            }
        }

        NDArray featureNd = manager.create(features.toArray(new float[0][0]));
        NDArray labelNd = manager.create(labels.toArray(new float[0][0]));

        return new ArrayDataset.Builder()
                .setData(featureNd)
                .optLabels(labelNd)
                .setSampling(1, true)
                .build();
    }

    public static String[] getLabels() {
        return LABELS;
    }
}
