package net.staretta.modules;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import net.staretta.RawrBot;
import net.staretta.businesslogic.BaseListener;
import net.staretta.businesslogic.ModuleInfo;
import net.staretta.businesslogic.entity.TellEntity;
import net.staretta.businesslogic.services.TellService;
import net.staretta.businesslogic.util.ircUtil;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class Tell extends BaseListener
{
	TellService service;
	ModuleInfo moduleInfo;
	
	public Tell()
	{
		service = RawrBot.applicationContext.getBean(TellService.class);
	}
	
	@Override
	protected ModuleInfo setModuleInfo()
	{
		moduleInfo = new ModuleInfo();
		moduleInfo.setName("Tell");
		moduleInfo.setAuthor("Staretta");
		moduleInfo.setVersion("v1.0");
		moduleInfo.addCommand("!tell", "!tell <Nickname> <Message> : Tells the user the message, "
				+ "if they are offline, they will be notified of the message when they join channel.");
		moduleInfo.addCommand("!told",
				"!told [Nickname] : Displays messages to a user, or all users, and if a user has received the messages");
		// moduleInfo.addCommand("!note", "!note <Nickname> : Gives a note to a user");
		return moduleInfo;
	}
	
	@Override
	public void onJoin(JoinEvent<PircBotX> event) throws Exception
	{
		super.onJoin(event);
		
		if (!event.getUser().getNick().equals(event.getBot().getNick()))
		{
			ArrayList<TellEntity> tells = service
					.getTells(event.getUser().getNick(), event.getBot().getConfiguration().getServerHostname());
			if (tells != null)
			{
				for (TellEntity tell : tells)
				{
					String date = "[" + new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(tell.getDate()) + "]";
					String message = date + " <" + tell.getFromNickname() + "> " + tell.getMessage();
					ircUtil.sendNotice(event, message);
					
					tell.setTold(true);
					service.setTold(tell);
				}
			}
		}
	}
	
	@Override
	public void OnMessage(MessageEvent event)
	{
		if (ircUtil.isCommand(event, "!tell"))
		{
			ArrayList<String> params = new ArrayList<String>(Arrays.asList(event.getMessage().split("\\s")));
			if (params.size() > 2)
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < params.size(); i++)
					sb.append(params.get(i) + " ");
				if (service.addTell(params.get(1), event.getUser(), sb.toString().trim(), event.getBot().getConfiguration()
						.getServerHostname()))
					ircUtil.sendPrivateMessage(event, params.get(1) + " will be told: " + sb.toString().trim());
			}
			else
			{
				ircUtil.sendPrivateMessage(event, moduleInfo.getCommands().get("!tell"));
			}
		}
		else if (ircUtil.isCommand(event, "!told"))
		{
			
		}
	}
	
	@Override
	public void OnPrivateMessage(PrivateMessageEvent event)
	{
		if (ircUtil.isCommand(event, "!tell"))
		{
			ArrayList<String> params = new ArrayList<String>(Arrays.asList(event.getMessage().split("\\s")));
			if (params.size() > 2)
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < params.size(); i++)
					sb.append(params.get(i) + " ");
				if (service.addTell(params.get(1), event.getUser(), sb.toString().trim(), event.getBot().getConfiguration()
						.getServerHostname()))
					ircUtil.sendPrivateMessage(event, params.get(1) + " will be told: " + sb.toString().trim());
			}
			else
			{
				ircUtil.sendPrivateMessage(event, moduleInfo.getCommands().get("!tell"));
			}
		}
		else if (ircUtil.isCommand(event, "!told"))
		{
			
		}
	}
}
