package com.ligen.wellwatcher.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ligen.wellwatcher.R;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * 写入nfc文本
 */
public class NfcWriteActivity extends AppCompatActivity {

    private static final String TAG = "NfcWriteActivity";
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPeningIntent;

    private EditText mEtText;
    private Button mBtnWrite;
    private Button mBtnBack;
    private TextView mTvText;

    private String mText;
    AlertDialog dialog;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPeningIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);

        if (mNfcAdapter == null) {
            finish();
        }

        mEtText = (EditText) findViewById(R.id.et_write_text);
        mBtnWrite = (Button) findViewById(R.id.btn_write_text);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mTvText = (TextView) findViewById(R.id.tv_write_text);

        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEtText.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(NfcWriteActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mText = text;
                mTvText.setText("巡检项目名为：" + text + "，请将标签靠近！");
                mEtText.setText(text);
                mEtText.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(NfcWriteActivity.this);
                View dialogView = View.inflate(NfcWriteActivity.this, R.layout.dialog_nfc_image, null);
                builder.setView(dialogView);
                dialog = builder.show();
                dialog.setCanceledOnTouchOutside(false);
                Button btnBack = (Button) dialogView.findViewById(R.id.btn_back);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 感应到标签后触发此方法
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //输入检测
        if (TextUtils.isEmpty(mText)) {
            Toast.makeText(this, "你还未输入文本！", Toast.LENGTH_LONG).show();
            return;
        }
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage message = new NdefMessage(new NdefRecord[]{
                createTextRecord(mText)
        });
        if (writeMessageToNfc(message, tag)) {
            Toast.makeText(NfcWriteActivity.this, "写入成功！", Toast.LENGTH_SHORT).show();
            if(dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    /**
     * 确保此界面优先触发感应
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPeningIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    /**
     * 写入文本至nfc标签
     *
     * @param message
     * @param tag
     * @return
     */
    private boolean writeMessageToNfc(NdefMessage message, Tag tag) {

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                //检查标签是否已格式化
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if (ndefFormatable != null) {
                    ndefFormatable.connect();
                    ndefFormatable.format(message);
                    Toast.makeText(NfcWriteActivity.this, "格式化标签..", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
            ndef.connect();
            ndef.writeNdefMessage(message);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "写入标签失败");
        }
        return false;

    }

    /**
     * 将message写入record
     * @param text
     * @return
     */
    private NdefRecord createTextRecord(String text) {

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
