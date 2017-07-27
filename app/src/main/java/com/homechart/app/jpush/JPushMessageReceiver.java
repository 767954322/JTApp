package com.homechart.app.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.homechart.app.home.activity.SetActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import cn.jpush.android.api.JPushInterface;

public class JPushMessageReceiver extends BroadcastReceiver
{
    public JPushMessageReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[JPushMessageReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
        {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JPushMessageReceiver] Registration Id : " + regId);
//            registerJPushRegId(regId);
        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
        {
            Log.d(TAG, "[JPushMessageReceiver] custom message received: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))//收到通知
        {
            Log.d(TAG, "[JPushMessageReceiver] Push down notice received");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushMessageReceiver] notification ID: " + notifactionId);
        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))//打开通知
        {
            Log.d(TAG, "[JPushMessageReceiver] Notification Opened event");
            //打开自定义的Activity
            Intent i = new Intent(context, SetActivity.class);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        }
        else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
        {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[JPushMessageReceiver]" + intent.getAction() + " connected state change to " + connected);
        }
        else
        {
            Log.d(TAG, "[JPushMessageReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    private String printBundle(Bundle bundle)
    {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet())
        {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA))
            {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty())
                {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try
                {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext())
                    {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e)
                {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            }
            else
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

//    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle)
//    {
//
//        if (AdskApplication.getInstance().isChatRoomActivityInForeground())
//        {
//            //Do we need to handle this??
//            // TODO: add code incase you want to handle this condition
//        }
//        else
//        {
//            //Posting notification
//            String title = context.getResources().getString(R.string.app_name);
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String contentText = message;
//
//            try
//            {
//                JSONObject jsonObject = new JSONObject(message);
//
//                if (jsonObject != null)
//                {
//
//                    JSONArray jArray = jsonObject.getJSONArray("args");
//
//                    if (jsonObject.has("loc-key"))
//                    {
//                        String key = jsonObject.optString("loc-key");
//
//                        if (key.equalsIgnoreCase("PRIVATE_MESSAGE_AUDIO"))
//                        {
//                            if (jArray.length() > 0)
//                                contentText = context.getResources().getString(R.string.push_notification_audio_message, MPChatUtility.getUserDisplayNameFromUser(jArray.getString(0)));
//                        }
//                        else if (key.equalsIgnoreCase("PRIVATE_MESSAGE_IMAGE"))
//                        {
//                            if (jArray.length() > 0)
//                                contentText = context.getResources().getString(R.string.push_notification_image_message, MPChatUtility.getUserDisplayNameFromUser(jArray.getString(0)));
//                        }
//                    }
//                    else
//                    {
//                        if (jArray.length() > 1)
//                        {
//                            String userName = MPChatUtility.getUserDisplayNameFromUser(jArray.getString(0));
//                            String userMessage = jArray.getString(1);
//                            contentText = context.getResources().getString(R.string.push_notification_text_message, userName, userMessage);
//                        }
//                    }
//                }
//            } catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//
//            //creating basic notification UI
//            // You can create custom notification UI also if you want
//            NotificationManager nm;
//            NotificationCompat.Builder builder;
//            int notificationId = getNotificationId(bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            int unreadMessageCount = getUnreadMessagesCount(context, String.valueOf(notificationId));
//
//            //this default notification id is only used when we are not getting member id in
//            //json payload. This case may not hit but just adding this for fallback mechanism
//            boolean isDefaultNotificationId = isDefaultNotificationId(notificationId);
//            String description = null;
//            unreadMessageCount = unreadMessageCount + 1;
//
//            if (unreadMessageCount > 1)
//            {
//                if (isDefaultNotificationId)
//                    description = context.getResources().getString(R.string.push_notification_unread_message, unreadMessageCount);
//                else
//                    contentText = "(" + unreadMessageCount + ")" + " " + contentText;
//            }
//
//            updateUnreadMessagesCount(context, String.valueOf(notificationId), unreadMessageCount);
//
//            builder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.mipmap.icon_launcher_label)
//                    .setContentTitle(title)
//                    .setContentText(contentText)
//                    .setContentIntent(getNotificationContentIntent(context, bundle));
//
//            if (description != null)
//            {
//                builder.setContentText(description);
//                builder.setSubText(contentText);
//            }
//
//            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.push_notification);
//
//            if (sound != null)
//                builder.setSound(sound);
//
//            builder.setAutoCancel(true);
//            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Notification notification = builder.build();
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//            nm.notify(getNotificationId(1), notification);
//        }
//    }
//
//    /**
//     * 获取提示类型
//     * @param notificationId
//     * @return
//     */
//    public int getNotificationId(int notificationId) {
//        switch (notificationId) {
//            case 1:
//                return 1;
//            default:
//                return 0;
//
//
//        }
//    }

//    private void registerJPushRegId(String regId)
//    {
//        SharedPreferencesUtils.writeString(REGID, regId);
//
//        //if log-in register this ID with marketplace
//        // please note that this regid will be treated as device id in Marketplace API
//
//        MemberEntity memberEntity = AdskApplication.getInstance().getMemberEntity();
//
//        if (memberEntity != null)
//        {
//            PushNotificationHttpManager.registerDeviceWithMarketplace(regId, new OkStringRequest.OKResponseCallback()
//            {
//                @Override
//                public void onErrorResponse(VolleyError volleyError)
//                {
//
//                }
//
//                @Override
//                public void onResponse(String s)
//                {
//
//                }
//            });
//        }
//    }
//
//    private PendingIntent getNotificationContentIntent(Context context, Bundle bundle)
//    {
//        Intent notificationIntent = null;
//        notificationIntent = new Intent(context, AdskApplication.getInstance().getSplashActivityClass());
//        notificationIntent.putExtras(bundle);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return contentIntent;
//    }
//
//    private int getNotificationId(String jsonPayload)
//    {
//        String notificationKey = null; //same as notification Id eventually
//        int notificationId = DEFAULT_NOTIFICATION_ID;
//
//        try
//        {
//            JSONObject jsonObject = new JSONObject(jsonPayload);
//
//            if (jsonObject != null)
//            {
//
//                JSONArray jArray = jsonObject.getJSONArray("data");
//
//                if (jsonObject != null && jArray.length() > 1)
//                    notificationKey = jArray.getString(1); //get member id
//                else if (jsonObject.has("appId"))//give app level id
//                    notificationKey = jsonObject.getString("appId");
//
//            }
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//
//        if (notificationKey != null && !notificationKey.isEmpty())
//        {
//            try
//            {
//                notificationId = Integer.parseInt(notificationKey);
//            } catch (Exception e)
//            {
//                notificationId = DEFAULT_NOTIFICATION_ID; //safer side
//            }
//        }
//
//        return notificationId;
//    }
//
//
//    private int getUnreadMessagesCount(Context context, String messageKey)
//    {
//        int unreadMessageCount = 0;
//
//        SharedPreferences sharedpreferences = context.getSharedPreferences(AdskApplication.JPUSH_STORE_KEY, Context.MODE_PRIVATE);
//
//        if (sharedpreferences != null)
//            unreadMessageCount = sharedpreferences.getInt(messageKey, 0);
//
//        return unreadMessageCount;
//    }
//
//
//    private void updateUnreadMessagesCount(Context context, String messageKey, int unreadMessageCount)
//    {
//        SharedPreferences sharedpreferences = context.getSharedPreferences(AdskApplication.JPUSH_STORE_KEY, Context.MODE_PRIVATE);
//
//        if (sharedpreferences != null)
//        {
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putInt(messageKey, unreadMessageCount);
//            editor.commit();
//        }
//
//    }
//
//    private boolean isDefaultNotificationId(int notificationId)
//    {
//        return (notificationId == DEFAULT_NOTIFICATION_ID);
//    }


    private static final String TAG = "JPush";
    public static final String REGID = "com.autodesk.easyhome.marketplace.REGID";
    public static final int DEFAULT_NOTIFICATION_ID = 1001;
}
