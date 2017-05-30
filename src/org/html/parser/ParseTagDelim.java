package org.html.parser;

public class ParseTagDelim extends ParseTag implements Cloneable
{
	private String startDelim;
	private String revertDelim;					// if start not found check on revert
	private String endDelim;
	
	public ParseTagDelim(String name, int count, String startDelim, String endDelim) {
		super( name, count );
		this.startDelim = startDelim;
		this.endDelim = endDelim;
	}
	
	@Override
	public Object clone()
	{
		ParseTagDelim ptd = new  ParseTagDelim(new String(getName()), getCount(), new String(startDelim), new String(endDelim));
		ptd.setRevertDelim(new String(getRevertDelim()));
		return ptd;
	}

	public String getStartDelim() {
		return startDelim;
	}

	public void setStartDelim(String startDelim) {
		this.startDelim = startDelim;
	}

	public String getEndDelim() {
		return endDelim;
	}

	public void setEndDelim(String endDelim) {
		this.endDelim = endDelim;
	}
	
	public String parseValue(String value)
	{
		int spos = 0;
		int epos = value.length();
		String sDelim = startDelim;
		if (startDelim != null)
			spos = value.indexOf(startDelim);
		if (spos<0)
		{
			if (revertDelim != null)
				spos = value.indexOf(revertDelim);
			sDelim = revertDelim;
		}
			
		if (endDelim != null)
			epos = value.indexOf(endDelim,spos);
		if (spos >-1 && epos>-1)
			return filterValue( value.substring(spos+sDelim.length(),epos) );
		if (spos>-1)
			return filterValue( value.substring(spos+sDelim.length()) );
		if (epos>-1)
			return filterValue( value.substring(0,epos) );
		
		return filterValue( value );
	}

	public void setRevertDelim(String revertDelim) {
		this.revertDelim = revertDelim;
	}

	public String getRevertDelim() {
		return revertDelim;
	}
}
