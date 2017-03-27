package com.dongsw.authority.controller;

import com.dongsw.authority.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@RestController
@RequestMapping("/console")
public class TestController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate<Serializable, Object>	template;
	@Autowired
	private RestTemplate						restTemplate;

	@GetMapping("/basedata/teamtype/teamclassandtypelist")
	public String getUser() {
		return responseSuccess("{datasend/console/basedata/teamtype/teamclassandtypelist success}");
	}


	@GetMapping("/bbasedata/teamtype/tokenrequired")
	public String getUser1() {
		return responseSuccess("send/console/basedata/teamtype/tokenrequired success");
	}

}
