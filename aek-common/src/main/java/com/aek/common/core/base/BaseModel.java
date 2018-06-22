package com.aek.common.core.base;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * jackson 基础数据过滤,只过滤BaseModel类中的字段
	 * 
	 * @author Mr.han
	 */
	public interface BaseView {
	};

	/**
	 * 主键ID
	 */
	@ApiModelProperty("ID")
	@TableId(value = "id", type = IdType.AUTO)
	@JsonView({ BaseView.class })
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
