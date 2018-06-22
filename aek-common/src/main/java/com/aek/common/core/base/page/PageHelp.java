package com.aek.common.core.base.page;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页帮助类
 * 
 * @author Mr.han
 *
 */
@ApiModel(value = "Page", description = "分页帮助类")
public class PageHelp<T> {

	public static final int DEFAULT_PAGENO = 1;
	public static final int DEFAULT_PAGESIZE = 10;

	@ApiModelProperty(value = "当前页")
	private Integer pageNo;

	@ApiModelProperty(value = "分页大小")
	private Integer pageSize;

	@ApiModelProperty(value = "排序列(不需要则为null)")
	private String orderByField;

	@ApiModelProperty(value = "排序方式(排序列存在则排序方式必须存在)", allowableValues = "teue,false")
	private Boolean isAsc;

	public Page<T> getPage() {

		int current = this.pageNo == null ? DEFAULT_PAGENO : this.pageNo;
		int size = this.pageSize == null ? DEFAULT_PAGESIZE : this.pageSize;
		Page<T> page = null;

		if (StringUtils.isBlank(orderByField)) {
			page = new Page<T>(current, size);
		} else {
			page = new Page<T>(current, size, this.orderByField);
			if (this.isAsc != null) {
				page.setAsc(isAsc);
			}
		}
		return page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}

	public Boolean getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(Boolean isAsc) {
		this.isAsc = isAsc;
	}
}
