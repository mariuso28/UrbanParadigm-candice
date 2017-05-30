package org.cz.scrape;

import org.apache.log4j.Logger;
import org.html.parser.ParseBlock;
import org.html.parser.ParseEntry;
import org.html.parser.ParseUrl;
import org.html.parser.UrlParserException;

public class SecurityGrab{

	private static final Logger log = Logger.getLogger(SecurityGrab.class);
	private ParseUrl url;
	private String sourceBase;
	private String code;
	private String ticker;
	private String name;
	private String error;
	
	public SecurityGrab(String code) throws Exception
	{
		extract("http://klse.i3investor.com/quoteservlet.jsp?sa=ss&q=" + code);
	}
	
	public void setSourceBase(String sourceBase) {
		this.sourceBase = sourceBase;
	}

	public String getSourceBase() {
		return sourceBase;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	private void setup() throws UrlParserException
	{
	    url = new ParseUrl( "sss" );

		ParseBlock parseBlock = getParseSecurity();
		url.addParseBlock(parseBlock);
	}
	
	private ParseBlock getParseSecurity() throws UrlParserException
	{
		// <title>ACCSOFT-WA (0018WA): ORIENTED MEDIA GROUP BERHAD - Overview | I3investor</title>
		
		ParseBlock parseBlock = new ParseBlock( "security" );

		parseBlock.setStartToken( "<head>" );
		parseBlock.setEndToken("</head>");
	
		ParseEntry pe = new ParseEntry( "info", "<title>", "<title>", " - Overview");
		parseBlock.addParseEntry(pe);
		
		return parseBlock;
	}
	
	private void extract(String sourceBase) throws Exception
	{
		setSourceBase(sourceBase);
		setup();											
		url.parse(sourceBase);

		String info = url.getValue("security.info");
		
		log.info("Info: " + info);
		
		// 3A (0012): THREE-A RESOURCES BHD
		if (info == null)
		{
			error = "Code not found";
			return;
		}
		
		try
		{
			int pos1 = info.indexOf('(');
			int pos2 = info.indexOf(')',pos1);
			ticker = info.substring(0,pos1).trim();
			code = info.substring(pos1+1,pos2).trim();
			name = info.substring(pos2+2).trim();
		}
		catch (Exception e)
		{
			error = e.getMessage();
		}
	}
	
	@Override
	public String toString() {
		return "SecurityGrab [code=" + code + ", ticker=" + ticker + ", name=" + name + ", error=" + error + "]";
	}

	public static void main(String[] args)
	{
		try
		{
			SecurityGrab pfx = new SecurityGrab("COMPOSITE");
			log.info(pfx.toString());
		}
		catch (Exception e)
		{
			log.error(e + " : " + e.getMessage());
		}
	}

}

