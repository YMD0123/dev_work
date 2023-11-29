package com.example.AttendanceManage.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean Login(int userid, String password) {

        String sql = "SELECT password FROM users WHERE id = ?";
        String getPassword;
        String result;

        try {
            result = getMD5Hash(password);
            getPassword = jdbcTemplate.queryForObject(sql, String.class, userid);
        } catch (Exception e) {
            return false;
        }

        // passwordとpostされたgetPasswordを比較
        if (result.equals(getPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // passwordをハッシュ化
    private String getMD5Hash(String stringToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(stringToHash.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 hashing algorithm not found", e);
        }
    }
}