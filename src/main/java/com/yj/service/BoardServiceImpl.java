package com.yj.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.yj.domain.BoardVO;
import com.yj.domain.FileVO;
import com.yj.mapper.BoardMapper;
import com.yj.mapper.FileMapper;
import com.yj.util.FileUtils;





@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BoardVO> getBoardList() throws Exception {
		return boardMapper.selectBoardList();
	}

	@Override
	public BoardVO getBoard(Long bno) throws Exception {
		return boardMapper.selectBoardDetail(bno);
	}

	@Override
	public void registerBoard(BoardVO boardVO) throws Exception {
		if(boardVO.getBno() == null) {
			boardMapper.registerBoard(boardVO);
		}else {
			boardMapper.updateBoard(boardVO);
			
			if("Y".equals(boardVO.getChangeYn())) {
				fileMapper.deleteFile(boardVO.getBno());
				
				if(CollectionUtils.isEmpty(boardVO.getFileIdxs()) == false) {
					fileMapper.updateFile(boardVO.getFileIdxs());
				}
			}
		}
	}

	@Override
	public void registerBoard(BoardVO boardVO, MultipartFile[] files) throws Exception {
		//boardMapper.registerBoard(boardVO);
		registerBoard(boardVO);
		
		List<FileVO> fileList = fileUtils.uploadFile(files, boardVO.getBno());
		if (CollectionUtils.isEmpty(fileList) == false) {
			System.out.println("fileList" + fileList);
			fileMapper.insertFile(fileList);
		}
	}

	@Override
	public void deleteBoard(Long bno) throws Exception {
		BoardVO boardVO = boardMapper.selectBoardDetail(bno);
		if(boardVO != null && "N".equals(boardVO.getDeleteYn())) {
			boardMapper.deleteBoard(bno);
			
			
		}
	}

	@Override
	public List<FileVO> getFileList(Long bno) throws Exception {
		int fileTotalCount = fileMapper.selectFileTotalCount(bno);
		if (fileTotalCount < 1) {
			return Collections.emptyList();
		}
		return fileMapper.selectFileList(bno);
	}

	@Override
	public FileVO getFile(Long fno) throws Exception {
		return fileMapper.selectFileDetail(fno);
	}

	@Override
	public void deleteFile(Long bno) throws Exception {
		fileMapper.deleteFile(bno);
	}

}
