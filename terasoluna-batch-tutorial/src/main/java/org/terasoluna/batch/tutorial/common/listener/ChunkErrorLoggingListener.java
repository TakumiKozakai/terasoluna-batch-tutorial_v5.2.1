package org.terasoluna.batch.tutorial.common.listener;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ChunkErrorLoggingListener implements ChunkListener {

	private static final Logger logger = LoggerFactory.getLogger(ChunkErrorLoggingListener.class);

	@Inject
	MessageSource messageSource;

	@Override
	public void beforeChunk(ChunkContext chunkContext) {
		// do nothing.
	}

	@Override
	public void afterChunk(ChunkContext chunkContext) {
		// do nothing.
	}

	@Override
	public void afterChunkError(ChunkContext chunkContext) {

		Exception e = (Exception) chunkContext.getAttribute(ChunkListener.ROLLBACK_EXCEPTION_KEY);

		if (e instanceof ValidationException) {
			logger.error(
					// プロパティファイルからメッセージIDがerrors.maxIntegerのメッセージを取得
					messageSource.getMessage(
							"errors.maxInteger",
							new String[] { "point", "1000000" },
							Locale.getDefault()));
		}
	}
}
