package net.staretta.businesslogic.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.staretta.businesslogic.entity.MessageLogEntity;
import net.staretta.businesslogic.entity.MessageLogEntity.MessageType;
import net.staretta.businesslogic.entity.MessageLogEntity.Role;

import org.hibernate.Session;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.snapshot.ChannelSnapshot;
import org.pircbotx.snapshot.UserSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSortedSet;

@Service
@Transactional
public class MessageLogService
{
	@PersistenceContext
	private EntityManager em;

	Logger logger = LoggerFactory.getLogger(getClass());

	public MessageLogService()
	{

	}

	public void addLog(User user, String message, ImmutableSortedSet<Channel> channels, String server, Role role, MessageType messageType)
	{
		Session s = getSession();
		MessageLogEntity log = new MessageLogEntity();
		log.setUsername(user.getLogin());
		log.setNickname(user.getNick());
		log.setHostmask(user.getHostmask());
		log.setServer(server);

		StringBuilder sb = new StringBuilder();
		for (Channel channel : channels)
			sb.append(channel.getName() + " ");
		log.setChannel(sb.toString().trim());

		log.setRole(role);
		log.setMessage(message);
		log.setMessageType(messageType);
		s.saveOrUpdate(log);
	}

	private Session getSession()
	{
		return em.unwrap(EntityManagerImpl.class).getSession();
	}

	public void addLog(UserSnapshot user, String message, ChannelSnapshot channel, String serverHostname, Role role, MessageType part)
	{

	}
}