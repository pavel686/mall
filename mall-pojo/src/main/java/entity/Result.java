package entity;

import java.io.Serializable;

public class Result implements Serializable {
	
	private boolean success; //判断是否操作成功
	private String message; //操作成功与否的信息
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Result(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

}
