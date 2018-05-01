package com.lock.demo.controller;

import com.lock.demo.entity.JsonResponse;
import com.lock.demo.entity.SimpleSessionParam;
import com.lock.demo.entity.UserParam;
import com.lock.demo.service.UserService;
import com.lock.demo.tools.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "用户controller", description = "用户操作", tags = {"用户登录接口"})
@RestController
@RequestMapping("/user")
public class UserController {


    @ApiOperation(value = "测试方法", notes = "需要提供SessionID与版本号AppVersion")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "session_id", value = "会话id", required = true),
//            @ApiImplicitParam(name = "app_version", value = "app版本", required = true)
//    })
    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse test(@RequestBody SimpleSessionParam param) {
        String SessionId = param.getSessionId();
        String AppVersion = param.getAppVersion();
        String msg = "Test I am spring login user ! The sessionid is:" + SessionId + ",the app version is:" + AppVersion;
        return Json.success(msg);
    }

    @ApiOperation(value = "用户登录", notes = "需要提供SessionId=admin,AppVersion=admin即可获得session", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse login(SimpleSessionParam param) {
        String SessionId = param.getSessionId();
        String AppVersion = param.getAppVersion();
        String loginInfo = UserService.instance.login(SessionId, AppVersion);
        return Json.success(loginInfo);
    }


    @ApiOperation(value = "新增用户", notes = "需要提供username,passwd", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse add(@RequestBody UserParam param) {
        String username = param.getUserName();
        String passwd = param.getPasswd();
        String info = UserService.instance.addUser(username, passwd);
        String msg = "I am Error Test:" + info;
        return Json.error(msg);
    }
}
