package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理服务
 * @Date 2016年12月31日
 * @Author 朱良才
 * @Email 1024955966@qq.com
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		Object object = new Object();
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 根据parentId查询子节点列表
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			// 添加到结果
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public TaotaoResult insertContentCat(long parentId, String name) {
		// 创建一个内容分类对象
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setIsParent(false);
		contentCategory.setSortOrder(1);// 排序方式，默认为1
		contentCategory.setStatus(1);// 分类状态，1是正常，2是删除
		Date date = new Date();
		contentCategory.setCreated(date);
		contentCategory.setUpdated(date);
		// 插入节点
		contentCategoryMapper.insert(contentCategory);
		// 判断父节点是否是叶子节点
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			// 更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCat(Long id, String name) {
		// 创建一个内容分类对象
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setId(id);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return TaotaoResult.ok(contentCategory);
//		TbContentCategory contentNode = contentCategoryMapper.selectByPrimaryKey(id);
//		contentCategoryMapper.updateByPrimaryKey(contentNode);
//		return TaotaoResult.ok(contentNode);
	}

	@Override
	public TaotaoResult deleteContentCat(Long id) {
		contentCategoryMapper.deleteByPrimaryKey(id);
		return TaotaoResult.ok();
	}

}
