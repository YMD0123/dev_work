package com.example.AttendanceManage.DBtest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.util.List;

public class ConectDBTest {
    public static void main(String[] args) {
        // データソースの設定
        DataSource dataSource = new DriverManagerDataSource(
                "jdbc:postgresql://localhost:5432/akbc", "ishikawakoki", "");

        // JdbcTemplateのインスタンス化
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // データベースからデータを取得するための簡単なクエリ
        String sql = "SELECT * FROM users"; // 適切なテーブル名に置き換えてください
        List<?> data = jdbcTemplate.queryForList(sql);

        // 結果の出力
        for (Object row : data) {
            System.out.println(row.toString());
        }
    }
}
