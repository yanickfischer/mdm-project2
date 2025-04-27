package ch.zhaw.mdm.energy.classification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassificationApplication {

    public static void main(String[] args) throws Exception {
        TrainerService.trainModel("data/energy_mix_test.csv", "models/");

        // Inferenz
        PredictorService predictor = new PredictorService("models/");
        String result = predictor.predict(0.7f, 0.1f, 0.1f);
        System.out.println("Prediction: " + result);
    }
}
