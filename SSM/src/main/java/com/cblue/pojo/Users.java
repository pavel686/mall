package com.cblue.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Users implements Serializable {
    private Integer id;

    @NotBlank(message="{username.error}")
    private String username;
    
    @Length(min=6,max=15,message="{userpass.error}")
    private String userpass;

    private Date birthday;

    private String image;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass == null ? null : userpass.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", userpass="
				+ userpass + ", birthday=" + birthday + ", image=" + image
				+ "]";
	}
   
}