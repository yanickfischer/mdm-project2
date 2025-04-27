package ch.zhaw.mdm.energy.classification;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassificationApplication {

    public static void main(String[] args) throws Exception {
        TrainerService.trainModel("src/main/resources/data/energy_cleaned_europe_final.csv", "models/");

        // Inferenz
        PredictorService predictor = new PredictorService("models/");
        String result = predictor.predict(5000f);
        System.out.println("Prediction: " + result);
    }
}
