package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "过滤类型", description = "戚羿辰,董双伟")
public class ResourceFilters {
	
	@ApiModelProperty(value = "id", dataType = "Integer")
	private Integer	id;
	//过滤类型
	@ApiModelProperty(value = "过滤类型", dataType = "String")
	private String	filterType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

}