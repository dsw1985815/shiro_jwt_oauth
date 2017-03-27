package com.dongsw.authority.controller.token;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */

import com.dongsw.authority.controller.BaseController;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.model.TokenType;
import com.dongsw.authority.service.TokenTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/token/type")
@Api(value = "令牌类型相关", description = "戚羿辰,董双伟")
public class TokenTypeController extends BaseController {

    @Autowired
    private TokenTypeService sevrice;

    @ApiOperation(value = "新增令牌类型", notes = "新增令牌类型信息")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @PostMapping
    public String insert(@Valid @RequestBody TokenType tokenType, BindingResult bindingResult) {
        sevrice.insert(tokenType);
        return responseSuccess(tokenType.getId());
    }

    @ApiOperation(value = "删除令牌类型", notes = "新增令牌类型信息")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @DeleteMapping("/{id}")
    public String insert(@PathVariable Integer id) {
        sevrice.delete(id);
        return responseSuccess();
    }

    @ApiOperation(value = "修改令牌类型", notes = "新增令牌类型信息")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @PutMapping
    public String update(@Valid @RequestBody TokenType tokenType, BindingResult bindingResult) {
        sevrice.update(tokenType);
        return responseSuccess();
    }

    @ApiOperation(value = "查询令牌类型", notes = "新增令牌类型信息")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @GetMapping("/{id}")
    public String get(@PathVariable Integer id) {
        return responseSuccess(sevrice.get(id));
    }
    
    @ApiOperation(value = "查询令牌类型", notes = "新增令牌类型信息")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @GetMapping()
    public String all() {
    	return responseSuccess(sevrice.all());
    }

}