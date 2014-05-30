package net.staretta;

import java.util.List;

import net.staretta.businesslogic.entity.Settings;
import net.staretta.businesslogic.services.SettingsService;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RawrBot extends ListenerAdapter implements Listener
{
	public static void main(String[] args)
	{
		Logger logger = LoggerFactory.getLogger(RawrBot.class);

		logger.info("Initializing Spring context.");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
		logger.info("Spring context initialized.");

		SettingsService settingsService = (SettingsService) applicationContext.getBean(SettingsService.class);
		List<Settings> serverSettings = settingsService.getBotSettings();
		logger.info("Bot Settings Loaded.");

		MultiBotManager<PircBotX> manager = new MultiBotManager();
		for (Settings setting : serverSettings)
		{
			// @formatter:off
			Builder builder = new Configuration.Builder()
				.setName(setting.getNickname())
				.setLogin(setting.getUsername())
				.setRealName(setting.getVersion())
				.setAutoNickChange(true)
				.setAutoReconnect(true)
				.setCapEnabled(true)
				.setIdentServerEnabled(false)
				.setServerHostname(setting.getServer())
				.setServerPort(setting.getPort())
				.setNickservPassword(setting.getPassword());
			// @formatter:on
			if (setting.isSsl())
				builder.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates());

			for (String channel : setting.getChannels())
				builder.addAutoJoinChannel(channel);

			for (String module : setting.getModules())
			{
				try
				{
					builder.addListener((Listener) Class.forName("net.staretta.modules." + module).newInstance());
				}
				catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
				{
					logger.error("Exception in RawrBot.main ", e);
				}
			}

			Configuration config = builder.buildConfiguration();
			manager.addBot(config);
			logger.info("Added IRC bot to manager: " + setting.getServer());
		}
		logger.info("Starting IRC bots.");
		manager.start();
	}
}