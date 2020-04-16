package ucas.iie.rd6.mobileinfo.user;

import android.app.AlarmManager;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.ContactsContract.Contacts.CONTENT_URI;

class Phone{
    private static final String CANNOT_EXTRACT = "Cannot extract";

    public static String getPhoneNumber(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return CANNOT_EXTRACT;
        }

        try {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (subscriptionManager == null) {
                return CANNOT_EXTRACT;
            }

            List<SubscriptionInfo> list = subscriptionManager.getActiveSubscriptionInfoList();
            if (list != null) {
                List<String> results = new ArrayList<>();
                for (SubscriptionInfo subscriptionInfo : list) {
                    results.add(subscriptionInfo.getNumber());
                }

                return TextUtils.join(", ", results);
            }
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        } catch (SecurityException ex) {
            return CANNOT_EXTRACT;
        }
    }
}


class Contacts{
    public  static String[] getContacts(Context context) {
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return new String[]{};
        }
        try {
            ArrayList<String> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME)));
            }
            return list.toArray(new String[]{});
        } finally {
            cursor.close();
        }
    }
}

class UserSms{
    public static String[] getSms(Context context) {
        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return new String[]{};
        }

        try {
            ArrayList<String> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                list.add(address + ": " + body);
            }
            return list.toArray(new String[]{});
        } finally {
            cursor.close();
        }
    }
}

class UserCalls{
    static String[] getCallLog(Context context) {
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return new String[]{};
        }
        try {
            ArrayList<String> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                String CHACHED_NAME = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String NUMBER = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String CALL_TYPE = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String DATE = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                long temp_date = Long.parseLong(DATE);
                Date date = new Date(temp_date);
                String FormatTime = new SimpleDateFormat("yyyy-MMM-ddd hhh:mmm:sss a E").format(date);
                String DURATION = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                list.add("联系人:"+ CHACHED_NAME + "\\\n" + "电话:" + NUMBER + "\\\n" + "通话类型:" + CALL_TYPE + "\\\n" + "通话日期: "+ FormatTime + "\\\n" + "通话时长:" + DURATION + "\n\n\n\n");
            }
            return list.toArray(new String[]{});
        } finally {
            cursor.close();
        }
    }
}


class AudioSetting{
    public static Map<String, String> getAudioSetting(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //通话音量

        int VIOCE_CALL_max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int VIOCE_CALL_current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        Log.d("VIOCE_CALL", "max : " + VIOCE_CALL_max + "current : " + VIOCE_CALL_current);
        //系统音量

        int SYSTEM_max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        int SYSTEM_current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        Log.d("SYSTEM", "max : " + SYSTEM_max + " current : " + SYSTEM_current);

        //铃声音量

        int RING_max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        int RING_current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
        Log.d("RING", "max : " + RING_max + " current : " + RING_current);

        //音乐音量

        int MUSIC_max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int MUSIC_current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        Log.d("MUSIC", "max : " + MUSIC_max + "current :"  + MUSIC_current);

        //提示声音音量

        int ALARM_max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        int ALARM_current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
        Log.d("ALARM", "max : " + ALARM_max + "current : " + ALARM_current);

        Map<String, String> AudioSetting = new HashMap<String,String>();
        AudioSetting.put("通话音量",String.valueOf(VIOCE_CALL_current)+"/"+String.valueOf(VIOCE_CALL_max));
        AudioSetting.put("系统音量",String.valueOf(SYSTEM_current)+"/"+String.valueOf(SYSTEM_max));
        AudioSetting.put("铃声音量",String.valueOf(RING_current)+"/"+String.valueOf(RING_max));
        AudioSetting.put("音乐音量",String.valueOf(MUSIC_current)+"/"+String.valueOf(MUSIC_max));
        AudioSetting.put("提示声音音量",String.valueOf(ALARM_current)+"/"+String.valueOf(ALARM_max));
        Log.d("Output",AudioSetting.toString());
        return AudioSetting;
    }
}

class Alarm{
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String getAlarmSetting(Context context){
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert mAlarmManager != null;
        AlarmManager.AlarmClockInfo alarmInfo = mAlarmManager.getNextAlarmClock();
        if (alarmInfo == null) {
            return "No Alarm";
        }
        long alarmTriggerTime = alarmInfo.getTriggerTime();
        Date date = new Date(alarmTriggerTime);
        String FormatTime = new SimpleDateFormat("yyyy-MMM-ddd hhh:mmm:sss a E").format(date);
        Log.d("TriggerTime", FormatTime);
        return FormatTime;
    }
}