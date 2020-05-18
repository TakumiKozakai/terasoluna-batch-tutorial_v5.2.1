package org.terasoluna.batch.tutorial.common.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ExitStatusChangeListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// do nothing.
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = stepExecution.getExitStatus();

		if (this.conditionalCheck(stepExecution)) {
			exitStatus = new ExitStatus("SKIPPED");
		}

		return exitStatus;
	}

	private boolean conditionalCheck(StepExecution stepExecution) {
		// 入力（読込）データ件数と出力（書込）データ件数が一致の時、trueを返却
		return (stepExecution.getWriteCount() != stepExecution.getReadCount());
	}

}
