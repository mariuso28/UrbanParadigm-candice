package org.html.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ParseBlockList extends ParseBlock implements Cloneable{

	private static final Logger log = Logger.getLogger(ParseBlockList.class);
	private List<ParseBlock> blocks;
	
	@Override
	public Object clone()
	{
		ParseBlockList pbl = new ParseBlockList(getName());
		super.copyValues(pbl);
		for (ParseBlock pb : getBlocks())
		{
			pbl.getBlocks().add((ParseBlock) pb.clone());
		}
		return pbl;
	}
	
	public ParseBlockList(String name) {
		super(name);
		blocks = new ArrayList<ParseBlock>();
	}
	
	@Override
	public List<String> grabLines(List<String> lines) throws UrlParserException
	{
		return lines;							// this code is horrible
	}

	public void parse() throws UrlParserException
	{
		List<String> lines = getLines();
		while (true)
		{
			ParseBlock pb = new ParseBlock(this.getName());
			copyValues(pb);
			List<String> gLines = super.grabLines( lines );
			if (gLines.isEmpty())
			{
				log.info("Finished with parse block list # blocks = " + blocks.size());
				
				return;
			}
			pb.setLines( gLines ); 
			pb.parse();
			
			blocks.add(pb);
			
			String lastBlockLine = pb.getLines().get(pb.getLines().size()-1);
			if ((lines = skipToNextStartBlock(lines,lastBlockLine)) == null)
				return;
			setLines(lines);
		}
	}
	
	private List<String> skipToNextStartBlock(List<String> lines,String lastBlockLine) {
		
		for (int c=0; c<lines.size(); c++)
			if (lines.get(c).endsWith(lastBlockLine))				// the start token is stripped
				return lines.subList(c+1,lines.size());
		return null;
	}

	public void parse( UrlParser parser ) throws UrlParserException
	{// FIX FIX FIX - UNTESTED NO TEST CASE
		
		while (true)
		{
			List<String> gLines = parser.grabLines( getStartToken(), getEndToken() );
			if (gLines.isEmpty())
			{
				log.info("Finished with parse block list");
				return;
			}
			setLines( gLines ); 
			super.parse();

		}
	}
	
	public void setBlocks(List<ParseBlock> blocks) {
		this.blocks = blocks;
	}

	public List<ParseBlock> getBlocks() {
		return blocks;
	}

	
}
