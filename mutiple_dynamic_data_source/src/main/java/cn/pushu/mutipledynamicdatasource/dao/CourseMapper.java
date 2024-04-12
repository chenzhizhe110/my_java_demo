package cn.pushu.mutipledynamicdatasource.dao;

import cn.pushu.mutipledynamicdatasource.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> CourseMapper<br>
 * <b>projectName:</b> myTest<br>
 * <b>description:</b> TODO <br>
 * <b>date:</b> 2024/4/1210:36
 */
@Mapper
public interface CourseMapper {

    @Select("SELECT * FROM course_1")
    List<Course>  listCourse();
}
