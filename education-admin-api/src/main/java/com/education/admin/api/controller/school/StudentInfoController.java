package com.education.admin.api.controller.school;import cn.afterturn.easypoi.excel.ExcelImportUtil;import cn.afterturn.easypoi.excel.entity.ImportParams;import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;import com.education.common.base.BaseController;import com.education.common.model.AdminUserSession;import com.education.common.model.ModelBeanMap;import com.education.common.model.StudentInfo;import com.education.common.utils.ObjectUtils;import com.education.common.utils.Result;import com.education.common.utils.ResultCode;import com.education.service.school.StudentInfoService;import lombok.extern.slf4j.Slf4j;import org.apache.shiro.authz.annotation.Logical;import org.apache.shiro.authz.annotation.RequiresPermissions;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import java.io.InputStream;import java.util.Date;import java.util.List;import java.util.Map;/** * 学员管理 * @author zengjintao * @create 2019/4/19 10:49 * @since 1.0 **/@RestController@Slf4j@RequestMapping("/system/studentInfo")public class StudentInfoController extends BaseController {    @Autowired    private StudentInfoService studentInfoService;    @GetMapping    @RequiresPermissions("system:studentInfo:list")    public Result<ModelBeanMap> list(@RequestParam Map params) {        AdminUserSession adminUserSession = studentInfoService.getAdminUserSession();        if (adminUserSession.isPrincipalAccount()) {            params.put("schoolId", adminUserSession.getUserMap().get("school_id"));        }        return studentInfoService.pagination(params);    }   /* @GetMapping("findById")    @RequiresPermissions("system:studentInfo:update")    public Map findById(Integer id) {        return studentInfoService.findById(id);    }*/    @PostMapping("saveOrUpdate")    @RequiresPermissions(value = {"system:studentInfo:save", "system:studentInfo:update"}, logical = Logical.OR)    public ResultCode saveOrUpdate(@RequestBody ModelBeanMap studentInfoMap) {        boolean updateFlag = false;        Date now = new Date();        if (ObjectUtils.isNotEmpty(studentInfoMap.get("id"))) {           updateFlag = true;           studentInfoMap.put("update_date", now);           studentInfoMap.remove("create_date");        } else {            studentInfoMap.put("create_date", now);        }        AdminUserSession userSession = studentInfoService.getAdminUserSession();        if (userSession.isPrincipalAccount()) {            studentInfoMap.put("school_id", studentInfoService.getAdminUser().get("school_id"));        }        return studentInfoService.saveOrUpdate(updateFlag, studentInfoMap);    }    @DeleteMapping    @RequiresPermissions({"system:studentInfo:deleteById"})    public ResultCode deleteById(@RequestBody ModelBeanMap studentInfoMap) {        return studentInfoService.deleteById(studentInfoMap);    }    /**     * 学员导出     * @param params     */    @PostMapping("exportStudent")    @RequiresPermissions({"system:studentInfo:export"})    public ResultCode exportStudent(@RequestBody Map params) {        return studentInfoService.exportExcel(params);    }    /**     * 学员导入     * @param file     * @return     */    @RequestMapping(value = "importStudent", method = {RequestMethod.GET, RequestMethod.POST})    @RequiresPermissions({"system:studentInfo:import"})    public ResultCode importStudent(@RequestParam MultipartFile file) {        try {            if (!excelTypes.contains(file.getContentType())) {                return new ResultCode(ResultCode.FAIL, "只能导入excel文件");            }            InputStream inputStream = file.getInputStream();            ExcelImportResult importResult = ExcelImportUtil.importExcelMore(inputStream, StudentInfo.class, new ImportParams());            return studentInfoService.importStudentFromExcel(importResult.getList());        } catch (Exception e) {            log.error("学员信息导入失败", e);        }        return new ResultCode(ResultCode.FAIL, "学员信息导入失败");    }    /**     * 获取学员答题信息列表     * @param params     * @return     */    @GetMapping("getStudentCourseOrPaperQuestionInfoList")    public List<ModelBeanMap> getStudentCourseOrPaperQuestionInfoList(@RequestParam Map params) {        return studentInfoService.getStudentCourseOrPaperQuestionInfoList(params);    }    @GetMapping("getStudentPaperList")    public Map getStudentPaperList(@RequestParam Map params) {        return null;        /* AdminUserSession adminUserSession = studentInfoService.getAdminUserSession();        if (adminUserSession.isPrincipalAccount()) {            params.put("schoolId", adminUserSession.getUserMap().get("school_id"));        }        return super.getPageData(params); */    }    /**     * 批改试题     * @param params     * @return     */    @PostMapping("correctStudentQuestionAnswer")    public Map correctStudentQuestionAnswer(@RequestBody Map params) {        return studentInfoService.correctStudentQuestionAnswer(params);    }}