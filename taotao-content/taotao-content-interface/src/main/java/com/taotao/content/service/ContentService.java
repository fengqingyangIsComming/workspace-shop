package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);

	void saveContent(TbContent content);
	void deleteContent(String ids);
	List<TbContent> getContentList(long categoryId);
}
