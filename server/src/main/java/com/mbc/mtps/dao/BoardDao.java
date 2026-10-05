package com.mbc.mtps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.BoardDto;
import com.mbc.mtps.dto.BoardParam;
@Mapper
@Repository
public interface BoardDao {

//================================== 게시판 ======================================

	// 게시글 전체 목록 조회
	List<BoardDto> getBoardList(BoardParam param);

	// 전체 게시글 개수 조회
	int getBoardCount(BoardParam param);
	
	// 게시글 상세 조회
	BoardDto getBoard(int boardNo);

	// 게시글 등록
	int insertBoard(BoardDto board);

	// 게시글 조회수 1 증가
	int updateViewCnt(int boardNo);

	// 게시글 수정 (작성자 본인만 가능)
	int updateBoard(BoardDto board);

	// 게시글 삭제 (작성자 본인만 가능)
	int deleteBoard(int boardNo);
}
