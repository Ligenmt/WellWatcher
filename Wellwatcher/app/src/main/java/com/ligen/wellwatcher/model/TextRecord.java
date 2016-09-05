package com.ligen.wellwatcher.model;

import android.nfc.NdefRecord;

import java.util.Arrays;

/**
 * nfc文本记录
 * Created by mac on 16/2/16.
 */
public class TextRecord {

    private String mText;
    private TextRecord(String text){
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public static TextRecord parseRecord(NdefRecord record) {

        if(record.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        if(!Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            byte[] payload = record.getPayload();
            //16进制80 为2进制 10000000
            String encoding = ((payload[0] & 0x80) == 0) ? "utf-8" : "utf-16";

            int lanCodeLength = (payload[0] & 0x3f);
            String lanCode = new String(payload, 1, lanCodeLength, "US-ASCII");

            String text = new String(payload, lanCodeLength+1, payload.length-lanCodeLength-1, encoding);
            return new TextRecord(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
