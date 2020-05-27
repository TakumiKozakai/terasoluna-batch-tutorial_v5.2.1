package org.terasoluna.batch.tutorial.dbaccess.tasklet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.terasoluna.batch.tutorial.common.dto.MemberInfoDto;

@Component
public class PointAddTasklet implements Tasklet {

	private static final String TARGET_STATUS = "1";
	private static final String INITIAL_STATUS = "0";
	private static final String GOLD_MEMBER = "G";
	private static final String NORMAL_MEMBER = "N";
	private static final int MAX_POINT = 1000000;
	private static final int CHUNK_SIZE = 10;

	// データベースアクセスするためにItemReaderのサブインタフェースである、ItemStreamReaderとして型を定義する。
	// ItemStreamReaderはリソースのオープン/クローズを実行する必要がある。
	@Inject
	ItemStreamReader<MemberInfoDto> reader;
	// ItemWriterを定義する。
	// ItemStreamReaderとは異なり、リソースのオープン/クローズを実行する必要はない。
	@Inject
	ItemWriter<MemberInfoDto> writer; // (10)
	@Inject
	Validator<MemberInfoDto> validator;

	// ビジネスロジック
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		MemberInfoDto item = null;
		// 一定件数分のitemを格納するためのリストを定義する。
		List<MemberInfoDto> items = new ArrayList<>(CHUNK_SIZE);

		try {
			// 入力リソースをオープンする。このタイミングでSQLが発行される。
			reader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());

			// 入力リソース全件を逐次ループ処理する。
			// ItemReader#readは、入力データがすべて読み取り末端に到達した場合、nullを返却する。
			while ((item = reader.read()) != null) {
				validator.validate(item);

				if (TARGET_STATUS.contentEquals(item.getStatus())) {
					if (GOLD_MEMBER.equals(item.getType())) {
						item.setPoint(item.getPoint() + 100);
					} else if (NORMAL_MEMBER.contentEquals(item.getType())) {
						item.setPoint(item.getPoint() + 10);
					}

					if (item.getPoint() > MAX_POINT) {
						item.setPoint(MAX_POINT);
					}

					item.setStatus(INITIAL_STATUS);
				}

				items.add(item);

				// リストに追加したitemの数が一定件数に達したかどうかを判定する。
				// 一定件数に達した場合は、データベースへ出力し、リストをclearする。
				if (items.size() == CHUNK_SIZE) {
					// データベースへ出力する。
					// このタイミングでコミットするわけではないため留意すること。
					writer.write(items);
					items.clear();
				}
			}

			// 全体の処理件数/一定件数の余り分をデータベースへ出力する。
			writer.write(items);
		} finally {
			// リソースをクローズする。なお、ここでは実装を簡易にするため例外処理を実装していない。例外処理は必要に応じて実装すること。
			// ここで例外が発生した場合、タスクレット全体のトランザクションがロールバックされ、例外のスタックトレースを出力し、ジョブが異常終了する。
			reader.close();
		}

		// Taskletの処理が完了したかどうかを返却する。
		// 常にreturn RepeatStatus.FINISHED;と明示する。
		return RepeatStatus.FINISHED;
	}
}
