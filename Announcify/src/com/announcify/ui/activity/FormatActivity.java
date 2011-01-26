
package com.announcify.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.announcify.R;

public class FormatActivity extends BaseActivity {
    private EditText formatstring;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarContentView(R.layout.activity_formatstring);

        formatstring = (EditText)findViewById(R.id.edit_formatstring);

        findViewById(R.id.button_name).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<NAME>");
            }
        });
        findViewById(R.id.button_firstname).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<FIRSTNAME>");
            }
        });
        findViewById(R.id.button_lastname).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<LASTNAME>");
            }
        });
        findViewById(R.id.button_nickname).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<NICKNAME>");
            }
        });
        findViewById(R.id.button_fullname).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<FULLNAME>");
            }
        });
        findViewById(R.id.button_title).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<TITLE>");
            }
        });
        findViewById(R.id.button_address).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<ADDRESS>");
            }
        });
        findViewById(R.id.button_address_type).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<ADDRESSTYPE>");
            }
        });
        findViewById(R.id.button_event).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<EVENT>");
            }
        });
        findViewById(R.id.button_message).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                formatstring.append("<MESSAGE>");
            }
        });
    }
}
