package org.cz.user;

import java.util.HashMap;

import org.apache.log4j.Logger;

public enum Role{
	
		ROLE_TRADER("Trader",0,'p',"Player","FFD6D6"),
		ROLE_ADMIN("Admin",1,'x',"Admin","F7D6FF");
		
		@SuppressWarnings("unused")
		private static final Logger log = Logger.getLogger(Role.class);
		private String desc;
		private int rank;					// don't use ordinal cos we might mess with the enum order
		private Character code;
		private String shortCode;
		private String color;
		private static HashMap<Character,Role> codeMap;
		
		private Role(String desc,int rank,char code,String shortCode,String color)
		{
			setRank(rank);
			setDesc(desc);
			setCode(code);
			setColor(color);
			setShortCode(shortCode);
			Role.addCodeMap(this,code);
		}
		
		public String getColor() {
			return color;
		}



		public void setColor(String color) {
			this.color = color;
		}

		private static void addCodeMap(Role role,Character code)
		{
			if (codeMap==null)									// static initialization dont work
				codeMap=new HashMap<Character,Role>();
			codeMap.put(code, role);
		}
		
		public static Role getRoleForCode(String code)
		{
			Character ch = '?';
			for (int index=code.length()-1; index>=0; index--)
			{
				ch = code.charAt(index);
				if (!Character.isDigit(ch))
					break;
			}
			return codeMap.get(ch);
		}
		
		private void setDesc(String desc)
		{
			this.desc = desc;
		}
		
		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getRank() {
			return rank;
		}

		public String getDesc()
		{
			return desc;
		}

		public void setCode(Character code) {
			this.code = code;
		}

		public Character getCode() {
			return code;
		}

		public void setShortCode(String shortCode) {
			this.shortCode = shortCode;
		}

		public String getShortCode() {
			return shortCode;
		}

}
