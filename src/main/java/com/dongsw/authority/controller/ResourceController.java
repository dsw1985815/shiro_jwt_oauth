package com.dongsw.authority.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.service.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/resource")
@Api(value = "资源相关", description = "戚羿辰,董双伟")
public class ResourceController extends BaseController {

	@Autowired
	private ResourceService service;

	@ApiOperation(value = "新增资源", notes = "新增资源信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "resourcePath", dataType = "String", required = true, value = "资源地址"),
			@ApiImplicitParam(paramType = "query", name = "tokenTypeId", dataType = "Integer", required = false, value = "令牌类型"), })
	@PostMapping
	public String insert(@ApiIgnore @RequestBody Resource resource) {
		service.insert(resource);
		return responseSuccess(resource.getId());
	}

	@ApiOperation(value = "删除资源", notes = "新增资源信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "id", dataType = "Integer", required = true, value = "资源id"), })
	@DeleteMapping("/{id}")
	public String insert(@PathVariable Integer id) {
		service.delete(id);
		return responseSuccess();
	}

	@ApiOperation(value = "修改资源", notes = "新增资源信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "resourcePath", dataType = "String", required = true, value = "资源地址"),
			@ApiImplicitParam(paramType = "query", name = "tokenTypeId", dataType = "Integer", required = false, value = "令牌类型"),
			@ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", required = true, value = "资源id") })
	@PutMapping
	public String update(@ApiIgnore @RequestBody Resource resource) {
		service.update(resource);
		return responseSuccess();
	}

	@ApiOperation(value = "查询资源", notes = "新增资源信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "id", dataType = "Integer", required = true, value = "资源id"), })
	@GetMapping("/{id}")
	public String get(@PathVariable Integer id) {
		return responseSuccess(service.get(id));
	}

	@ApiOperation(value = "查询资源列表", notes = "新增资源信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "resourcePath", dataType = "String", required = false, value = "资源地址"),
			@ApiImplicitParam(paramType = "query", name = "tokenTypeId", dataType = "Integer", required = false, value = "令牌类型"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "分页大小,默认10", defaultValue = ConstantDef.DEFAULT_PAGESIZE),
			@ApiImplicitParam(paramType = "query", name = "pageNo", dataType = "Integer", required = false, value = "页码,默认1", defaultValue = ConstantDef.DEFAULT_PAGENO), })
	@GetMapping
	public String query(@ApiIgnore Resource resource, Integer pageSize, Integer pageNo) {
		return responseSuccess(service.query(resource, pageSize, pageNo));
	}
}
