package ch.zhaw.mdm.energy.classification;

import ai.djl.Model;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.metric.Metrics;
import ai.djl.ndarray.NDManager;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.loss.Loss;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.EasyTrain;
import ai.djl.training.dataset.Batch;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

public class TrainerService {

    public static void trainModel(String csvPath, String modelDir) throws IOException, TranslateException {
        try (NDManager manager = NDManager.newBaseManager()) {
            var records = EnergyDatasetLoader.loadDataset(csvPath);
            ArrayDataset dataset = EnergyDatasetBuilder.buildDataset(records, manager);

            try (Model model = Model.newInstance("energy-mix-classifier")) {
                // Input size: 1 (quantity_MJ), Output size: 2 (fossil, green)
                model.setBlock(new Mlp(1, 2, new int[]{10}));

                DefaultTrainingConfig config = setupTrainingConfig();

                try (Trainer trainer = model.newTrainer(config)) {
                    trainer.setMetrics(new Metrics());
                    trainer.initialize(new ai.djl.ndarray.types.Shape(1, 1));

                    for (int epoch = 0; epoch < 10; epoch++) {
                        for (Batch batch : trainer.iterateDataset(dataset)) {
                            try (Batch b = batch) {
                                EasyTrain.trainBatch(trainer, b);
                                trainer.step();
                            }
                        }
                        trainer.notifyListeners(listener -> listener.onEpoch(trainer));
                    }

                    TrainingResult result = trainer.getTrainingResult();
                    System.out.println("Final accuracy: " + result.getValidateEvaluation("Accuracy"));
                    System.out.println("Final loss: " + result.getValidateLoss());

                    model.setProperty("Accuracy", String.format("%.5f", result.getValidateEvaluation("Accuracy")));
                    model.save(Paths.get(modelDir), "energy-mix-classifier");
                }
            }
        }
    }

    private static DefaultTrainingConfig setupTrainingConfig() {
        Tracker lrt = Tracker.fixed(0.01f);
        Optimizer sgd = Optimizer.sgd().setLearningRateTracker(lrt).build();
        return new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .optOptimizer(sgd)
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());
    }
}
