package org.html.parser;

import java.util.ArrayList;
import java.util.List;


public class ParseTag implements Cloneable
{
	private String name;
	private int count;
	private List<ValueFilter> valueFilters;
	private String scanToValue;

	
	@Override
	public Object clone()
	{
		ParseTag pt = new ParseTag(new String(name),count );
		pt.setScanToValue(new String(scanToValue)); 
		for (ValueFilter vf : valueFilters)
			pt.getValueFilters().add((ValueFilter) vf.clone());
		return pt;
	}
	
	public ParseTag(String name, int count) {
		super();
		this.name = name;
		this.count = count;
		valueFilters = new ArrayList<ValueFilter>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	protected String filterValue(String value)
	{
		if (scanToValue != null)
		{
			int pos = value.indexOf(scanToValue);
			if (pos>0)
				value = value.substring(pos+scanToValue.length());
		}
		for (ValueFilter vf : valueFilters)
		{
			value = value.replaceAll(vf.getTarget(), vf.getSub());
		}
		return value;
	}
	
	
	public String parseValue(String value)
	{
		return filterValue(value);
	}
	
	public void addValueFilter( String source, String dest)
	{
		ValueFilter vf = new ValueFilter(source,dest);
		valueFilters.add(vf);
	}

	public String getScanToValue() {
		return scanToValue;
	}

	public void setScanToValue(String scanToValue) {
		this.scanToValue = scanToValue;
	}

	public List<ValueFilter> getValueFilters() {
		return valueFilters;
	}

	public void setValueFilters(List<ValueFilter> valueFilters) {
		this.valueFilters = valueFilters;
	}
	
	
	
}
