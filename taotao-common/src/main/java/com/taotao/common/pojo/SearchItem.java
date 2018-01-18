package com.taotao.common.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable {
	public static final String ID = "id";
	public static final String TITLE = "item_title";
	public static final String SELL_POINT = "item_sell_point";
	public static final String PRICE = "item_price";
	public static final String IMAGE = "item_image";
	public static final String CATEGORY_NAME = "item_category_name";
	public static final String ITEM_DESC = "item_desc";
	private Long id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String category_name;
	private String item_desc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSell_point() {
		return sell_point;
	}

	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getItem_desc() {
		return item_desc;
	}

	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}

	public String[] getImages() {
		if (image != null && !"".equals(image)) {
			String[] split = image.split(",");
			return split;
		}
		return null;
	}

}
