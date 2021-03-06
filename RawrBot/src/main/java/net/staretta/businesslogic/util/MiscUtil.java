package net.staretta.businesslogic.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class MiscUtil
{
	// URL Regex matching
	static String urlRegex = "(?:\\b(?:http|ftp|www\\.)\\S+\\b)|(?:\\b\\S+\\.com\\S*\\b)";
	static Pattern urlPattern = Pattern.compile(urlRegex);
	
	/**
	 * Chooses a random number based on the length of a list.
	 * 
	 * @param fileLength
	 *            number of elements within a file list
	 * @return a random number between 0 and file length
	 */
	public static int randomNumber(int fileLength)
	{
		// int randomNumber = (int) (Math.random() * fileLength);
		Random random = new Random();
		int randomNumber = random.nextInt(fileLength);
		return randomNumber;
	}
	
	/**
	 * Chooses a random selection using the {@link randomNumber} function, and returns the line for that index from a
	 * list.
	 * 
	 * @param fileList
	 *            List of lines
	 * @return a random line from a list
	 */
	public static String randomSelection(String[] fileList)
	{
		// Chooses a random selection from a list of lines.
		String randomLine = fileList[randomNumber(fileList.length)];
		return randomLine;
	}
	
	public static String redactURL(String line)
	{
		String message = "";
		if (urlPattern.matcher(line).find())
		{
			message = urlPattern.matcher(line).replaceAll("URL_REDACTED");
		}
		return message;
	}
	
	public static int parsePeriodTime(String time)
	{
		PeriodFormatter formatter = ISOPeriodFormat.standard();
		Period p = formatter.parsePeriod(time);
		Seconds s = p.toStandardSeconds();
		return s.getSeconds();
	}
	
	public static String durationFormat(int total_seconds)
	{
		// Helper variables.
		int MINUTE = 60;
		int HOUR = MINUTE * 60;
		
		// Get the days, hours, etc.
		int hours = (total_seconds) / HOUR;
		int minutes = (total_seconds % HOUR) / MINUTE;
		int seconds = total_seconds % MINUTE;
		
		// Build a pretty string. (like this: "[HH:mm:ss]")
		String uptime = "[";
		if (hours > 0)
			uptime += hours + ":";
		if (uptime.length() > 0 || minutes > 0)
			if (minutes < 10 && hours > 0)
				uptime += "0" + minutes + ":";
			else
				uptime += minutes + ":";
		// if (seconds < 10)
		uptime += String.format("%1$02d", seconds) + "]";
		
		// Return the ugly bastard.
		return uptime;
	}
	
	/**
	 * Outputs a formatted time based on the current time, and the last seen time.
	 * 
	 * @param oldTime
	 *            Time since person was last seen in seconds.
	 * @return a formatted string like this: "N days" or "N minutes" or "N hours"
	 */
	@SuppressWarnings("unused")
	public static String timeFormat(int oldTime)
	{
		// Make a pretty string (like this: "N days" or "N minutes" or "N hours", etc.)
		int seconds = (int) (System.currentTimeMillis() / 1000) - oldTime;
		String a_second;
		String formatTime = seconds + " " + (a_second = (seconds == 1) ? "second" : "seconds");
		
		int minutes = seconds / 60;
		if (minutes > 0)
		{
			String a_minute;
			formatTime = minutes + " " + (a_minute = (minutes == 1) ? "minute" : "minutes");
		}
		int hours = minutes / 60;
		if (hours > 0)
		{
			String an_hour;
			formatTime = hours + " " + (an_hour = (hours == 1) ? "hour" : "hours");
		}
		int days = hours / 24;
		if (days > 0)
		{
			String a_day;
			formatTime = days + " " + (a_day = (days == 1) ? "day" : "days");
		}
		
		// Return the pretty string.
		return formatTime;
	}
	
	/**
	 * Outputs a formatted time based on the current time, and the start time of the system, or the bot.
	 * 
	 * @param total_seconds
	 *            Time since the bot/system has been started in seconds.
	 * @return a formatted string like this: "N days, N hours, N minutes, N seconds"
	 */
	@SuppressWarnings("unused")
	public static String uptimeFormat(long total_seconds)
	{
		// Helper variables.
		int MINUTE = 60;
		int HOUR = MINUTE * 60;
		int DAY = HOUR * 24;
		
		// Get the days, hours, etc.
		
		long days = total_seconds / DAY;
		long hours = (total_seconds % DAY) / HOUR;
		long minutes = (total_seconds % HOUR) / MINUTE;
		long seconds = total_seconds % MINUTE;
		
		// Build a pretty string. (like this: "N days, N hours, N minutes, N seconds")
		String uptime = "";
		if (days > 0)
		{
			String a_day;
			uptime += days + " " + (a_day = (days == 1) ? "day" : "days") + ", ";
		}
		if (uptime.length() > 0 || hours > 0)
		{
			String an_hour;
			uptime += hours + " " + (an_hour = (hours == 1) ? "hour" : "hours") + ", ";
		}
		if (uptime.length() > 0 || minutes > 0)
		{
			String a_minute;
			uptime += minutes + " " + (a_minute = (minutes == 1) ? "minute" : "minutes") + ", ";
		}
		String a_second;
		uptime += seconds + " " + (a_second = (seconds == 1) ? "second" : "seconds");
		
		// Return the ugly bastard.
		return uptime;
	}
	
	/**
	 * Outputs a formatted date based on the current time.
	 * 
	 * @return a formatted string like this: "MM/dd/yy HH:mm:ss"
	 */
	public static String dateFormat()
	{
		// Set up the date stuff. Seems a bit redundant though. Argh.
		String dateNow = null;
		Date date = new Date();
		dateNow = new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(date);
		
		return dateNow;
	}
	
	/**
	 * Returns current seconds based on the systems milliseconds
	 * 
	 * @return an integer seconds
	 */
	public static int currentTimeSeconds()
	{
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	public static int randomInt(Random rand, int min, int max)
	{
		// return (int)(Math.random() * (max - min) + min);
		max += 1; // otherwise its not inclusive of max
		return (int) (rand.nextDouble() * (max - min) + min);
	}
	
	public static String generateRandomString(int min, int max)
	{
		Random rand = new Random(System.currentTimeMillis());
		
		int num = (int) (byte) randomInt(rand, min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
		{
			int x = randomInt(rand, 0, 2);
			if (x == 0)
				b[i] = (byte) randomInt(rand, '0', '9');
			else if (x == 1)
				b[i] = (byte) randomInt(rand, 'a', 'z');
			else if (x == 2)
				b[i] = (byte) randomInt(rand, 'A', 'Z');
		}
		
		return new String(b);
	}
}