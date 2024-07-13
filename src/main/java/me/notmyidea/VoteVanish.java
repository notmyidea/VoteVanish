package me.notmyidea;

import me.notmyidea.application.JBS;
import me.notmyidea.files.FileBuilder;

public class VoteVanish {
    public static void main(String[] args) {

        FileBuilder fileBuilder = new FileBuilder();
        JBS.JBS(fileBuilder);
    }




    public void sentMessage(String message) {
        System.out.println(message);
    }

    public void sentDebugMessage(String message) {
        // if(debug) {
        System.out.println("DEBUG: " + message);
        // }
    }
}