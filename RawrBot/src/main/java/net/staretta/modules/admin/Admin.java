package net.staretta.modules.admin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.staretta.RawrBot;
import net.staretta.businesslogic.admin.AdminInfo;
import net.staretta.businesslogic.admin.AdminListener;
import net.staretta.businesslogic.services.EmailService;
import net.staretta.businesslogic.services.UserService;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import com.google.common.collect.ImmutableSet;

public class Admin extends AdminListener
{
	private UserService userService;
	private EmailService emailService;
	
	public Admin()
	{
		userService = RawrBot.getAppCtx().getBean(UserService.class);
		emailService = RawrBot.getAppCtx().getBean(EmailService.class);
	}
	
	@Override
	public AdminInfo setAdminInfo()
	{
		AdminInfo adminInfo = new AdminInfo();
		adminInfo.setAdminVersion("v0.1");
		adminInfo.addCommand("register", "");
		adminInfo.addCommand("identify");
		adminInfo.addCommand("email");
		adminInfo.addCommand("password");
		adminInfo.addCommand("verify");
		return adminInfo;
	}
	
	@Override
	public void OnAdminPrivateMessage(PrivateMessageEvent<PircBotX> event)
	{
		String m = event.getMessage();
		List<String> params = Arrays.asList(m.split("\\s"));
		// Check and see if all they entered was !admin, and if so, spit out the admin commands.
		if (m.trim().equalsIgnoreCase("!admin"))
		{
			ImmutableSet<Listener<PircBotX>> listeners = event.getBot().getConfiguration().getListenerManager().getListeners();
			StringBuilder commands = new StringBuilder();
			for (Listener<PircBotX> mod : listeners)
			{
				if (AdminListener.class.isAssignableFrom(mod.getClass()))
				{
					AdminListener listener = (AdminListener) mod;
					HashMap<String, List<String>> commandList = listener.getAdminInfo().getCommands();
					for (Entry<String, List<String>> command : commandList.entrySet())
					{
						if (!command.getKey().isEmpty())
						{
							commands.append(command.getKey() + " ");
						}
					}
				}
			}
			List<String> commandHelp = Arrays.asList("Admin Commands: " + commands.toString(),
					"For command specific help, type \"--help\" or \"-h\" after a command.");
			commandHelp.forEach(message -> event.getUser().send().message(message));
		}
		else if (isOption(m, "r", "register"))
		{
			// Check the size, verify that the user entered a valid email address, and the password isn't unreasonably small or large.
			// Then create the user and save it to the database
			// !admin register email password
			if (params.size() == 4)
			{
				String email = params.get(2);
				String password = params.get(3);
				if (!userService.isNicknameAvailable(event.getUser()))
				{
					event.getUser().send().message("Nickname already in use.");
				}
				else if (!userService.isValidEmail(email))
				{
					event.getUser().send().message("Invalid Email address, please enter a correct email address.");
				}
				else if (!userService.isValidPassword(password))
				{
					String message = "Invalid password. Passwords must be 8-31 characters long, "
							+ "and contain at least 3 of the following:"
							+ " a Digit, an Uppercase Character, a Lowercase Character, or a Special Character.";
					event.getUser().send().message(message);
				}
				else
				{
					userService.createUser(emailService, event.getUser(), email, password);
					String message = "User Created - A verification email has been sent to the email provided. "
							+ "To verify the email: \"!admin verify <code from email>\"";
					event.getUser().send().message(message);
				}
			}
			else
			{
				event.getUser().send().message("Incorrect number of parameters. \"!admin register user@email.com P@sSw0rd\"");
			}
		}
		else if (isOption(m, "i", "identify"))
		{
			// !admin identify password
			if (params.size() == 3)
			{
				String password = params.get(2);
				if (userService.isNicknameAvailable(event.getUser()))
				{
					event.getUser().send().message("Nickname not registered. \"!admin register user@email.com P@sSw0rd\"");
				}
				else if (!userService.isValidPassword(password))
				{
					String message = "Invalid password. Passwords must be 8-31 characters long, "
							+ "and contain at least 3 of the following:"
							+ " a Digit, an Uppercase Character, a Lowercase Character, or a Special Character.";
					event.getUser().send().message(message);
				}
				else if (userService.isLoggedIn(event.getUser()))
				{
					event.getUser().send().message("You are already logged in.");
				}
				else
				{
					if (userService.checkPassword(event.getUser(), password))
					{
						userService.setLastActive(event.getUser());
					}
					else
					{
						event.getUser().send().message("Incorrect Password.");
					}
				}
			}
			else
			{
				event.getUser().send().message("Incorrect number of parameters. \"!admin identify <password>\"");
			}
		}
		else if (isOption(m, "p", "pass", "password"))
		{
			if (params.size() == 3)
			{
				
			}
		}
		else if (isOption(m, "e", "email"))
		{
			if (params.size() == 3)
			{
				
			}
		}
		else if (isOption(m, "v", "verify"))
		{
			if (params.size() == 3)
			{
				if (userService.verifyEmail(event.getUser(), params.get(2)))
				{
					event.getUser().send().message("Email successfully verified. You can now login using \"!admin identify <password>\"");
				}
			}
			else
			{
				event.getUser().send().message("Incorrect number of parameters. \"!admin verify <code>\"");
			}
		}
	}
}