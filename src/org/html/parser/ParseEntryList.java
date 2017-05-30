package org.html.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ParseEntryList extends ParseEntry implements Cloneable
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ParseEntryList.class);
	
	private List<ParseValue> parseValues;

	public ParseEntryList(String name, String flag, String startToken,
			String endToken) {
		super(name, flag, startToken, endToken);
		parseValues = new ArrayList<ParseValue>();
	}

	@Override
	public Object clone()
	{
		ParseEntryList pel = new ParseEntryList(new String(getName()), new String(getFlag()), 
						new String(getStartToken()), new String(getEndToken()));
	
		for (ParseValue pv : getParseValues())
			pel.getParseValues().add(pv);
		return pel;
	}
	
	public List<String> getValues() 
	{
		List<String> values = new ArrayList<String>();
		for (ParseValue pv : parseValues)
			values.add(pv.get());
		return values;
	}
	
	public List<String> getValues(String tag) 
	{
		List<String> values = new ArrayList<String>();
		for (ParseValue pv : parseValues)
			values.add(pv.get(tag));
		return values;
	}

	public String getValue( int offset ) throws UrlParserException
	{
		ParseValue pv = parseValues.get(offset);
		return pv.get();
	}
	
	public String getValue( String tag, int offset ) throws UrlParserException
	{
		ParseValue pv = parseValues.get(offset);
		return pv.get( tag );
	}
	
	public int size()
	{
		return parseValues.size();
	}
	
	public void parse( List<String> lines, String endBlockToken ) throws UrlParserException
	{
		int offset = 0;
		if (lines.get(0).contains(endBlockToken))
		{
			int endPos = lines.get(0).indexOf(endBlockToken);
			while (true)
			{
				parseValue = null;
				offset = super.parseSingleLine( lines.get(0), offset, endPos );
				if (offset<0)
					return;
				if (parseValue!=null)
					parseValues.add(parseValue);
			}
		}
		
		while (true)
		{
			parseValue = null;
			offset = super.parse( lines, offset, endBlockToken );
			if (offset<0)
				break;
			if (parseValue!=null)
				parseValues.add(parseValue);
			
			offset++;
			if (offset >= lines.size())
				break;
		}
	}
	
	
	
	public List<ParseValue> getParseValues() {
		return parseValues;
	}

	public void setParseValues(List<ParseValue> parseValues) {
		this.parseValues = parseValues;
	}

	public String toString() {
		return "ParseEntryList [name = " + name + ", endToken=" + endToken + ", flag=" + flag
				+ ", startToken=" + startToken + ", parseValues=" + parseValues + "]";
	}

	
}
