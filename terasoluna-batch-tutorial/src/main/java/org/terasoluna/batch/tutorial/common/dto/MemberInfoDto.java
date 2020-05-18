package org.terasoluna.batch.tutorial.common.dto;

import javax.validation.constraints.Max;

public class MemberInfoDto {

	private String id;
	private String type;
	private String status;
	@Max(1000000)
	private int point;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
