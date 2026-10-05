package com.mbc.mtps.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.BoardDao;
import com.mbc.mtps.dto.BoardDto;
import com.mbc.mtps.dto.BoardParam;

@Service
@Transactional
public class BoardService {

	final BoardDao dao;

	public BoardService(BoardDao dao) {
		this.dao = dao;
	}

//================================== 게시판 ======================================

	// 게시글 전체 목록 조회
	public List<BoardDto> getBoardList(BoardParam param) {
		return dao.getBoardList(param);
	}

	// 전체 게시글 개수 조회
	public int getBoardCount(BoardParam param) {
	    return dao.getBoardCount(param);
	}
	
	// 게시글 상세 조회 (조회수 증가 후 상세 데이터 반환)
	public BoardDto getBoard(int boardNo) {

		int updated = dao.updateViewCnt(boardNo);

		if (updated > 0) {
			System.out.println("[조회수 증가 성공] boardNo=" + boardNo);
		} else {
			System.out.println("[조회수 증가 실패] boardNo=" + boardNo);
		}

		return dao.getBoard(boardNo);
	}

	// 게시글 등록
	public boolean insertBoard(BoardDto board) {
		int count = dao.insertBoard(board);
		return count > 0;
	}

	// 게시글 수정
	public boolean updateBoard(BoardDto board) {
		int count = dao.updateBoard(board);
		return count > 0;
	}

	// 게시글 삭제
	public boolean deleteBoard(int boardNo) {
		int count = dao.deleteBoard(boardNo);
		return count > 0; 
	}
}