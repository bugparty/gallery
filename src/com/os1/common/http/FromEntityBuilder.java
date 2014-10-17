package com.os1.common.http;

import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanbowen on 2014/10/8.
 */
public class FromEntityBuilder {
        private List<NameValuePair> nameValuePairs;

        private FromEntityBuilder() {
            this.nameValuePairs = new ArrayList<NameValuePair>();
        }
        public static FromEntityBuilder create(){
            FromEntityBuilder p = new FromEntityBuilder();
            return p;
        }
        public FromEntityBuilder add(String key, String value) {
            nameValuePairs.add(new BasicNameValuePair(key,
                    value));
            return this;
        }

        public FromEntityBuilder add(TextView tvKey, EditText etValue) {
            nameValuePairs.add(new BasicNameValuePair(tvKey.getText().toString(),
                    etValue.getText().toString()));
            return this;
        }

        public FromEntityBuilder add(EditText etKey, EditText etValue) {
            nameValuePairs.add(new BasicNameValuePair(etKey.getText().toString(),
                    etValue.getText().toString()));
            return this;
        }

        public UrlEncodedFormEntity build() {
            UrlEncodedFormEntity entity;
            try {
                 entity = new UrlEncodedFormEntity(nameValuePairs);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
            return entity;
        }

}
