package org.cz.json.message;

public class CzResultJson {

	private CzStatusJson status;
	private String message;
	private Object result;

	public CzResultJson() {}

	public void fail(String message)
	{
		setStatus(CzStatusJson.ERROR);
		setMessage(message);
	}
	
	public void success(Object result)
	{
		setStatus(CzStatusJson.OK);
		setResult(result);
	}
	
	public void success()
	{
		setStatus(CzStatusJson.OK);
	}
	
	public CzStatusJson getStatus() {
		return status;
	}

	public void setStatus(CzStatusJson status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	
}