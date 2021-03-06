package com.education.student.api.controller;

import com.education.common.base.BaseController;
import com.education.common.utils.Result;
import com.education.service.system.SystemDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/11 21:46
 */
@RestController
@RequestMapping("/student/dict")
public class DictController extends BaseController {

    @Autowired
    private SystemDictService systemDictService;

    @GetMapping
    public Result list(@RequestParam Map params) {
        return systemDictService.pagination(params);
    }
}
