package com.et.config.secretsmanager;

import io.awspring.cloud.secretsmanager.ValidationException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * Custom Analyzer that makes {@link ValidationException} more readable.
 *
 * @author Matej Nedic
 * @since 2.3.1
 */
public class AwsSecretsManagerValidationAnalyzer extends AbstractFailureAnalyzer<ValidationException> {

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, ValidationException cause) {
		return new FailureAnalysis("Validation failed for field: " + cause.getField(), cause.getMessage(), cause);
	}

}
