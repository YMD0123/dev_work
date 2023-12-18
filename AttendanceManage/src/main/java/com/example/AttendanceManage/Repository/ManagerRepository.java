package com.example.AttendanceManage.Repository;


import com.example.AttendanceManage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ManagerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> list = new ArrayList<>();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            User user = new User (
                    Integer.parseInt(map.get("id").toString()),
                    map.get("username").toString(),
                    map.get("role").toString(),
                    map.get("department_code").toString());
            list.add(user);
        }
        return list;
    }

    public boolean userInsert(String username, String password, String role, String department_code) {

        String sql = "INSERT INTO users (username, password, role, department_code)" +
                "VALUES (?, ?, ?, ?)";
        String hashPassword;

        try {
            hashPassword = getMD5Hash(password);
            jdbcTemplate.update(sql, username, hashPassword, role, department_code);
        } catch (Exception e) {
            return false;
        }
        System.out.println("*********新規登録完了*********");
        System.out.println("username        :" + username);
        System.out.println("password        :" + password);
        System.out.println("role            :" + role);
        System.out.println("department_code :" + department_code);

        return true;
    }

    public User userEditView(int userId) {

        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try {
            Map<String, Object> user_map = jdbcTemplate.queryForMap(sql, userId);
            user = mapToUser(user_map);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
        return user;
    }

    public boolean userDelete(int userId) {

        String sql = "DELETE FROM users WHERE id = ?";

        try {
            jdbcTemplate.update(sql, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean userUpdate(String username, String role, String department_code, int id) {
        // TODO ユーザー更新メソッド作成
        String sql = "UPDATE users SET username = ? role = ? department_code = ?  WHERE id = ?";

        try {
            jdbcTemplate.update(sql, username, role, department_code, id);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
            return false;
        }
        System.out.println("更新完了");
        return true;
    }

    private User mapToUser(Map user_map) {
        User user = new User();
        user.setId((int)user_map.get("id"));
        user.setUsername((String) user_map.get("username"));
        user.setRole((String) user_map.get("role"));
        user.setDepartmentCode((String) user_map.get("department_code"));
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

}
