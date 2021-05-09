package com.yj.controller;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yj.domain.BoardVO;
import com.yj.domain.FileVO;
import com.yj.service.BoardService;



@Controller
public class BoardController {

	@Autowired
    private BoardService boardService;
	
	@GetMapping("/")
	public String home(){
        return "index";
    }
	
	@GetMapping("/boardList")
	public String boardList(Model model) throws Exception{
		List<BoardVO> boardList = boardService.getBoardList();
		model.addAttribute("boardList", boardList);
		return "boardList";
	}
	
	@GetMapping("/registerBoard")
	public String openRegisterBoard(@RequestParam(value = "bno", required = false) Long bno, Model model) throws Exception{
		if(bno == null) {
			model.addAttribute("board", new BoardVO());
		}else {
			BoardVO boardVO = boardService.getBoard(bno);
			if(boardVO == null) {
				return "redirect:/boardList";
			}
			model.addAttribute("board", boardVO);
			
			List<FileVO> fileList = boardService.getFileList(bno);
			model.addAttribute("fileList", fileList);
		}
		return "registerBoard";
	}
	
	@PostMapping("/registerBoard")
	public String registerBoard(BoardVO boardVO, MultipartFile[] files) throws Exception{
		boardService.registerBoard(boardVO, files);
		return "redirect:/boardList";
	}
	
	@GetMapping("/getBoard")
	public String getBoard(@RequestParam(value = "bno", required = false) Long bno, Model model) throws Exception{
			if(bno == null) {
				return "redirect:/boardList";
			}else {
				
				BoardVO boardVO = boardService.getBoard(bno);
				model.addAttribute("board", boardVO);
				
				List<FileVO> fileList = boardService.getFileList(bno);
				model.addAttribute("fileList", fileList);
			}
		return "getBoard";
	}
	
	@GetMapping("/deleteBoard")
	public String deleteBoard(@RequestParam(value = "bno") Long bno) throws Exception{
		
		boardService.deleteBoard(bno);
		/*
		//업로드 폴더의 파일 삭제
		List<FileVO> fileList = boardService.getFileList(bno);
		System.out.println(fileList);
		for(FileVO list : fileList) {
			
			File file = new File(list.getSaveName());
			boolean result = file.delete();
			System.out.println(result);
		}
		*/
		boardService.deleteFile(bno);
         
		return "redirect:/boardList";
	}
	
	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam(value = "fno", required = false) Long fno, HttpServletRequest request) throws Exception{
		FileVO fileVO = boardService.getFile(fno);
		
		Path path = Paths.get(fileVO.getSaveName());
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
		
		return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileVO.getOriginalName(),"UTF-8") + "\"")
	            .body(resource);
	}
	
}
