package com.ligen.wellwatcher.util;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.ligen.wellwatcher.model.TextRecord;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by ligen on 2016/5/28.
 */
public class NfcUtil {

    private static final String TAG = "NfcUtil";

    /**
     * 读取nfc标签
     */
    public static String readNfcTag(Intent intent) {

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.i(TAG, "read nfc success");
        }
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] messages = null;
        if(rawMsgs != null) {
            messages = new NdefMessage[rawMsgs.length];
            for(int i=0; i<messages.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
            }
        }
        if(messages != null) {
            NdefRecord record = messages[0].getRecords()[0];
            TextRecord textRecord = TextRecord.parseRecord(record);
            String content = textRecord.getText();
            Log.i(TAG, "nfc text:" + content);
            return content;
        }
        return null;
    }

    /**
     * 写入文本
     * @param message
     * @param tag
     * @return
     */
    public static boolean writeTag(Context context, NdefMessage message, Tag tag) {

        try {
            Ndef ndef = Ndef.get(tag);
            if(ndef == null){
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if(ndefFormatable != null) {
                    ndefFormatable.connect();
                    ndefFormatable.format(message);
                    Toast.makeText(context, "格式化..", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            ndef.connect();
            ndef.writeNdefMessage(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 创建Record
     * @param text
     * @return
     */
    public static NdefRecord createTextRecord(String text) {

        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utf = Charset.forName("UTF-8");
        byte[] textBytes = text.getBytes(utf);
        int utfBit = 0;
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[1], data);
        return record;

    }


}
