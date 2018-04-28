package com.lucadev.trampoline.security.ac.decision;

import com.lucadev.trampoline.security.ac.AccessEvaluator;
import com.lucadev.trampoline.security.ac.evaluate.Evaluation;
import com.lucadev.trampoline.security.ac.evaluate.EvaluationFactory;
import com.lucadev.trampoline.security.ac.evaluate.EvaluationResult;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * {@link DecisionManager} that requires no evaluator to deny access.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 27-4-18
 */
@AllArgsConstructor
public class AffirmativeDecisionManager implements DecisionManager {

    private static final EvaluationResult DEFAULT_EVALUATION_RESULT = EvaluationResult.IGNORE;

    private final EvaluationFactory evaluationFactory;
    private final List<AccessEvaluator> accessEvaluators;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean decide(Authentication auth, Object domainObject, String targetType, String permission) {
        for (AccessEvaluator accessEvaluator : accessEvaluators) {
            Evaluation evaluation = evaluationFactory.create(DEFAULT_EVALUATION_RESULT);
            accessEvaluator.checkAccess(evaluation, auth, domainObject, targetType, permission);

            if (evaluation.getEvaluationResult() == EvaluationResult.DENY) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public List<AccessEvaluator> getAccessEvaluators() {
        return accessEvaluators;
    }
}