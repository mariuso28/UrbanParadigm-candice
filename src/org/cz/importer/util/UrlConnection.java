package org.cz.importer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.html.parser.UrlParserException;

public class UrlConnection {

	private static final Logger log = Logger.getLogger(UrlConnection.class);
	
	private URL url;
	private BufferedReader bins;
	
	public UrlConnection(String source) throws UrlParserException, MalformedURLException
	{
		url = new URL( source );
		connectUrl(url);
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
				log.info("Setting User-Agent Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240");
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240");
				InputStreamReader ins = new InputStreamReader( connection.getInputStream() );
				setBins(new BufferedReader( ins ));
				break;
			}
			catch (IOException e)
			{
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

	public BufferedReader getBins() {
		return bins;
	}

	public void setBins(BufferedReader bins) {
		this.bins = bins;
	}
}
