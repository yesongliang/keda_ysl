package com.kedacom.ysl.model;

import java.util.Arrays;

/**
 * 用户类
 * 
 * @author ysl
 * 
 */
public class User {
    /** 惟一标识 **/
    private String id;
    /** 用户名 **/
    private String username;
    /** 密码 **/
    private String password;
    /** 邮箱 **/
    private String email;
    /** 团队 **/
    private String groups;
    /** 权限 **/
    private String power;
    /** 头像 **/
    private byte[] photo;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getGroups() {
	return groups;
    }

    public void setGroups(String groups) {
	this.groups = groups;
    }

    public String getPower() {
	return power;
    }

    public void setPower(String power) {
	this.power = power;
    }

    public byte[] getPhoto() {
	return photo;
    }

    public void setPhoto(byte[] photo) {
	this.photo = photo;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", groups=" + groups + ", power=" + power + ", photo=" + Arrays.toString(photo) + "]";
    }

}
