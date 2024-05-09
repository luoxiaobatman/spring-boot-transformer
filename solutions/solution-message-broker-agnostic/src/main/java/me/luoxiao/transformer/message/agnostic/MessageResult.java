package me.luoxiao.transformer.message.agnostic;

public enum MessageResult {
	SUCCESS("SUCCESS", "成功"),
	FAILED("FAILED", "失败");

	private String code;
	private String msg;

	MessageResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
