package org.terasoluna.batch.tutorial.common.listener;

import java.util.Collection;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class JobExitCodeChangeListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// do nothing.
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

		for (StepExecution stepExecution : stepExecutions) {

			if("SKIPPED".equals(stepExecution.getExitStatus().getExitCode())) {
				jobExecution.setExitStatus(new ExitStatus("SKIPPED"));
				break;
			}
		}
	}

}
