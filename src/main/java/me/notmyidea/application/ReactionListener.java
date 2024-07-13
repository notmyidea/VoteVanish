package me.notmyidea.application;

import me.notmyidea.files.FileBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReactionListener extends ListenerAdapter {

    private FileBuilder fileBuilder;

    public ReactionListener(FileBuilder fileBuilder) {
        this.fileBuilder = fileBuilder;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        String reaction = event.getEmoji().getName();
        User user = event.getUser();
        if (user == null) {
            return;
        }
        String userId = user.getId();

        // Is user undeletable?
        if (fileBuilder.getStringList("undeletableUsers").contains(userId)) {
            return;
        }
        // Is role undeletable?
        for (String role : event.getMember().getRoles().stream().map(r -> r.getName().toLowerCase()).toList()) {
            if (fileBuilder.getStringList("undeletableRoles").contains(role)) {
                return;
            }
        }
        // Is user (sender) ignored?
        if (fileBuilder.getStringList("ignoredUsers").contains(userId)) {
            return;
        }
        // Is user (sender role) ignored?
        for (String role : event.getMember().getRoles().stream().map(r -> r.getName().toLowerCase()).toList()) {
            if (fileBuilder.getStringList("ignoredRoles").contains(role)) {
                return;
            }
        }
        // Is channel ignored?
        if (fileBuilder.getStringList("ignoredChannels").contains(event.getChannel().getId())) {
            return;
        }
        // is delete emoji ?
        if (reaction.equals(fileBuilder.getString("deleteEmoji")) || reaction.equals(fileBuilder.getString("keepEmoji"))) {
            int requiredUsersToDelete = fileBuilder.getInteger("requireUsersToDelete");
            if (requiredUsersToDelete > 0) {
                event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
                    long deleteCount = message.getReactions().stream()
                            .filter(r -> r.getEmoji().getName().equals(fileBuilder.getString("deleteEmoji")))
                            .findFirst()
                            .map(r -> r.getCount())
                            .orElse(0);

                    long keepCount = message.getReactions().stream()
                            .filter(r -> r.getEmoji().getName().equals(fileBuilder.getString("keepEmoji")))
                            .findFirst()
                            .map(r -> r.getCount())
                            .orElse(0);

                    if (deleteCount - keepCount >= requiredUsersToDelete) {
                        message.delete().queue();
                    }
                });
            } else {
                event.getChannel().deleteMessageById(event.getMessageId()).queue();
            }
        }
    }
}