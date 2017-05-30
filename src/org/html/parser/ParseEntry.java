package org.html.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ParseEntry implements Cloneable
{
	private static final Logger log = Logger.getLogger(ParseEntry.class
			.getName());
	protected String name;
	protected String flag;
	protected String startToken;
	protected String endToken;
	protected ParseValue parseValue;
	protected boolean jumpStart;						// jump over flat on current line to find startToken
	protected boolean jumpLine;							// jump over line containing flag to find startToken
	protected List<ParseTagList> parseTagLists;
	private List<String> skipListTokens;				// any tokens appear in the block skip to next
	
	
	public ParseEntry( String name, String flag, String startToken, String endToken )
	{
		setName( name );
		setFlag( flag );
		setStartToken( startToken );
		setEndToken( endToken );
		setJumpStart( true );
		parseTagLists = new ArrayList<ParseTagList>();
		skipListTokens = new ArrayList<String>();
	}
	
	@Override
	public Object clone()
	{
		ParseEntry pe = new ParseEntry(new String(name), new String(flag), new String(startToken), new String(endToken));
		for (ParseTagList tpl : parseTagLists)
		{
			pe.getParseTagLists().add((ParseTagList) tpl.clone());
		}
		for (String str : skipListTokens)
		{
			pe.getSkipListTokens().add(new String(str));
		}
		if (getParseValue()!=null)
			pe.setParseValue((ParseValue) getParseValue().clone());
		return pe;
	}
	
	public void addSkipListToken(String tok)
	{
		skipListTokens.add(tok);
	}
	
	public boolean inSkipListTokens(String line)
	{
		for (String tok : skipListTokens)
			if (line.contains(tok))
			{
				log.debug("Line contains skip token " + tok + " Skipping line : " + line);
				return true;
			}
		return false;
				
	}
	
	protected int parseSingleLine( String line, int offset, int endPos ) throws UrlParserException
	{
		int fpos = line.indexOf(flag);
		if (fpos < 0)
				return -1;
		int spos = line.indexOf( startToken, offset );
		if (spos < 0 || spos>endPos)
		{
			return -1;
		}
		else
			log.debug("Found startToken : " + this.startToken + " on line " +line);
		int epos = line.indexOf( endToken, spos+startToken.length() );
		if (epos >= 0)							
		{
			setValue( line.substring( spos+startToken.length(), epos ));
			return epos+endToken.length();
		}
		log.error("Cannot find end token for ParseEntry : " + this); 
		throw new UrlParserException( "Cannot find end token for ParseEntry : " + this );
	}
	
	protected int parse( List<String> lines, int offset, String endBlockToken ) throws UrlParserException
	{
		int cnt = offset;
		String line = "";
		for (; cnt < lines.size(); cnt++)
		{
			line = lines.get(cnt);			
			if (line.contains(endBlockToken))
				return -1;
			if (inSkipListTokens(line))					// don't use 
				return cnt;
			int fpos = 0;
			int spos = -1;
			fpos = line.indexOf(flag);
			if (fpos < 0)
				continue;
			if (isJumpLine())
			{
				int cnt1=cnt;
				for (; cnt1<lines.size(); cnt1++)
				{
					line = lines.get(cnt1);
					spos = line.indexOf( startToken );
					if (spos>=0)
						break;
				}
				cnt = cnt1-1;
			}
			else
			{
				if (isJumpStart())
					line = line.substring( fpos );
				spos = line.indexOf( startToken );
			}
			if (spos < 0)
			{
				log.error("Cannot find Start token for ParseEntry : " + this); 
				throw new UrlParserException( "Cannot find Start token for ParseEntry : " + this );
			}
			else
				log.debug("Found startToken : " + this.startToken + " on line " +line);
			line = line.substring(spos+startToken.length());
			if (endToken.equals("\n"))
			{
				setValue( line );
				return cnt;
			}
			
			int epos = line.indexOf( endToken );
			if (epos >= 0)									// end in same as start
			{
				if (inSkipListTokens(line))					// don't use 
				{
					return cnt;
				}
				setValue( line.substring(0, epos ));
				return cnt;
			}
			else
				break;
		}
		// end not found in current line try compressing the lines together
		cnt++;
		for (; cnt < lines.size(); cnt++)
		{
			String nextLine = lines.get(cnt);
			if (inSkipListTokens(nextLine))					// don't use 
				return cnt;
			if (nextLine.contains(endBlockToken))
				return -1;
			line += nextLine;
			int epos = line.indexOf( endToken );
			if (epos > 0)
			{
				setValue( line.substring(0, epos ));
				return cnt;
			}
		}
		log.error("Cannot find End token for ParseEntry : " + this);
		throw new UrlParserException( "Cannot find End token for ParseEntry : " + this );
	}
	
	
	public void parse( List<String> lines, String endToken ) throws UrlParserException
	{
		log.debug("Parsing lines till endToken : " + endToken);
		
		parse( lines, 0, endToken );
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getStartToken() {
		return startToken;
	}
	
	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}
	
	public String getEndToken() {
		return endToken;
	}
	
	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}
	
	public String getValue( String tag )
	{
		if (parseValue == null)
			return null;
		return parseValue.get( tag );
	}
	
	public String getValue() 
	{
		if (parseValue == null)
			return null;
		return parseValue.get();
	}
	
	
	
	
	
	public void setValue(String text) 
	{
		parseValue = new ParseValue( text );
		if (parseTagLists.size() > 0)
			parseTaggedValues( text );
	}

	private void parseTaggedValues( String text )
	{
		for (ParseTagList ptl : parseTagLists)
		{
			List<String> texts = ptl.split(text);
			for (String text1 : texts)
				log.debug("Found text : " + text1);
			
			for (ParseTag pt : ptl)
			{
				int pos = pt.getCount()-1;
				if (pos >= texts.size())
				{
					log.error("Parse Tag : " + pt.getName() + " in : " + text + " could not resolve");
					break;
				}
				String val = pt.parseValue( texts.get(pos) );
				log.debug("Parsed : " + val);
				parseValue.put( pt.getName(), val );
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isJumpStart() {
		return jumpStart;
	}

	public void setJumpStart(boolean jumpStart) {
		this.jumpStart = jumpStart;
	}

	
	public boolean isJumpLine() {
		return jumpLine;
	}

	public void setJumpLine(boolean jumpLine) {
		this.jumpLine = jumpLine;
	}

	public void addParseTagList(ParseTagList parseTagList)
	{
		parseTagLists.add(parseTagList);
	}
	
	@Override
	public String toString() {
		return "ParseEntry [name = " + name + ", endToken=" + endToken + ", flag=" + flag
				+ ", startToken=" + startToken + ", parseValue=" + parseValue + "]";
	}

	public ParseValue getParseValue() {
		return parseValue;
	}

	public void setParseValue(ParseValue parseValue) {
		this.parseValue = parseValue;
	}

	public List<ParseTagList> getParseTagLists() {
		return parseTagLists;
	}

	public void setParseTagLists(List<ParseTagList> parseTagLists) {
		this.parseTagLists = parseTagLists;
	}

	public List<String> getSkipListTokens() {
		return skipListTokens;
	}

	public void setSkipListTokens(List<String> skipListTokens) {
		this.skipListTokens = skipListTokens;
	}
	
}
