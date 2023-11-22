package com.example.AttendanceManage.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    BCryptPasswordEncoder passwordEncoder;

    public boolean Login(int userid, String password) {

        String sql = "SELECT password FROM users WHERE id = ?";
        String getPassword;

        try {
            getPassword = jdbcTemplate.queryForObject(sql, String.class, userid);
        } catch (Exception e) {
            return false;
        }
        // passwordとpostされたgetPasswordを比較
        return passwordEncoder.matches(password, getPassword);
    }
}