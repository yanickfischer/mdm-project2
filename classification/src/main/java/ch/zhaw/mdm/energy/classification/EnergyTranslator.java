package ch.zhaw.mdm.energy.classification;

import ai.djl.modality.Classifications;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDArray;
import ai.djl.translate.Batchifier;

import java.util.Arrays;
import java.util.List;

public class EnergyTranslator implements Translator<NDList, Classifications> {

    private final List<String> classLabels;

    public EnergyTranslator(String[] labels) {
        this.classLabels = Arrays.asList(labels);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, NDList input) {
        return input;
    }

    @Override
    public Classifications processOutput(TranslatorContext ctx, NDList list) {
        NDArray probabilities = list.singletonOrThrow();
        return new Classifications(classLabels, probabilities);
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}