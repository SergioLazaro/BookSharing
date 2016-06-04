package com.example.android.booksharing.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Sergio on 2/6/16.
 */
public class User {

    // The higher the number of iterations the more
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 65536;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 512;

    private String username, password, hash;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.hash = bin2hex(generateHash(password));
    }

    private byte[] generateHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }
    private String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getHash(){
        return hash;
    }

    public String generateJSONObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("username",username);
            obj.put("password",hash);
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
