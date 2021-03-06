package net.staretta.businesslogic.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.staretta.businesslogic.util.MiscUtil;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "generatorMySeq", sequenceName = "users" + "_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generatorMySeq")
	private long id;
	private String email;
	private String username;
	private String hostmask;
	private String server;
	private String password;
	// Email verification
	private boolean verified = false;
	private String verificationCode;
	// Nickserv Identified
	private boolean identified = false;
	private Date registerDate;
	// Used to see how long they've been idle, and if they need to re sign in.
	private Date lastLogin;
	@OneToMany(mappedBy = "user")
	private Set<UserChannelEntity> channels = new HashSet<UserChannelEntity>();
	@Enumerated(EnumType.STRING)
	private Role role = Role.User;
	@OneToMany(mappedBy = "user")
	private Set<UserAliasEntity> alias = new HashSet<UserAliasEntity>();
	
	public UserEntity()
	{
		this.registerDate = new Date();
		this.verificationCode = generateVerificationCode();
	}
	
	public enum Role
	{
		User, Operator, Admin, SuperAdmin
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getHostmask()
	{
		return hostmask;
	}
	
	public String getServer()
	{
		return server;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public boolean isVerified()
	{
		return verified;
	}
	
	public boolean isIdentified()
	{
		return identified;
	}
	
	public Date getRegisterDate()
	{
		return registerDate;
	}
	
	public Date getLastLogin()
	{
		return lastLogin;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public void setHostmask(String hostmask)
	{
		this.hostmask = hostmask;
	}
	
	public void setServer(String server)
	{
		this.server = server;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public void setVerified(boolean verified)
	{
		this.verified = verified;
	}
	
	public void setIdentified(boolean identified)
	{
		this.identified = identified;
	}
	
	public void setRegisterDate(Date registerDate)
	{
		this.registerDate = registerDate;
	}
	
	public void setLastLogin(Date lastLogin)
	{
		this.lastLogin = lastLogin;
	}
	
	public String getVerificationCode()
	{
		return verificationCode;
	}
	
	public void setVerificationCode(String verificationCode)
	{
		this.verificationCode = verificationCode;
	}
	
	public String generateVerificationCode()
	{
		return MiscUtil.generateRandomString(20, 20);
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public void setRole(Role role)
	{
		this.role = role;
	}
	
	public Set<UserChannelEntity> getChannels()
	{
		return channels;
	}
	
	public void setChannels(Set<UserChannelEntity> channels)
	{
		this.channels = channels;
	}
	
	public boolean hasChannel(String channel)
	{
		return channels.contains(channel);
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public Set<UserAliasEntity> getAlias()
	{
		return alias;
	}
}
