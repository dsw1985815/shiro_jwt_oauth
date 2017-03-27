package com.dongsw.authority;

import com.dongsw.authority.controller.BaseController;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
@WebMvcTest(BaseController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseHttpTestCase extends BaseTestCase{
}
