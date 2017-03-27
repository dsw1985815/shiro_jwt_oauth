package com.dongsw.authority.controller;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.model.RolePermissions;
import com.dongsw.authority.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@RequestMapping("/role")
@Api(value = "角色相关", description = "戚羿辰,董双伟")
public class RoleController extends BaseController {

	@Autowired
	private RoleService service;

	@ApiOperation(value = "查询角色", notes = "查询角色信息")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "id", dataType = "Integer", required = true, value = "id"),
			@ApiImplicitParam(paramType = "path", name = "platType", dataType = "String", required = true, value = "平台类型"), })
	@GetMapping("/{id}/{platType}")
	public String get(@PathVariable(value = "id") Integer id, @PathVariable(value = "platType") String platType) {
		return responseSuccess(service.getRole(id, platType));
	}

	@ApiOperation(value = "查询角色列表", notes = "查询角色信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "rolaName", dataType = "String", required = false, value = "角色名称"),
			@ApiImplicitParam(paramType = "query", name = "platType", dataType = "String", required = false, value = "平台类型"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "分页大小,默认10", defaultValue = ConstantDef.DEFAULT_PAGESIZE),
			@ApiImplicitParam(paramType = "query", name = "pageNo", dataType = "Integer", required = false, value = "页码,默认1", defaultValue = ConstantDef.DEFAULT_PAGENO), })
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@GetMapping
	public String query(String rolaName, String platType, Integer pageSize, Integer pageNo) {
		return responseSuccess(service.query(rolaName, platType, pageSize, pageNo));
	}

	@ApiOperation(value = "新增角色对权限关系", notes = "需要指定具体的增删改查权限时可以设置method")
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@PostMapping("/permission")
	public String insertPermission(@RequestBody RolePermissions rolePermissions) {
		service.insertPermission(rolePermissions);
		return responseSuccess();
	}

	@ApiOperation(value = "删除角色对权限关系", notes = "查询角色权限列表信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "roleId", dataType = "Integer", required = true, value = "角色id"),
			@ApiImplicitParam(paramType = "query", name = "permissionId", dataType = "Integer", required = true, value = "权限id"),
			@ApiImplicitParam(paramType = "query", name = "platType", dataType = "String", required = true, value = "平台类型"),
			@ApiImplicitParam(paramType = "query", name = "method", dataType = "String", required = false, value = "权限方法[creade,update,read,delete]"), })
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@DeleteMapping("/permission")
	public String delPermission(@ApiIgnore @RequestBody RolePermissions rolePermissions) {
		service.delPermission(rolePermissions);
		return responseSuccess();
	}

	@ApiOperation(value = "查询角色对应的权限列表,不分页", notes = "查询角色权限列表信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "roleId", dataType = "String", required = false, value = "角色名称"),
			@ApiImplicitParam(paramType = "query", name = "platType", dataType = "String", required = false, value = "平台类型"), })
	@ApiResponses({ @ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果") })
	@GetMapping("/permission")
	public String queryPermission(@ApiIgnore RolePermissions rolePermissions) {
		return responseSuccess(service.queryPermission(rolePermissions));
	}

}
