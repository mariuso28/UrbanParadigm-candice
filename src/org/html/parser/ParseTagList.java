package org.html.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ParseTagList extends ArrayList<ParseTag> implements Cloneable
{
	private static final Logger log = Logger.getLogger(ParseTagList.class);
	
	private static final long serialVersionUID = 1L;
	private List<String> startTags;
	private List<String> endTags;
	private List<String> ignoreTags;
	
	public ParseTagList() {
		super();
		startTags = new ArrayList<String>();
		endTags = new ArrayList<String>();
		ignoreTags = new ArrayList<String>();
	}
	
	public ParseTagList(String startTag, String endTag) {
		this();
		addStartTag(startTag);
		addEndTag(endTag);
	}
	
	@Override
	public Object clone()
	{
		ParseTagList ptl = new ParseTagList();
		Iterator<ParseTag> iter = this.iterator();
		while (iter.hasNext())
		{
			ParseTag pt = (ParseTag) iter.next().clone();
			ptl.add(pt);
		}
		for (String str : startTags)
			ptl.getStartTags().add(new String(str));
		for (String str : endTags)
			ptl.getStartTags().add(new String(str));
		for (String str : ignoreTags)
			ptl.getStartTags().add(new String(str));
		
		return ptl;
	}

	public void addStartTag(String startTag)
	{
		startTags.add(startTag);
	}
	
	public void addEndTag(String endTag)
	{
		endTags.add(endTag);
	}
	
	public ParseTagList(List<String> startTags, List<String> endTags) {
		super();
		this.startTags = startTags;
		this.endTags = endTags;
	}
	
	public List<String> getStartTags() {
		return startTags;
	}

	public List<String> getEndTags() {
		return endTags;
	}
	
	/*
	</td><td align="center">29405</td><td><a href="/tabid/207/ctl/details/Mid/1081/Default.aspx?horsename=METEOR+ONE" target=_blank>METEOR ONE</a>
	</td><td align="center">&nbsp;</td><td align="center">&nbsp;</td><td>EG DA CRUZ<sup><br /></sup>
	</td><td>TAN S H</td><td align="center">9</td><td align="center">50</td><td align="center">51.5</td> */
	
	public List<String> split(String text)
	{
		log.debug("Splitting : " + text );
		List<String> toks = new ArrayList<String>();
		while (text!=null)
			text = getNextToken(toks,text);
		return toks;
	}
	
	private String getNextToken(List<String> toks,String text)
	{
		int spos=Integer.MAX_VALUE;
		String startTok = null;
		for (String start  : startTags)
		{
			int pos = text.indexOf(start);
			if (pos>=0)
			{
				if (pos<spos)
				{
					startTok = start;
					spos = pos;
				}
			}
		}
		if (spos==Integer.MAX_VALUE)
			return null;
		text = text.substring(spos+startTok.length());
	int epos=Integer.MAX_VALUE;
		String endTok = null;
		for (String end  : endTags)
		{
			int pos = text.indexOf(end);
			if (pos>=0)
			{
				if (pos<epos)
				{
					endTok = end;
					epos = pos;
				}
			}
		}
		if (epos==Integer.MAX_VALUE)
			return null;
		String tok = text.substring(0,epos);
		String stext = ignoreTag(tok,text);
		if (stext!=null)
		{
			return stext;
		}
		toks.add(tok);
		return text.substring(epos+endTok.length());
	}

	public void addIgnoreTag(String string) {
		ignoreTags.add(string);
	}
	
	private String ignoreTag(String tok,String text)
	{
		for (String ig : ignoreTags)
		{
			int tpos = tok.indexOf(ig);
			if (tpos>=0)
				return text.substring(tpos);
		}
		return null;
	}
	
	
}
