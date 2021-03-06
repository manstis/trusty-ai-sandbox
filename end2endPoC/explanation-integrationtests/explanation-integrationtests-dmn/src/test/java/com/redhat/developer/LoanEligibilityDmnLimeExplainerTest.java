package com.redhat.developer;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureFactory;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.dmn.DecisionModelWrapper;
import com.redhat.developer.xai.lime.LimeExplainer;
import org.junit.jupiter.api.RepeatedTest;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.dmn.DMNKogito;
import org.kie.kogito.dmn.DmnDecisionModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoanEligibilityDmnLimeExplainerTest {

    @RepeatedTest(10)
    public void testLoanEligibilityDMNExplanation() {
        DMNRuntime dmnRuntime = DMNKogito.createGenericDMNRuntime(new InputStreamReader(getClass().getResourceAsStream("/dmn/LoanEligibility.dmn")));
        assertEquals(1, dmnRuntime.getModels().size());

        final String FRAUD_NS = "https://github.com/kiegroup/kogito-examples/dmn-quarkus-listener-example";
        final String FRAUD_NAME = "LoanEligibility";
        DecisionModel decisionModel = new DmnDecisionModel(dmnRuntime, FRAUD_NS, FRAUD_NAME);

        final Map<String, Object> client = new HashMap<>();
        client.put("age", 43);
        client.put("salary", 1950);
        client.put("existing payments", 100);
        final Map<String, Object> loan = new HashMap<>();
        loan.put("duration", 15);
        loan.put("installment", 100);
        final Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("client", client);
        contextVariables.put("loan", loan);

        Model model = new DecisionModelWrapper(decisionModel);
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newCompositeFeature("context", contextVariables));
        PredictionInput predictionInput = new PredictionInput(features);
        List<PredictionOutput> predictionOutputs = model.predict(List.of(predictionInput));
        Prediction prediction = new Prediction(predictionInput, predictionOutputs.get(0));
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        Saliency saliency = limeExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<String> strings = saliency.getTopFeatures(3).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("installment") || strings.contains("duration"));
    }
}
