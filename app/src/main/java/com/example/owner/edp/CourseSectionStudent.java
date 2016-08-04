package com.example.owner.edp;

/**
 * Created by Krishna N. Deoram on 2016-04-08.
 * Gumi is love. Gumi is life.
 */
public class CourseSectionStudent {

    private String courseCode, courseId, courseTitle;
    private String sectionNumber, sectionId, semester, year, taId, taUsername;
    private String userId, username, firstname, lastname, courseSectionId, courseSectionStudentId;

    public CourseSectionStudent(String courseCode, String courseId, String courseTitle) { //Type 1
        this.courseCode = courseCode;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public CourseSectionStudent(String sectionNumber, String sectionId, String semester, String year) { //Type 2
        this.sectionNumber = sectionNumber;
        this.sectionId = sectionId;
        this.semester = semester;
        this.year = year;
    }

    public CourseSectionStudent(String courseSectionId, String courseId, String courseCode, String sectionId, String sectionNumber, String taId, //Type 3
                                String taUsername) {
        this.courseSectionId = courseSectionId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.sectionId = sectionId;
        this.sectionNumber = sectionNumber;
        this.taId = taId;
        this.taUsername = taUsername;
    }

    public CourseSectionStudent(String courseSectionStudentId, String userId, String username, String firstname, String lastname, String courseSectionId,
                                String courseCode, String sectionNumber) { //Type 4
        this.courseSectionStudentId = courseSectionStudentId;
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.courseSectionId = courseSectionId;
        this.courseCode = courseCode;
        this.sectionNumber = sectionNumber;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public String getTaId() {
        return taId;
    }

    public String getTaUsername() {
        return taUsername;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getCourseSectionId() {
        return courseSectionId;
    }

    public String getCourseSectionStudentId() {
        return courseSectionStudentId;
    }
}
