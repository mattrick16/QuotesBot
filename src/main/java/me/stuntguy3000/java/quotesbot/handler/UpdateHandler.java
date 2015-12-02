package me.stuntguy3000.java.quotesbot.handler;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.stuntguy3000.java.quotesbot.QuotesBot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by bo0tzz
 */
public class UpdateHandler implements Runnable {

    QuotesBot instance;

    public UpdateHandler(QuotesBot instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        File build = new File("build");
        File jar = new File("QuotesBot.new");
        int currentBuild = QuotesBot.BUILD;
        int newBuild = 0;

        while (true) {
            try {
                newBuild = Integer.parseInt(Unirest.get("http://ci.zackpollard.pro/job/QuotesBot/lastSuccessfulBuild/buildNumber").asString().getBody());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            if (newBuild > currentBuild) {
                LogHandler.log("Downloading build #" + newBuild);
                instance.sendToAdmins("Downloading build #" + newBuild);
                try {
                    FileUtils.writeStringToFile(build, String.valueOf(newBuild));
                    FileUtils.copyURLToFile(new URL("http://ci.zackpollard.pro/job/QuotesBot/lastSuccessfulBuild/artifact/target/QuotesBot.jar"), jar);
                    LogHandler.log("Build #" + newBuild + " downloaded. Restarting...");
                    instance.sendToAdmins("Build #" + newBuild + " downloaded. Restarting...");
                } catch (IOException e) {
                    System.err.println("Updater failed!");
                    e.printStackTrace();
                    break;
                }
                System.exit(0);
            }
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}