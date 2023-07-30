package gg.enes.moderation.util;

import gg.enes.moderation.Main;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class IPUtil {
    private static final Logger logger = Main.getInstance().getLogger();

    /**
     * Method to check various attributes of an IP address.
     *
     * @param ip The timestamp to calculate relative time from
     * @return The attributes of IP as a string
     */
    public static String check(String ip) {
        String apiUrl = "http://ip-api.com/json/" + ip + "?fields=status,message,mobile,proxy,hosting";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String responseString = response.toString();
                JSONObject jsonObject = new JSONObject(responseString);

                boolean mobile = jsonObject.getBoolean("mobile");
                boolean proxy = jsonObject.getBoolean("proxy");
                boolean hosting = jsonObject.getBoolean("hosting");

                if (mobile) {
                    return "mobile";
                }

                if (proxy) {
                    return "proxy";
                }

                if (hosting) {
                    return "hosting";
                }
            } else {
                logger.log(Level.WARNING, "Received HTTP response code: " + responseCode);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while checking IP.");
            logger.log(Level.WARNING, e.getMessage());
        }

        return "none";
    }
}
