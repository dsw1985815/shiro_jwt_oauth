package com.dongsw.authority.controller;

import com.dongsw.authority.common.def.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongsw.authority.model.ResourceFilters;
import com.dongsw.authority.service.ResourceFiltersService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * 过滤controller
 * Created by 戚羿辰 on 2017/02/17。
 */
@RestController
@RequestMapping("/resourceFilters")
@Api(value = "过滤相关", description = "戚羿辰,董双伟")
public class ResourceFilterController extends BaseController {

	@Autowired
	private ResourceFiltersService sevrice;

	@ApiOperation(value = "新增过滤", notes = "新增过滤")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@PostMapping
	public String insert(@RequestBody ResourceFilters entity) {
		sevrice.insert(entity);
		return responseSuccess(entity.getId());
	}
	
	@ApiOperation(value = "删除过滤", notes = "新增过滤信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Integer id) {
		sevrice.delete(id);
		return responseSuccess();
	}
	
	@ApiOperation(value = "修改过滤", notes = "新增过滤信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@PutMapping
	public String update(@RequestBody ResourceFilters entity) {
		sevrice.update(entity);
		return responseSuccess();
	}
	
	@ApiOperation(value = "查询过滤", notes = "新增过滤信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@GetMapping("/{id}")
	public String get(@PathVariable Integer id) {
		return responseSuccess(sevrice.get(id));
	}
	
	@ApiOperation(value = "查询过滤列表", notes = "新增过滤信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@GetMapping
	public String query() {
		return responseSuccess(sevrice.all());
	}

}
