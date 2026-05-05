package com.smartmind.biz.bo.dto.classcourse;

import java.util.List;

/**
 * DTO for saving relation
 */
public  class CourseClassRelationDto {
    private Long courseId;
    private List<Long> classIds;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<Long> classIds) {
        this.classIds = classIds;
    }
}
