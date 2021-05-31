package com.ourd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ourd.domain.TipBoardDTO;

@Mapper
public interface TipBoardMapper {
	
	public int insertBoard2(TipBoardDTO params);

	public TipBoardDTO selectBoardDetail2(Long idx);

	public int updateBoard2(TipBoardDTO params);

	public int deleteBoard2(Long idx);

	public List<TipBoardDTO> selectBoardList2(TipBoardDTO params);

	public int selectBoardTotalCount2(TipBoardDTO params);
}
