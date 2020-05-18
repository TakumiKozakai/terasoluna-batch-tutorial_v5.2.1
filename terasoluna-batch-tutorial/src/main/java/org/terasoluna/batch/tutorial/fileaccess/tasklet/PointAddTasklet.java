package org.terasoluna.batch.tutorial.fileaccess.tasklet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.terasoluna.batch.tutorial.common.dto.MemberInfoDto;

@Component
@Scope("step")
public class PointAddTasklet implements Tasklet {

	private static final String TARGET_STATUS = "1";
	private static final String INITIAL_STATUS = "0";
	private static final String GOLD_MEMBER = "G";
	private static final String NORMAL_MEMBER = "N";
	private static final int MAX_POINT = 1000000;
	private static final int CHUNK_SIZE = 10;

	@Inject
	ItemStreamReader<MemberInfoDto> reader;

	@Inject
	ItemStreamWriter<MemberInfoDto> writer;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		MemberInfoDto item = null;

		List<MemberInfoDto> items = new ArrayList<>(CHUNK_SIZE);

		try {
			reader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
			writer.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());

			while ((item = reader.read()) != null) {

				if (TARGET_STATUS.equals(item.getStatus())) {

					if (GOLD_MEMBER.equals(item.getType())) {
						item.setPoint(item.getPoint() + 100);
					} else if (NORMAL_MEMBER.equals(item.getType())) {
						item.setPoint(item.getPoint() + 10);
					}

					if (item.getPoint() > MAX_POINT) {
						item.setPoint(MAX_POINT);
					}

					item.setStatus(INITIAL_STATUS);
				}

				items.add(item);

				if (items.size() == CHUNK_SIZE) {
					writer.write(items);
					items.clear();
				}
			}

			writer.write(items);
		} finally {
			try {
				reader.close();
			} catch (ItemStreamException e) {
				// do nothing.
			}
			try {
				writer.close();
			} catch (ItemStreamException e) {
				// do nothing.
			}
		}

		return RepeatStatus.FINISHED;
	}
}