package org.terasoluna.batch.tutorial.common.repository;

import org.apache.ibatis.cursor.Cursor;
import org.terasoluna.batch.tutorial.common.dto.MemberInfoDto;

public interface MemberInfoRepository {
	Cursor<MemberInfoDto> cursor();

	int updatePointAndStatus(MemberInfoDto memberInfo);
}
