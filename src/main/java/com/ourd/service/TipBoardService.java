package com.ourd.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ourd.domain.AttachDTO;
import com.ourd.domain.TipBoardDTO;

public interface TipBoardService {
	
	public boolean registerBoard2(TipBoardDTO params);

	public TipBoardDTO getBoardDetail2(Long idx);

	public boolean deleteBoard2(Long idx);

	public List<TipBoardDTO> getBoardList2(TipBoardDTO params);

}
