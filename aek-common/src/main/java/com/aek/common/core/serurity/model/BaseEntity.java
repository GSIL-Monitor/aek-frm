package com.aek.common.core.serurity.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.aek.common.core.util.DataUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity 基类
 *
 * @author zj@aek56.com
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -5301879485649441727L;

	/**
	 * 实体编号（唯一标识）
	 */
	private String id;

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	private boolean isNewRecord = false;

	public BaseEntity() {

	}

	public BaseEntity(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 插入之前执行方法，子类实现
	 */
	public abstract void preInsert();

	/**
	 * 更新之前执行方法，子类实现
	 */
	public abstract void preUpdate();

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 *
	 * @return 是否是新记录
	 */
	@JsonIgnore
	public boolean getIsNewRecord() {
		return isNewRecord || DataUtil.isEmpty(getId());
	}

	/**
	 * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 *
	 * @param isNewRecord
	 *            是否为新数据
	 */
	public void setIsNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}