package net.a1337ism.modules;

import java.io.IOException;

import net.a1337ism.RawrBot;
import net.a1337ism.util.Json;
import net.a1337ism.util.ircUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JokeCommand extends ListenerAdapter {
    // Set up the logger stuff
    private static Logger logger = LoggerFactory.getLogger(RawrBot.class);

    // Check for channel messages.
    public void onMessage(MessageEvent event) throws Exception {

        // Check if message starts with !joke and if they are rate limited
        if (event.getMessage().trim().toLowerCase().startsWith("!joke")) {

            // Return if they are rate limited
            if (RateLimiter.isRateLimited(event.getUser().getNick()))
                return;

            if (event.getMessage().trim().toLowerCase().endsWith("-help")
                    || event.getMessage().trim().toLowerCase().endsWith("-h")) {
                // If message ends with -help or -h, then send them the help information
                String jokeHelp = "!joke : Says a random joke from the Internet Chuck Norris Database.";
                ircUtil.sendMessage(event, jokeHelp);

            } else {
                // It's currently in an array because I wanted to know what ID number the joke was using in the event
                // that I needed to debug escape characters.
                Object[] joke = getJoke();
                // If joke isn't null, meaning it didn't crash, then do the good stuff!
                if (joke != null) {
                    ircUtil.sendMessage(event, joke[1].toString());
                }
            }
        }
    }

    // Check for private messages.
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {

        // Check if message starts with !joke
        if (event.getMessage().trim().toLowerCase().startsWith("!joke")) {

            if (event.getMessage().trim().toLowerCase().endsWith("-help")
                    || event.getMessage().trim().toLowerCase().endsWith("-h")) {
                // If message ends with -help or -h, then send them the help information
                String jokeHelp = "!joke : Says a random joke from the Internet Chuck Norris Database.";
                ircUtil.sendMessage(event, jokeHelp);

            } else {
                // It's currently in an array because I wanted to know what ID number the joke was using in the event
                // that I needed to debug escape characters.
                Object[] joke = getJoke();
                // If joke isn't null, meaning it didn't crash, then do the good stuff!
                if (joke != null) {
                    ircUtil.sendMessage(event, joke[1].toString());
                }
            }
        }
    }

    private Object[] getJoke() {
        try {
            // grabs JSONobject and stores it into json for us to read from
            org.json.JSONObject json = Json
                    .readJsonFromUrl("http://api.icndb.com/jokes/random?escape=javascript&firstName=Rawr&lastName=Bot");
            // stores the specific values I want into an array to be used later.
            Object[] array = { ((JSONObject) json.get("value")).get("id"), ((JSONObject) json.get("value")).get("joke") };
            return array;
        } catch (JSONException ex) {
            logger.error("ERROR " + ex);
        } catch (IOException ex) {
            logger.error("ERROR " + ex);
        }
        return null;
    }
}