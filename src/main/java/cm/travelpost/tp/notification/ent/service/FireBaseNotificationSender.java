/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class FireBaseNotificationSender {

    Logger logger = LoggerFactory.getLogger(FireBaseNotificationSender.class);

    final static String METHOD_POST = "POST";
    final static String JSON_TITLE = "title";
    final static String JSON_BODY = "body";
    final static String JSON_TO = "to";
    final static String JSON_NOTIFICATION = "notification";
    final static String JSON_MESSAGE = "message";
    final static String JSON_TEXT = "text";
    final static String JSON_DATA = "data";
    final static String JSON_REGISTRATION_IDS = "registration_ids";

    //@Value("${app.firebase-configuration-url}")
    private static String firebaseUrl = "https://fcm.googleapis.com/fcm/send";

    //@Value("${app.firebase-configuration-server-key}")
    private static String serverKey = "AAAAQAV7XxA:APA91bGcY7R0KVoNP-9-JsNII62F2Vy-r_FzzqzMDqOo4SSKsVxdnieysqpNiQjIZYfqMUIpgUcfg8-Ei9d1CiTvi7xPCAZRlOvT9gl9CjNZxs5aKdZc9DH87LWbxWZh3IjwktFj6jVV";
    //final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    /**
     * Method to send push notification to Android FireBased Cloud messaging
     * Server.
     *
     * @param tokenId Generated and provided from Android Client Developer
     * @param message which contains actual information.
     */

    public void sendNotification(String tokenId, String message) {
        try {
            // Create URL instance.
            URL url = new URL(firebaseUrl);
            // create connection.
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //set method as POST or GET
            conn.setRequestMethod(METHOD_POST);
            //pass FCM server key
            conn.setRequestProperty("Authorization", "key=" + serverKey);
            //Specify Message Format
            conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
            //Create JSON Object & pass value
            JSONObject infoJson = new JSONObject();

            infoJson.put(JSON_TITLE, "Alankit");
            infoJson.put(JSON_BODY, message);

            JSONObject json = new JSONObject();
            json.put(JSON_TO, tokenId.trim());
            json.put(JSON_NOTIFICATION, infoJson);

            logger.info("json : {}", json.toString());
            logger.info("infoJson :{}", infoJson.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            int status = 0;
            if (null != conn) {
                status = conn.getResponseCode();
            }
            if (status != 0) {

                if (status == 200) {
                    //SUCCESS message
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    logger.info("Android Notification Response : {}", reader.readLine());
                } else if (status == 401) {
                    //client side error
                    logger.error("Notification Response : TokenId : {} ,{}", tokenId, " Error occurred :");
                } else if (status == 501) {
                    //server side error
                    logger.error("Notification Response : [ errorCode=ServerError ] TokenId : {}", tokenId);
                } else if (status == 503) {
                    //server side error
                    logger.error("Notification Response : FCM Service is Unavailable	TokenId : {}", tokenId);
                }
            }
        } catch (MalformedURLException mlfexception) {
            // Prototcal Error
            logger.error("Error occurred while sending push Notification!..{}", mlfexception.getMessage());
        } catch (Exception mlfexception) {
            //URL problem
            logger.error("Reading URL, Error occurred while sending push	Notification!..{}", mlfexception.getMessage());
        }

    }

    public void sendNotifications(List<String> putIds2, String tokenId, String message) {
        try {
            // Create URL instance.
            URL url = new URL(firebaseUrl);
            // create connection.
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //set method as POST or GET
            conn.setRequestMethod(METHOD_POST);
            //pass FCM server key
            conn.setRequestProperty("Authorization", "key=" + serverKey);
            //Specify Message Format
            conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
            //Create JSON Object & pass value

            JSONArray regId = null;
            JSONObject objData = null;
            JSONObject data = null;
            JSONObject notif = null;

            regId = new JSONArray();
            for (int i = 0; i < putIds2.size(); i++) {
                regId.add(putIds2.get(i));
            }
            data = new JSONObject();
            data.put(JSON_MESSAGE, message);
            notif = new JSONObject();
            notif.put(JSON_TITLE, "Alankit Universe");
            notif.put(JSON_TEXT, message);

            objData = new JSONObject();
            objData.put(JSON_REGISTRATION_IDS, regId);
            objData.put(JSON_DATA, data);
            objData.put(JSON_NOTIFICATION, notif);
            logger.info("!_@rj@_group_PASS:>{}", objData.toString());
            logger.info("json : {}", objData.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(objData.toString());
            wr.flush();
            int status = 0;
            if (null != conn) {
                status = conn.getResponseCode();
            }
            if (status != 0) {

                if (status == 200) {
                    //SUCCESS message
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    logger.info("Android Notification Response : {}", reader.readLine());
                } else if (status == 401) {
                    //client side error
                    logger.error("Notification Response : TokenId : {} {} ", tokenId, "Error occurred :");
                } else if (status == 501) {
                    //server side error
                    logger.error("Notification Response : [ errorCode=ServerError ]	TokenId : {}", tokenId);
                } else if (status == 503) {
                    //server side error
                    logger.error("Notification Response : FCM Service is Unavailable	TokenId : {}", tokenId);
                }
            }
        } catch (MalformedURLException mlfexception) {
            // Prototcal Error
            logger.error("Error occurred while sending push Notification!..{}", mlfexception.getMessage());
        } catch (IOException mlfexception) {
            //URL problem
            logger.error("Reading URL, Error occurred while sending push Notification!..{}", mlfexception.getMessage());
        } catch (Exception exception) {
            //General Error or exception.
            logger.error("Error occurred while sending push Notification!..{}", exception.getMessage());
        }

    }
}
