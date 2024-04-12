package cn.pushu.mutipledynamicdatasource.controller;

import cn.pushu.mutipledynamicdatasource.dao.CourseMapper;
import cn.pushu.mutipledynamicdatasource.entity.Course;
import cn.pushu.mutipledynamicdatasource.service.DataSourceContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> CourseControoler<br>
 * <b>projectName:</b> myTest<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/1210:45
 */
@RestController
public class CourseController {

     @Resource
     private CourseMapper courseMapper;

     @GetMapping("/list_course")
     public   List<Course> listCourse(String ds){
          DataSourceContextHolder.setDataSourceKey(ds);
          List<Course> courses = courseMapper.listCourse();
          DataSourceContextHolder.clearDataSourceKey();
          return courses;
     }

}
