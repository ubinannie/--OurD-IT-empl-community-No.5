package com.ourd.service;

import java.util.Collections;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ourd.domain.BoardDTO;
import com.ourd.domain.TipBoardDTO;
import com.ourd.mapper.TipBoardMapper;
import com.ourd.paging.PaginationInfo;

@Service
public class TipBoardServiceImple implements TipBoardService{

	@Autowired
	private TipBoardMapper tipBoardMapper;

	@Override
	public boolean registerBoard2(TipBoardDTO params) {
		int queryResult = 0;

		if (params.getIdx() == null) {
			queryResult = tipBoardMapper.insertBoard2(params);
		} else {
			queryResult = tipBoardMapper.updateBoard2(params);
		}

		return (queryResult == 1) ? true : false;
	}


	@Override
	public TipBoardDTO getBoardDetail2(Long idx) {
		return tipBoardMapper.selectBoardDetail2(idx);
		
	}

	@Override
	public boolean deleteBoard2(Long idx) {
		int queryResult = 0;

		TipBoardDTO board = tipBoardMapper.selectBoardDetail2(idx);

		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = tipBoardMapper.deleteBoard2(idx);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<TipBoardDTO> getBoardList2(TipBoardDTO params) {
		List<TipBoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = tipBoardMapper.selectBoardTotalCount2(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);

		params.setPaginationInfo(paginationInfo);

		if (boardTotalCount > 0) {
			boardList = tipBoardMapper.selectBoardList2(params);
		}

		return boardList;
	}


}
