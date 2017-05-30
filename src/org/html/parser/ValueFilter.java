package org.html.parser;

public class ValueFilter implements Cloneable{

	private String target;
	private String sub;
	
	@Override
	public Object clone()
	{
		return new ValueFilter(new String(target),new String(sub));
	}
		
	ValueFilter(String target,String sub)
	{
		setTarget(target);
		setSub(sub);
	}
		
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}

}
