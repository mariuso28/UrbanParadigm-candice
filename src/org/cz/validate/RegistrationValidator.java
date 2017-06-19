package org.cz.validate;

public class RegistrationValidator 
{
	public static String validateFields(String email, String password, String contact, String phone)
	{
		String msg = "";
		msg += checkEmail(email);
		msg += checkField("Contact",contact);
		msg += checkPhone(phone);
		msg += checkPassword(password);
		if (!msg.isEmpty())
			return msg.substring(0,msg.length()-1);
		
		return msg;
	}

	private static String checkEmail(String email)
	{
		String msg = checkField("Email",email);
		if (msg.isEmpty())
		{
			EmailValidator ev = new EmailValidator();
			if (!ev.validate(email))
				msg += "Email,";
		}
		return msg;
	}

	private static String checkPhone(String phone)
	{
		String msg = checkField("Phone",phone);
		if (msg.isEmpty())
		{
			PhoneValidator pv = new PhoneValidator();
			if (!pv.validate(phone))
				msg += "Phone,";
		}
		return msg;
	}

	private static String checkPassword(String password)
	{
		String msg = checkField("Password",password);
		if (msg.isEmpty())
		{
			if (password.length()<8)
				return "Password should be 8 chars or more - please fix";
		}
		return "";
	}

	private static String checkField(String field,String value)
	{
		if (value==null || value.isEmpty())
			return field + ",";
		return "";
	}
}