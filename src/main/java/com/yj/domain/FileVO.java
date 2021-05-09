package com.yj.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class FileVO {
	private Long fno;
	private Long bno;
	private String originalName;
	private String saveName;
	private Long size;
	private String deleteYn;
	private String insertTime;
	private String deleteTime;
}
