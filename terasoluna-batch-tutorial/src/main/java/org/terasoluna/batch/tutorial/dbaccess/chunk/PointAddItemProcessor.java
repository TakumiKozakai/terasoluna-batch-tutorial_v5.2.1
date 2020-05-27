package org.terasoluna.batch.tutorial.dbaccess.chunk;

import javax.inject.Inject;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.Validator;
import org.springframework.stereotype.Component;
import org.terasoluna.batch.tutorial.common.dto.MemberInfoDto;

@Component
public class PointAddItemProcessor implements ItemProcessor<MemberInfoDto, MemberInfoDto> {

	private static final String TARGET_STATUS = "1";
	private static final String INITIAL_STATUS = "0";
	private static final String GOLD_MEMBER = "G";
	private static final String NORMAL_MEMBER = "N";
	private static final int MAX_POINT = 1000000;

	@Inject
	Validator<MemberInfoDto> validator;

	@Override
	public MemberInfoDto process(MemberInfoDto item) throws Exception {

		validator.validate(item);

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

		return item;
	}
}
