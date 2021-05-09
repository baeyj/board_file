package com.yj.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yj.domain.FileVO;



@Mapper
public interface FileMapper {
	
	public List<FileVO> selectFileList(Long bno) throws SQLException; 
	
	public FileVO selectFileDetail(Long fno) throws SQLException;
	
	public int insertFile(List<FileVO> fileList) throws SQLException;
	
	public int updateFile(List<Long> fnos) throws SQLException;
	
	public int deleteFile(Long bno) throws SQLException;
	
	public int selectFileTotalCount(Long bno) throws SQLException;

}
