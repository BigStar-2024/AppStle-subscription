package com.et.config.paramstore;

import io.awspring.cloud.paramstore.ValidationException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * Custom Analyzer that makes {@link ValidationException} more readable.
 *
 * @author Matej Nedic
 * @since 2.3.1
 */
public class AwsParamStoreValidationAnalyzer extends AbstractFailureAnalyzer<ValidationException> {

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, ValidationException cause) {
		return new FailureAnalysis("Validation failed for field: " + cause.getField(), cause.getMessage(), cause);
	}

}
