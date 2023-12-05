package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

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
        System.out.println("result      : " + result);
        System.out.println("getPassword : " + getPassword);

        // passwordとpostされたgetPasswordを比較
        if (result.equals(getPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserInfo(int userid){
        String sql = "SELECT * FROM users WHERE id = ?";
        Map<String,Object> user_map = jdbcTemplate.queryForMap(sql,userid);
        User user = mapToUser(user_map);
        return user;
    }

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

    private User mapToUser(Map user_map){
        User user = new User();
        user.setId((int)user_map.get("id"));
        user.setUsername((String) user_map.get("username"));
        user.setRole((String) user_map.get("role"));
        user.setDepartmentCode((String) user_map.get("department_code"));
        return user;
    }
}