package org.html.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ParseBlock implements Cloneable{
	
	private static final Logger log = Logger.getLogger(ParseBlock.class);
	
	private String name;
	private String startToken;
	private String endToken;
	private List<String> lines;
	private String breakToken;
	private HashMap<String,ParseEntry> parseEntries;
	private HashMap<String,ParseBlock> parseBlocks;
	private List<ParseBlock> orderedParseBlocks;
	private List<String> ignoreBlockTokens;
	
	@Override
	public Object clone()
	{
		ParseBlock pb = new ParseBlock(getName());
		copyValues(pb);
		return pb;
	}
	
	public void copyValues(ParseBlock pb)
	{
		pb.setStartToken(new String(getStartToken()));
		pb.setEndToken(new String(getEndToken()));
		for (String str : getLines())
			pb.getLines().add(new String(str));
		if (getBreakToken()!=null)
			pb.setBreakToken(new String(getBreakToken()));
		for (ParseEntry pe : getParseEntries().values())
		{
			ParseEntry npe = (ParseEntry) pe.clone();
			pb.getParseEntries().put(npe.getName(),npe);
		}
		for (ParseBlock nblock : getOrderedParseBlocks())
		{
			ParseBlock newBlock = (ParseBlock) nblock.clone();
			pb.getParseBlocks().put(newBlock.getName(), newBlock);
			pb.getOrderedParseBlocks().add(newBlock);
		}
		
		for (String tok : getIgnoreBlockTokens())
		{
			pb.getIgnoreBlockTokens().add(new String(tok));
		}
	}
	
	public ParseBlock(String name)
	{
		setName( name );
		parseEntries = new HashMap<String,ParseEntry>();
		parseBlocks = new HashMap<String,ParseBlock>();
		setOrderedParseBlocks(new ArrayList<ParseBlock>());
		lines = new ArrayList<String>();
		ignoreBlockTokens = new ArrayList<String>();
	}
	
	public static ParseBlock locateBlock( HashMap<String,ParseBlock> parseBlocks,  String blockName  )
	{
		int bracket = blockName.indexOf('[');
		if (bracket<0)
			return parseBlocks.get( blockName );
		int close = blockName.indexOf(']');
		if (close<bracket)
			return null;
		String indexStr = blockName.substring(bracket+1,close);
		blockName = blockName.substring(0,bracket);
		int index = Integer.parseInt(indexStr);
		ParseBlockList pb = (ParseBlockList) parseBlocks.get( blockName );
		if (pb==null)
			return null;
		if (index>=pb.getBlocks().size())
			return null;
		return pb.getBlocks().get(index);
	}
	
	
	public void addIgnoreBlockToken(String token)
	{
		ignoreBlockTokens.add(token);
	}
	
	protected List<String> grabLines(List<String> lines) throws UrlParserException
	{
		List<String> blockLines = new ArrayList<String>();
		char status = 'S';
		for (String line : lines)
		{
			if (status == 'S')		// seeking start
			{
				int sPos = line.indexOf(startToken);
			//	log.info("Found start token");
				if (sPos>=0)
				{
					status = 'E';
					line = line.substring(sPos+startToken.length());
					blockLines.add(line);
					if (line.contains(endToken))
					{
						log.info("Found end token");
						//if (line.indexOf(endToken)>sPos)
						{
							status = 'K';
							break;
						}
					}
				}
				continue;
			}
			if (status == 'E')		// seeking end
			{
				blockLines.add(line);
				if (line.contains(endToken))
				{
		//			log.info("Found end token");
					status = 'K';
					break;
				}
			}
		}
		if (status != 'K')
			if (status == 'S')
			{
				if (this.getClass().equals(ParseBlockList.class))
					return new ArrayList<String>();
				log.error("Block startToken : [" + startToken + "] not found in block : " + name);
				throw new UrlParserException( "Block startToken : [" + startToken + "] not found in block : " + name );
			}
			else
			{
				log.error("Block startToken : [" + endToken + "] not found in block : " + name);
				throw new UrlParserException( "Block endToken : [" + endToken +  "] not found in block : " + name );
			}
		
		if (breakToken != null)
			blockLines = breakLines(blockLines);
		return blockLines;
	}
	
	private List<String> breakLines(List<String> lines) throws UrlParserException
	{
		List<String> brokenLines = new ArrayList<String>();
		
		for (String line : lines)
		{
			String[] blines = line.split(breakToken);
			for (String bline : blines)
				brokenLines.add(bline);
		}
		return brokenLines;
	}

	public void parse() throws UrlParserException
	{
		for (ParseBlock subBlock : orderedParseBlocks )
		{
			subBlock.setLines( subBlock.grabLines( lines ) ); 
			subBlock.parse();
		}
		
//		setLines( this.grabLines( lines ) ); 
		if (lines.isEmpty())
		{
			log.error("No Lines for ParseBlock : " + this);
			throw new UrlParserException( "No Lines for ParseBlock : " + this );
		}
		for (ParseEntry parseEntry : parseEntries.values() )
			parseEntry.parse( lines, endToken );
	}
	
	public ParseBlock getParseBlock(String blockName)
	{
		int dot = blockName.indexOf('.');
		if (dot < 0)
		{
			ParseBlock parseBlock = ParseBlock.locateBlock( parseBlocks, blockName );
			return parseBlock;
		}
		ParseBlock parseBlock = ParseBlock.locateBlock( parseBlocks, blockName.substring(0,dot) );
		if (parseBlock == null)
			return null;
		return parseBlock.getParseBlock(blockName.substring(dot+1));
	}
	
	public void addParseBlock(String name, ParseBlock childBlock) throws UrlParserException
	{
		int dot = name.indexOf('.');
		if (dot < 0)
		{
			parseBlocks.put( name, childBlock );
			orderedParseBlocks.add(childBlock);
			return;
		}
		ParseBlock parentChildBlock = getParseBlock( name.substring(0,dot) );
		if (parentChildBlock == null)
		{
			log.error("ParentBlock for ParseBlock : " + childBlock.getName() + " not found");
			throw new UrlParserException(  "ParentBlock for ParseBlock : " + childBlock.getName() + " not found" );
		}
		parentChildBlock.addParseBlock( name.substring(dot+1), childBlock );
	}

	public void parse( UrlParser parser ) throws UrlParserException
	{
		log.info("Parsing block for startToken : " + startToken );
		log.info("Parsing block for endToken: " + endToken);
		lines = parser.grabLines( startToken, endToken);
		
		log.debug(this.toString());
		parse();
	}
	
	public void addParseEntry( ParseEntry parseEntry )
	{
		parseEntries.put( parseEntry.getName(), parseEntry );
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String toString(int tab) 
	{
		String tabStr ="";
		for (; tab>0; tab--)
			tabStr+="\t";
		String str = tabStr + "ParseBlock [name = " + name + ", startToken=" + startToken + ", endToken="
				+ endToken + "]\n";
		if (parseEntries.size() > 0)
		{
			str += tabStr + "Entries:\n";
			for (ParseEntry parseEntry : parseEntries.values())
				str += tabStr + "\t" + parseEntry + "\n";
		}
		if (parseBlocks.size() > 0)
		{
			str += tabStr + "Sub Blocks:\n";
			for (ParseBlock parseBlock : parseBlocks.values())
				str += tabStr + "\t" + parseBlock.toString(tab) + "\n";
		}
		
		str += "\n\nLINES:\n";
		for (String line : lines)
		{
			str += line + "\n";
		}
		return str;
	}
	
	public ParseEntry getParseEntry( String entry )
	{
		return parseEntries.get(entry);
	}
	

	protected void setLines(List<String> lines) 
	{	
		this.lines = lines;	
	}

	public void setBreakToken(String breakToken) {
		this.breakToken = breakToken;
	}

	public String getBreakToken() {
		return breakToken;
	}

	@Override
	public String toString() {
		return toString(0);
	}

	public void setOrderedParseBlocks(List<ParseBlock> orderedParseBlocks) {
		this.orderedParseBlocks = orderedParseBlocks;
	}

	public List<ParseBlock> getOrderedParseBlocks() {
		return orderedParseBlocks;
	}

	public HashMap<String, ParseEntry> getParseEntries() {
		return parseEntries;
	}

	public void setParseEntries(HashMap<String, ParseEntry> parseEntries) {
		this.parseEntries = parseEntries;
	}

	public HashMap<String, ParseBlock> getParseBlocks() {
		return parseBlocks;
	}

	public void setParseBlocks(HashMap<String, ParseBlock> parseBlocks) {
		this.parseBlocks = parseBlocks;
	}

	public List<String> getIgnoreBlockTokens() {
		return ignoreBlockTokens;
	}

	public void setIgnoreBlockTokens(List<String> ignoreBlockTokens) {
		this.ignoreBlockTokens = ignoreBlockTokens;
	}

	public List<String> getLines() {
		return lines;
	}
	
	
	
}
