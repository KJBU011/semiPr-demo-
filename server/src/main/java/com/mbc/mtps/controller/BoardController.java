package com.mbc.mtps.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.BoardDto;
import com.mbc.mtps.dto.BoardParam;
import com.mbc.mtps.service.BoardService;

@RestController
public class BoardController {

	final BoardService service;

	public BoardController(BoardService service) {
		this.service = service;
	}

//================================== 게시판 ======================================

	// 게시글 전체 목록 조회
	@GetMapping("getboardlist")
	public List<BoardDto> getBoardList(BoardParam param) {
		System.out.println("BoardController getBoardList " + new Date());
		return service.getBoardList(param);
	}

	// 전체 게시글 개수 조회
	@GetMapping("getboardcount")
	public int getBoardCount(BoardParam param) {
	    System.out.println("BoardController getBoardCount " + new Date());
	    return service.getBoardCount(param);
	}
	 
	// 게시글 상세 조회
	@GetMapping("getboard")
	public BoardDto getBoard(int boardNo) {
		System.out.println("BoardController getBoard " + new Date());
		return service.getBoard(boardNo);
	}

	// 게시글 등록
	@PostMapping("writeboard")
	public boolean writeBoard(BoardDto dto) {
		System.out.println("BoardController writeBoard " + new Date());

		if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()
			|| dto.getContent() == null || dto.getContent().trim().isEmpty()) {
			System.out.println("[등록 거부] 제목 또는 내용이 비어있음");
			return false;
		}

		return service.insertBoard(dto);
	}

	// 게시글 수정
	@PostMapping("updateboard")
	public boolean updateBoard(BoardDto dto) {
		System.out.println("BoardController updateBoard " + new Date());
		return service.updateBoard(dto);
	}

	// 게시글 삭제
	@PostMapping("deleteboard")
	public boolean deleteBoard(int boardNo) {
		System.out.println("BoardController deleteBoard " + new Date());
		return service.deleteBoard(boardNo);
	}
}
