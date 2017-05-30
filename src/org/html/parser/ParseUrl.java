package org.html.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ParseUrl 
{
	private static final Logger log = Logger.getLogger(ParseUrl.class
			.getName());
	
	private String name;
	private UrlParser parser;
	private HashMap<String,ParseBlock> parseBlocks;
	private List<ParseBlock> orderedParseBlocks;
	private String currentField;
	
	public ParseUrl(String name)
	{
		setName( name );
		parseBlocks = new HashMap<String,ParseBlock>();
		orderedParseBlocks = new ArrayList<ParseBlock>();
		setCurrentField("");
	}
	
	public void parseUnordered(String source) throws UrlParserException
	{
		for (ParseBlock parseBlock : orderedParseBlocks)
		{
			setParser( new UrlParser( source ) );							// must go back to start every time
			parseBlock.parse( parser );
		}
	}
	
	public void parse(String source, boolean exitOnFail) throws UrlParserException
	{
		setParser( new UrlParser( source, exitOnFail ) );
		for (ParseBlock parseBlock : orderedParseBlocks)
		{
			parseBlock.parse( parser );
		}
	}
	
	public void parse(String source) throws UrlParserException
	{
		setParser( new UrlParser( source ) );
		for (ParseBlock parseBlock : orderedParseBlocks)
		{
			parseBlock.parse( parser );
		}
	}
	
	private void setParser(UrlParser parser) {
		this.parser = parser;
	}
	
	public void addParseBlock(ParseBlock parseBlock) throws UrlParserException
	{
		int dot = parseBlock.getName().indexOf( '.' );
		if (dot<0)
		{
			parseBlocks.put( parseBlock.getName(), parseBlock );
			orderedParseBlocks.add(parseBlock);
			return;
		}
		ParseBlock parentParseBlock = getParseBlock(  parseBlock.getName().substring(0,dot));
		parentParseBlock.addParseBlock( parseBlock.getName().substring(dot+1), parseBlock );
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String str = "ParseUrl [name =" + name + " parser=" + parser + ", parseBlocks: \n"; 
		for (ParseBlock parseBlock : parseBlocks.values())
			str += parseBlock + "\n";
		return str;
	}
	
	public ParseBlock getParseBlock( String blockName )
	{
		int dot = blockName.indexOf('.');
		ParseBlock parseBlock;
		if (dot < 0)
		{
			parseBlock = ParseBlock.locateBlock( parseBlocks, blockName);
			return parseBlock;
		}
			
		parseBlock = ParseBlock.locateBlock( parseBlocks, blockName.substring(0,dot) );
		return parseBlock.getParseBlock( blockName.substring(dot+1) );
	}
	
	class ParseLookup
	{
		private ParseBlock block;
		private String target;
		
		ParseLookup(String target) throws UrlParserException 
		{
			int dot = target.lastIndexOf('.');
			if (dot < 0)
			{
				log.error("ParseUrl:getValue - malformed target: " + target );
				throw new UrlParserException( "ParseUrl:getValue - malformed target: " + target );
			}
				
			ParseBlock parseBlock = getParseBlock( target.substring(0, dot) );
			if (parseBlock == null)
			{
				log.error("Entry : " + target + " for ParseUrl : " + getName() + " not found" );
				throw new UrlParserException(  "Entry : " + target + " for ParseUrl : " + getName() + " not found" );
			}
			
			setBlock(parseBlock);
			setTarget(target.substring(dot+1));
		}
		
		public ParseBlock getBlock() {
			return block;
		}
		public void setBlock(ParseBlock block) {
			this.block = block;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
	}
	
	private ParseEntry getParseEntry( ParseLookup parseLookup ) throws UrlParserException 
	{
		ParseEntry parseEntry = parseLookup.getBlock().getParseEntry( parseLookup.getTarget() );
		if (parseEntry == null)
		{
			log.error("Entry : " + getCurrentField() + " for ParseUrl : " + getName() + " not found" );
			throw new UrlParserException(  "Entry : " + getCurrentField() + " for ParseUrl : " + getName() + " not found" );
		}
		
		return parseEntry;
	}
	
	private String getValueAtIndex( ParseLookup parseLookup ) throws UrlParserException
	{
		String target = parseLookup.getTarget();
		int epos = target.lastIndexOf(']');
		int spos = target.lastIndexOf('[');
		int index = Integer.parseInt(target.substring(spos+1,epos));
		target = target.substring(0,spos);
		ParseEntryList pel = null;
		try
		{
			int pos = target.indexOf(':');
			if (pos < 0)
			{
				parseLookup.setTarget(target);
				pel = (ParseEntryList) getParseEntry(parseLookup);
				return pel.getValue( index );
			}
			
			parseLookup.setTarget(target.substring(0,pos));
			pel = (ParseEntryList) getParseEntry( parseLookup );
			return pel.getValue( target.substring(pos+1), index );
		}
		catch (ClassCastException e)
		{
			return null;
		}
	}
	
	public String getValue( String target ) throws UrlParserException 
	{
		setCurrentField(target);
		
		ParseLookup parseLookup = new ParseLookup(target);	
		target = parseLookup.getTarget();
		int pos = target.lastIndexOf(']');
		if (pos > 0)
			return getValueAtIndex( parseLookup );
		
		pos = target.indexOf(':');
		if (pos < 0)
			return getParseEntry(parseLookup).getValue();
		
		parseLookup.setTarget(target.substring(0,pos));
		ParseEntry pe = getParseEntry( parseLookup );
		
		return pe.getValue( target.substring(pos+1) );
	}
	
	public List<String> getValues( String target ) throws UrlParserException 
	{
		setCurrentField(target);
		ParseLookup parseLookup = new ParseLookup(target);	
		target = parseLookup.getTarget();
		ParseEntryList pel = null;
		try
		{
			int pos = target.indexOf(':');
			if (pos < 0)
			{
				pel = (ParseEntryList) getParseEntry(parseLookup);
				return pel.getValues();
			}
			
			parseLookup.setTarget(target.substring(0,pos));
			pel = (ParseEntryList) getParseEntry( parseLookup );
			return pel.getValues( target.substring(pos+1) );
		}
		catch (ClassCastException e)
		{
			List<String> texts = new ArrayList<String>();
			texts.add( pel.getValue());
			return texts;
		}
	}
	
	private int getEntrySize( String target ) throws UrlParserException
	{
		setCurrentField(target);
		ParseBlock parseBlock = getParseBlock( target );		// for block list
		if (parseBlock != null)
		{
			try
			{
				ParseBlockList parseBlockList = (ParseBlockList) parseBlock;
				return parseBlockList.getBlocks().size();
			}
			catch (ClassCastException e)
			{
				return 1;
			}
		}
		ParseEntryList pel = null;
		try
		{
			ParseLookup parseLookup = new ParseLookup(target);	
			target = parseLookup.getTarget();
			int pos = target.indexOf(':');
			if (pos < 0)
			{
				pel = (ParseEntryList) getParseEntry(parseLookup);
				return pel.getValues().size();
			}
			parseLookup.setTarget(target.substring(0,pos));
			pel = (ParseEntryList) getParseEntry( parseLookup );
			return pel.getValues( target.substring(pos+1) ).size();
		}
		catch (ClassCastException e)
		{
			return 1;
		}
	}

	public int getSize( String target ) throws UrlParserException
	{
		ParseBlockList pbl = (ParseBlockList) getParseBlock(target);
		if (pbl==null)
			return getEntrySize(target);
		return pbl.getBlocks().size();
	}
	
	public void setCurrentField(String currentField) {
		this.currentField = currentField;
	}

	public String getCurrentField() {
		return currentField;
	}

	public UrlParser getParser() {
		return parser;
	}

	
}
