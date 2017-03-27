package com.dongsw.authority.controller;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.PermissionResources;
import com.dongsw.authority.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 权限controller
 * Created by 戚羿辰 on 2017/02/17。
 */
@RestController
@RequestMapping("/permission")
@Api(value = "权限相关", description = "戚羿辰,董双伟")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService service;

	@ApiOperation(value = "新增权限", notes = "新增权限信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "permission", dataType = "String", required = true, value = "权限名称"),
		@ApiImplicitParam(paramType = "query", name = "permissionCode", dataType = "String", required = true, value = "权限编码"), 
		})
	@PostMapping
	public String insert(@ApiIgnore @RequestBody Permission perm) {
		service.insert(perm);
		return responseSuccess(perm.getId());
	}
	
	@ApiOperation(value = "删除权限", notes = "新增权限信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@DeleteMapping("/{id}")
	public String insert(@PathVariable Integer id) {
		// TODO 删除权限需要删除所有关联信息
		service.delete(id);
		return responseSuccess();
	}
	
	@ApiOperation(value = "修改权限", notes = "新增权限信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "permission", dataType = "String", required = true, value = "权限名称"),
		@ApiImplicitParam(paramType = "query", name = "permissionCode", dataType = "String", required = true, value = "权限编码"), 
		@ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", required = true, value = "权限id"), 
		})
	@PutMapping
	public String update(@ApiIgnore @RequestBody Permission permission) {
		service.update(permission);
		return responseSuccess();
	}
	
	@ApiOperation(value = "查询权限", notes = "新增权限信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@GetMapping("/{id}")
	public String get(@PathVariable Integer id) {
		return responseSuccess(service.get(id));
	}
	
	@ApiOperation(value = "查询权限列表", notes = "新增权限信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "permission", dataType = "String", required = false, value = "权限"),
		@ApiImplicitParam(paramType = "query", name = "permissionCode", dataType = "String", required = false, value = "权限编码"),
		@ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "分页大小,默认10", defaultValue = ConstantDef.DEFAULT_PAGESIZE),
		@ApiImplicitParam(paramType = "query", name = "pageNo", dataType = "Integer", required = false, value = "页码,默认1", defaultValue = ConstantDef.DEFAULT_PAGENO), })
	@GetMapping
	public String query(@ApiIgnore Permission permission, Integer pageSize, Integer pageNo) {
		return responseSuccess(service.query(permission, pageSize, pageNo));
	}

	@ApiOperation(value = "新增权限对资源关系", notes = "需要指定具体的增删改查权限时可以设置method")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "resource", dataType = "Integer", required = true, value = "角色id"),
		@ApiImplicitParam(paramType = "query", name = "permissionId", dataType = "Integer", required = true, value = "权限id"), })
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@PostMapping("/resource")
	public String insertResource(@ApiIgnore @RequestBody PermissionResources permissionResources) throws IllegalDataException {
		service.insertResource(permissionResources);
		return responseSuccess();
	}

	@ApiOperation(value = "删除权限对应的资源", notes = "删除权限对应的资源")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@DeleteMapping("/resource")
	public String delResource(@RequestBody PermissionResources permissionResources) throws IllegalDataException {
		service.delResource(permissionResources);
		return responseSuccess();
	}

	@ApiOperation(value = "查询权限对应的资源", notes = "查询角色权限列表信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "resource", dataType = "String", required = false, value = "资源"),
		@ApiImplicitParam(paramType = "query", name = "permissionId", dataType = "Integer", required = false, value = "权限id"),
		@ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "分页大小,默认10", defaultValue = ConstantDef.DEFAULT_PAGESIZE),
		@ApiImplicitParam(paramType = "query", name = "pageNo", dataType = "Integer", required = false, value = "页码,默认1", defaultValue = ConstantDef.DEFAULT_PAGENO),
			})
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@GetMapping("/resource")
	public String queryResource(@ApiIgnore PermissionResources permissionResources, Integer pageSize, Integer pageNo) {
		return responseSuccess(service.queryPermission(permissionResources, pageSize, pageNo));
	}
	
}
