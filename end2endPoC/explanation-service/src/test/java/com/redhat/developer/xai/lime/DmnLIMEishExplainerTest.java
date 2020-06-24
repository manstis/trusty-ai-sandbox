package com.redhat.developer.xai.lime;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureFactory;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.model.dmn.DecisionModelWrapper;
import org.junit.jupiter.api.RepeatedTest;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.dmn.DMNKogito;
import org.kie.kogito.dmn.DmnDecisionModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DmnLIMEishExplainerTest {

    @RepeatedTest(10)
    public void testDMNExplanation() {
        DMNRuntime dmnRuntime = DMNKogito.createGenericDMNRuntime(new InputStreamReader(getClass().getResourceAsStream("/dmn/TrafficViolation.dmn")));
        assertEquals(1, dmnRuntime.getModels().size());

        final String TRAFFIC_VIOLATION_NS = "https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF";
        final String TRAFFIC_VIOLATION_NAME = "Traffic Violation";
        DecisionModel decisionModel = new DmnDecisionModel(dmnRuntime, TRAFFIC_VIOLATION_NS, TRAFFIC_VIOLATION_NAME);
        final Map<String, Object> driver = new HashMap<>();
        driver.put("Points", 10);
        final Map<String, Object> violation = new HashMap<>();
        violation.put("Type", "speed");
        violation.put("Actual Speed", 105);
        violation.put("Speed Limit", 100);
        final Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("Driver", driver);
        contextVariables.put("Violation", violation);

        final DMNContext context = decisionModel.newContext(contextVariables);
        DMNResult dmnResult = decisionModel.evaluateAll(context);
        List<Output> outputs = new LinkedList<>();
        for (DMNDecisionResult decisionResult : dmnResult.getDecisionResults()) {
            Output output = new Output(decisionResult.getDecisionName(), Type.TEXT, new Value<>(decisionResult.getResult()), 1d);
            outputs.add(output);
        }
        PredictionOutput predictionOutput = new PredictionOutput(outputs);
        Model model = new DecisionModelWrapper(decisionModel);
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newCompositeFeature("context", contextVariables));
        PredictionInput predictionInput = new PredictionInput(features);
        Prediction prediction = new Prediction(predictionInput, predictionOutput);
        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 1);
        Saliency saliency = limEishExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<String> strings = saliency.getTopFeatures(2).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("Actual Speed") || strings.contains("Speed Limit"));
    }
}
