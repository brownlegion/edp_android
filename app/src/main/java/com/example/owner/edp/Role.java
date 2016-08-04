package com.example.owner.edp;

/**
 * Created by Krishna N. Deoram on 2016-04-04.
 * Gumi is love. Gumi is life.
 */
public class Role {

    private static Role role;
    private boolean createUsers, deleteUsers, editUsers, viewUsers, createGrades, deleteGrades, editGrades, viewGrades, addStudents, createCourse, createSection;

    private Role() {

    }

    public static Role getInstance() {
        if (role == null)
            Role.role = new Role();

        return role;
    }

    public boolean isAddStudents() {
        return addStudents;
    }

    public void setAddStudents(boolean addStudents) {
        this.addStudents = addStudents;
    }

    public boolean isCreateCourse() {
        return createCourse;
    }

    public void setCreateCourse(boolean createCourse) {
        this.createCourse = createCourse;
    }

    public boolean isCreateSection() {
        return createSection;
    }

    public void setCreateSection(boolean createSection) {
        this.createSection = createSection;
    }

    public static Role getRole() {
        return role;
    }

    public static void setRole(Role role) {
        Role.role = role;
    }

    public boolean isCreateUsers() {
        return createUsers;
    }

    public void setCreateUsers(boolean createUsers) {
        this.createUsers = createUsers;
    }

    public boolean isDeleteUsers() {
        return deleteUsers;
    }

    public void setDeleteUsers(boolean deleteUsers) {
        this.deleteUsers = deleteUsers;
    }

    public boolean isEditUsers() {
        return editUsers;
    }

    public void setEditUsers(boolean editUsers) {
        this.editUsers = editUsers;
    }

    public boolean isViewUsers() {
        return viewUsers;
    }

    public void setViewUsers(boolean viewUsers) {
        this.viewUsers = viewUsers;
    }

    public boolean isCreateGrades() {
        return createGrades;
    }

    public void setCreateGrades(boolean createGrades) {
        this.createGrades = createGrades;
    }

    public boolean isDeleteGrades() {
        return deleteGrades;
    }

    public void setDeleteGrades(boolean deleteGrades) {
        this.deleteGrades = deleteGrades;
    }

    public boolean isEditGrades() {
        return editGrades;
    }

    public void setEditGrades(boolean editGrades) {
        this.editGrades = editGrades;
    }

    public boolean isViewGrades() {
        return viewGrades;
    }

    public void setViewGrades(boolean viewGrades) {
        this.viewGrades = viewGrades;
    }
}
