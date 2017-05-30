package org.html.parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class UrlParser
{	
	private static final Logger log = Logger.getLogger(UrlParser.class
			.getName());
	private String source;
	private BufferedReader bins;
	private boolean eof;
	private String currLine;
	private boolean exitOnFail;
	
	public UrlParser( String source ) throws UrlParserException
	{
		this( source, false);
	}
	
	public UrlParser( String source, boolean exitOnFail ) throws UrlParserException
	{
		setSource( source );
		setExitOnFail(exitOnFail);
		URL url = null;
		log.info("Reading URL [" + source + "]" );
		try
		{
			url = new URL( source );
			connectUrl(url);
			setEof( false );
		}
		catch (MalformedURLException e)
		{
			log.error( e );
			log.info("Attempting to connect as file:" + source );
			try
			{
				File file = new File(source);                
				InputStreamReader ins = new InputStreamReader( new FileInputStream(file) );
				bins = new BufferedReader( ins );
				setEof( false );
			}
			catch (Exception e1)
			{
				log.error( e1 );
				throw new UrlParserException( e1.getMessage() );
			}
		}
	}

	private void connectUrl(URL url) throws UrlParserException
	{
		int tries = 0;
		while (true)
		{
			int backoff = 5;
			try
			{
				URLConnection connection  = url.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				InputStreamReader ins = new InputStreamReader( connection.getInputStream() );
				bins = new BufferedReader( ins );
				break;
			}
			catch (IOException e)
			{
				if (exitOnFail)
				{
					e.printStackTrace();
					throw new UrlParserException( "Connection to : " + url + " - failed" );
				}
				e.printStackTrace();
				log.warn( "Couldn't connect - backing off from : " + url + "for : " + backoff + " secs");
				try
				{
					Thread.sleep( backoff*1000 );
					if (tries++ > 5)
						throw new UrlParserException( "Maximum tries to connect exceeded" );
				}
				catch (InterruptedException ie)
				{
					log.error( ie );
					throw new UrlParserException( ie.getMessage() );
				}
				// backoff+=backoff;
			}
		}
	}
	
	private void setEof() throws UrlParserException
	{
		setEof( true );
		close();
	}
	
	public String getCurrLine()
	{
		return currLine;
	}
	
	public void close()
	{
		try
		{
			bins.close();
		}
		catch (IOException e)
		{
			;
		}
	}
	
/*
	String parseToken( String line, String firstTok, String lastTok ) 
					throws UrlParserException
	{
		int pos1 = line.indexOf( firstTok );
		int pos2 = line.indexOf( lastTok );
		if (pos1 < 0 || pos2 < 0)
			return null;
		return line.substring( pos1 + firstTok.length(), pos2 ).trim();
	}	
	
	public String extractToken( String flagTok, String firstTok, String lastTok ) 
					throws UrlParserException
	{
		int fpos = currLine.indexOf( flagTok );
		if (fpos < 0)
			return null;
		String line = currLine.substring( fpos );
		int pos1 = line.indexOf( firstTok );
		int pos2 = line.indexOf( lastTok );
		if (pos1 < 0 || pos2 < 0)
			return null;
		return line.substring( pos1 + firstTok.length(), pos2 );
	}

	public String parseToken( String firstTok, String lastTok ) 
					throws UrlParserException
	{
		String line;
		while (true) 
		{	
			line = readLine();
			// i.e <TITLE>PENANG, Sunday 3 July 2005</TITLE></HEAD>
			if (line.startsWith( firstTok ))
			{
				return parseToken( line, firstTok, lastTok ); 
			}
		}
	}
	
	public String parseTokenAnyColumn( String firstTok, String lastTok ) 
					throws UrlParserException
	{// the first token can be any where in the string
		String line;
		while (true) 
		{	
			line = readLine();
			if (line.indexOf( firstTok ) >= 0)
			{
				return parseToken( line, firstTok, lastTok ); 
			}
		}
	}
	
	public String parseTokenEmbedded( String flagTok, String firstTok, String lastTok ) 
					throws UrlParserException
	{
		String line;
		while (true) 
		{	
			line = readLine();
			// move to flag token then extract between first and last
			if (line.startsWith( flagTok ))
			{
				return parseToken( line, firstTok, lastTok ); 
			}
		}
	}
	*/
	
	public String readLine() throws UrlParserException
	{
		int backOff = 2;
		while (true)
		{
			try
			{
				currLine = bins.readLine();
//				log.info( currLine );
				if (currLine == null)
				{
					setEof();
					throw new UrlParserException( "EOF" );
				}
				currLine = currLine.trim();
				return currLine;
			}
			catch (IOException e)
			{
				if (backOff > 5*60)
					throw new UrlParserException( e.getMessage() );
				System.out.println( e.getMessage() );
				System.out.println( "Backing off and retrying in : " +
								backOff + " secs" );
				try
				{
					Thread.sleep( backOff * 1000 );
				}
				catch (Exception ex)
				{
					throw new UrlParserException( ex.getMessage() );
				}
				backOff *= backOff;	
			}	
		}
	}
	
	public String readLine( int num ) throws UrlParserException
	{
		for (; num>0; num--)
			readLine();
		return currLine;
	}
	
	private String skipOn( String token ) throws UrlParserException
	{
		log.trace( "##############" );
		log.trace( "SKIPPING ON TO : " + token );
		log.trace( "##############" );
		
		do
		{
			readLine();
			log.trace( currLine );
			if (currLine == null)
			{
				setEof();
				throw new UrlParserException( "EOF" );
			}
		}
		while (currLine.indexOf( token ) < 0);
		log.trace( "##############" );
		log.trace( "SKIPPING ON TO RETURNING : " + currLine );
		log.trace( "##############" );

		return currLine;	
	}
	
	public String skipTo( String token ) throws UrlParserException
	{
		log.trace( "##############" );
		log.trace( "SKIPPING ON TO : " + token );
		log.trace( "##############" );
		
		if (currLine != null && currLine.indexOf( token ) >=0)
		{
			log.trace( "##############" );
			log.trace( "SKIPPING ON TO RETURNING : " + currLine );
			log.trace( "##############" );			
			return currLine;
		}
		return skipOn( token );
	}
	
	public String skipToFirstOf( String[] tokens ) throws UrlParserException
	{// skip to the first appearance of any one of the tokens
		log.trace( "##############" );
		log.trace( "SKIPPING TO FIRST : " + tokens[0] + tokens[1] );
		log.trace( "##############" );
		
		while (true)
		{
			log.trace( "FOUND : " + currLine );
			if (currLine == null)
			{
				setEof();
				throw new UrlParserException( "EOF" );
			}
			for (int i=0; i<tokens.length; i++)
				if (currLine.indexOf( tokens[i] ) >= 0)
				{
					log.trace( "##############" );
					log.trace( "SKIPPING TO FIRST RETURNING : " + currLine );
					log.trace( "##############" );
					
					return currLine;
				}
			readLine();
		}
	}

	public String[] getStrings( String startMode, String endMode, String lastToken ) throws UrlParserException
	{
		/* search file for eg:
		<FONT SIZE=1 FACE="System">O SIDEK<BR>
		W YEO<BR>
		TAN S H<BR>
		YB LEE<BR>
		WONG KF<BR>
		P PEREIRA<BR>
		KH LIM<BR>
		YN LIEW<BR>
		L KHOO<BR>
		TAN S H</FONT>      
		here StartMode is "<FONT" end mode is "</FONT>"
		separators are lastToken is "<"
		*/
		Vector<String> v = new Vector<String>();
		int spos, epos;
		boolean atStart = true;
		while ((spos = currLine.indexOf( startMode )) < 0)
		{
			readLine();
		}
		while (true)
		{
			v.add( currLine );
			epos = currLine.indexOf( endMode );
			
			if (epos >= 0)			// at end;
			{
				if (!atStart || (atStart && epos > spos))
				{
					break;
				}
			}
			readLine();
			atStart = false;
		}
		
		
		return stripStrings( v, lastToken, epos );
	}

	public String[] getStrings( String startMode, String endMode, String lastToken, int minTokNum ) throws UrlParserException
	{
		/* search file for eg:
		<FONT SIZE=1 FACE="System">O SIDEK<BR>
		W YEO<BR>
		TAN S H<BR>
		YB LEE<BR>
		WONG KF<BR>
		P PEREIRA<BR>
		KH LIM<BR>
		YN LIEW<BR>
		L KHOO<BR>
		TAN S H</FONT>      
		here StartMode is "<FONT" end mode is "</FONT>"
		separators are lastToken is "<"
		*/
		Vector<String> v = new Vector<String>();
		int spos, epos;
		boolean atStart = true;
		while ((spos = currLine.indexOf( startMode )) < 0)
		{
			readLine();
		}
		while (true)
		{
			minTokNum--;
			v.add( currLine );
			epos = currLine.indexOf( endMode );
			if (epos >= 0)			// at end;
			{
				if (minTokNum <= 0)
				{
					if (!atStart || (atStart && epos > spos))
					{
						break;
					}
				}
			}
			readLine();
			atStart = false;
		}
		return stripStrings( v, lastToken, epos );
	}

	private String[] stripStrings( Vector<String> v, String lastToken, int epos )
	{
		String strs[] = new String[v.size()];
		if (strs.length == 0)
			return strs;
		
		int i=0;
		for ( ; i<v.size()-1; i++)
		{
			String str = (String) v.elementAt( i );
			log.trace( str );
			int endPos = str.lastIndexOf( lastToken );
			if (endPos < 0)
				System.out.println( str );
			
			str = str.substring( 0, endPos );
			int startPos = str.lastIndexOf( '>' );
			if (startPos > 0)		// delimited
				str = str.substring( startPos + 1 );
			strs[i] = str;		
		}
		String str = (String) v.elementAt( i );
		str = str.substring( 0, epos );
		int startPos = str.lastIndexOf( '>' );
		if (startPos > 0)		// delimited
			str = str.substring( startPos + 1 );
		strs[i] = str;		
		return strs;
	}

	static int parseInt( String tok ) throws NumberFormatException
	{
		try
		{
			tok = tok.trim();
			return Integer.parseInt( tok );	
		}
		catch (NumberFormatException e)
		{
			// sometimes followed by alpha - get rid try again
			String str = "";
			for (int i=0; i<tok.length(); i++)
			{
				char ch = tok.charAt(i);
				if (!Character.isDigit(ch))
					break;
				str += ch;
			}
			if (str.length() <= 0)
				return -1;
			return Integer.parseInt( str );
		}
	}
	
	static double parseDouble( String tok ) throws NumberFormatException
	{
		try
		{
			return Double.parseDouble( tok );	
		}
		catch (NumberFormatException e)
		{
			// sometimes followed by alpha - get rid try again
			String str = "";
			for (int i=0; i<tok.length(); i++)
			{
				char ch = tok.charAt(i);
				if (!Character.isDigit(ch))
					break;
				str += ch;
			}
			if (str.length() <= 0)
				return -1.0;
			return Double.parseDouble( str );
		}
	}

	private void diag( String str )
	{
		log.debug(str);
		System.out.println( "Hit a key.." );
		try
		{
			BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
			br.readLine();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String[] tokenize( String toks, char delim )
	{
		diag( toks );
		Vector<String> v = new Vector<String>();
		String sb = "";
		for (int i=0; i<toks.length(); i++)
		{
			if (toks.charAt(i) == delim)
			{
				v.add( sb );
				sb = "";
				continue;
			}
			sb += toks.charAt(i);
		}
		String[] strs = new String[v.size()];
		for (int i=0; i<strs.length; i++)
			strs[i] = (String) v.elementAt( i );
		return strs;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isEof() {
		return eof;
	}

	public void setEof(boolean eof) {
		this.eof = eof;
	}

	public void setCurrLine(String currLine) {
		this.currLine = currLine;
	}

	@Override
	public String toString() {
		return "UrlParser [source=" + source + ", bins=" + bins + ", currLine="
				+ currLine + ", eof=" + eof + "]";
	}
	
	public List<String> grabLines( String startToken, String endToken ) throws UrlParserException
	{
		log.info("grabLines with : " + startToken + " end: " + endToken);
		List<String> block = new ArrayList<String>();
		String line = null;
		if (currLine != null)
			line = currLine;
		else
			line = readLine();
		boolean sameLine = false;
		while (true)
		{
			if (line.contains(startToken))
			{
				log.info("found start token");
				if (line.contains(endToken))
				{
					log.info("found end token");
					int endPos = line.indexOf(endToken);
					if (endPos > line.indexOf(startToken))
					{
						block.add(line);
						currLine = null;
						return block;
					}
					else
					{
						block.add(line.substring(endPos+endToken.length()));		// throw away the preceding end token
						sameLine = true;
						line = readLine();
						break;
					}
				}
				else
					break;
			}
			line = readLine();
		}
		
		while (true)
		{
			block.add(line);
			if (line.contains(endToken))
			{
				log.info("found end token - block size: " + block.size());
				break;
			}
			line = readLine();
		}
		
		if (sameLine)
			currLine = line;
		return block;
	}
	
	public static void main(String args[]) throws Exception
	{
		String source = "http://www.turfclub.com.sg/RacingInformation/HorsePerformance/tabid/207/ctl/details/mid/1081/Default.aspx?HorseName=MARTEL";
		UrlParser parser = new UrlParser( source );
		log.info(parser);
	}

	public boolean isExitOnFail() {
		return exitOnFail;
	}

	public void setExitOnFail(boolean exitOnFail) {
		this.exitOnFail = exitOnFail;
	}
		
}