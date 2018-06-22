package com.aek.common.core;

import java.io.Serializable;
import java.util.List;

/**
 * 返回页面数据
 */
public class PageVo<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 当前页
	 */
	private Integer pageNo;
	/**
	 * 每页的条数
	 */
	private Integer pageSize;
	/**
	 * 总条数
	 */
	private Integer total;

	private List<T> data;

	public PageVo() {
		super();
	}

	public PageVo(Integer pageNo, Integer pageSize, Integer total) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
