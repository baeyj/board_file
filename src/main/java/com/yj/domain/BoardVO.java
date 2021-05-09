package com.yj.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter 
@Setter
@ToString
public class BoardVO {
	private Long bno;
	private String title;
	private String writer;
	private String content;
	private String regdate;
	private Long viewcnt;
	private String deleteYn;
	
	/** 파일 변경 여부 */
	private String changeYn;

	/** 파일 인덱스 리스트 */
	private List<Long> fileIdxs;
} 

