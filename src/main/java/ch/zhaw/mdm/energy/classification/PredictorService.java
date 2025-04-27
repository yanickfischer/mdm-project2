package ch.zhaw.mdm.energy.classification;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.nio.file.Paths;

public class PredictorService {

    private Predictor<NDList, Classifications> predictor;
    private NDManager manager;
    private String[] labels;

    public PredictorService(String modelDir) throws IOException, ai.djl.MalformedModelException {
        manager = NDManager.newBaseManager();
        Model model = Model.newInstance("energy-mix-classifier");
        model.load(Paths.get(modelDir), "energy-mix-classifier");

        labels = new String[]{"fossil", "green"};
        predictor = model.newPredictor(new EnergyTranslator(labels));
    }

    public String predict(float quantityMj) throws TranslateException {
        NDArray inputArray = manager.create(new float[] { quantityMj });
        NDList input = new NDList(inputArray);
        Classifications result = predictor.predict(input);

        Classification best = result.best();
        return best.getClassName() + " (" + String.format("%.2f", best.getProbability() * 100) + "%)";
    }
}
