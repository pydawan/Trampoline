package com.lucadev.trampoline.security.abac.policy.impl;

import com.lucadev.trampoline.security.abac.context.SecurityAccessContext;
import com.lucadev.trampoline.security.abac.context.SecurityAccessContextFactory;
import com.lucadev.trampoline.security.abac.policy.PolicyDefinition;
import com.lucadev.trampoline.security.abac.policy.PolicyEnforcement;
import com.lucadev.trampoline.security.abac.policy.PolicyRule;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

/**
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 20-5-18
 */
@AllArgsConstructor
public class TrampolinePolicyEnforcement implements PolicyEnforcement {

    private static final Logger logger = LoggerFactory.getLogger(TrampolinePolicyEnforcement.class);
    private final SecurityAccessContextFactory securityAccessContextFactory;
    private final PolicyDefinition policyDefinition;

    /**
     * Check if we have access to the given resource.
     *
     * @param subject     the identity trying to access the resource.
     * @param resource    the resource which is being accessed
     * @param action      the action that is taken against the resource
     * @param environment the current context in which the action is taking place
     * @return the result of the policy check, true means we are allowed to access and false should deny access.
     */
    @Override
    public boolean check(Object subject, Object resource, Object action, Object environment) {
        //Get all policy rules
        List<PolicyRule> allRules = policyDefinition.getAllPolicyRules();
        //Wrap the context
        SecurityAccessContext cxt = securityAccessContextFactory.create(subject, resource, action, environment);
        //Filter the rules according to context.
        List<PolicyRule> matchedRules = filterRules(allRules, cxt);
        //finally, check if any of the rules are satisfied, otherwise return false.
        return checkRules(matchedRules, cxt);
    }

    /**
     * Check if we have access to the given resource.
     * In this method the Spring {@link org.springframework.security.core.context.SecurityContext} will be used.
     *
     * @param resource   the resource which is being accessed
     * @param permission the permission required by the action against the resource.
     */
    @Override
    public void check(Object resource, String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (check(auth.getPrincipal(), resource, permission, environmentContext()))
            throw new AccessDeniedException("Access is denied");
    }

    /**
     * Get the environment through the use of Spring context.
     *
     * @return environment built from Spring context.
     */
    private Map<String, Object> environmentContext() {
        Map<String, Object> environment = new HashMap<>();

		/*
        Object authDetails = auth.getDetails();
		if(authDetails != null) {
			if(authDetails instanceof WebAuthenticationDetails) {
				environment.put("remoteAddress", ((WebAuthenticationDetails) authDetails).getRemoteAddress());
			}
		}
		*/
        environment.put("time", new Date());
        return environment;
    }

    /**
     * Filters the rules to match the given {@link SecurityAccessContext}
     *
     * @param allRules the rules to filter.
     * @param cxt      the {@link SecurityAccessContext} to apply as filter constraints.
     * @return the filtered {@link List} of {@link PolicyRule} objects.
     */
    private List<PolicyRule> filterRules(List<PolicyRule> allRules, SecurityAccessContext cxt) {
        List<PolicyRule> matchedRules = new ArrayList<>();
        for (PolicyRule rule : allRules) {
            try {
                if (rule.getTarget().getValue(cxt, Boolean.class)) {
                    matchedRules.add(rule);
                }
            } catch (EvaluationException ex) {
                logger.info("An error occurred while evaluating PolicyRule.", ex);
            }
        }
        return matchedRules;
    }

    /**
     * Evaluate a {@link List} of {@link PolicyRule} objects against the {@link SecurityAccessContext}
     *
     * @param matchedRules the rules to evaluate.
     * @param cxt          the {@link SecurityAccessContext} to evaluate against
     * @return
     */
    private boolean checkRules(List<PolicyRule> matchedRules, SecurityAccessContext cxt) {
        for (PolicyRule rule : matchedRules) {
            try {
                if (rule.getCondition().getValue(cxt, Boolean.class)) {
                    return true;
                }
            } catch (EvaluationException ex) {
                logger.info("An error occurred while evaluating PolicyRule.", ex);
            }
        }
        return false;
    }
}
