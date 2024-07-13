package me.notmyidea.application;

import me.notmyidea.files.FileBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class JBS {

    public static void JBS(FileBuilder fileBuilder) {
        System.out.println("#------------ Loading Bot -----------#");
        try {
            JDABuilder.createLight(fileBuilder.getString("token"), EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING))
                    .addEventListeners(new ReactionListener(fileBuilder))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start bot, check your token and connection!");
        }
    }







}
