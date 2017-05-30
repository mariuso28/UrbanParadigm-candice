package org.html.parser;

import java.util.HashMap;

public class ParseValue implements Cloneable
{
	private String value;
	private HashMap<String,String> taggedValues;
	
	ParseValue( String text )
	{
		value = text;
		taggedValues = new HashMap<String,String>();
	}
	
	@Override
	public Object clone()
	{
		ParseValue pv = new ParseValue( new String(value) );
		for (String key : taggedValues.keySet())
		{
			String newKey = new String(key);
			String newValue = new String(taggedValues.get(key));
			pv.getTaggedValues().put(newKey, newValue);
		}
		return pv;
	}
	
	public String get()
	{
		return value;
	}
	
	public String get( String tag )
	{
		return taggedValues.get(tag);
	}
	
	public void put( String key, String value)
	{
		taggedValues.put(key, value);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public HashMap<String, String> getTaggedValues() {
		return taggedValues;
	}

	public void setTaggedValues(HashMap<String, String> taggedValues) {
		this.taggedValues = taggedValues;
	}

	@Override
	public String toString() {
		return "ParseValue [value=" + value + ", taggedValues=" + taggedValues
				+ "]";
	}
	
}
