package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.duolingo.app.models.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    // Hỗ trợ đăng nhập bằng cả Email hoặc Username
    @Query("SELECT * FROM users WHERE (email = :identifier OR username = :identifier) AND password = :password LIMIT 1")
    User login(String identifier, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

    // Kiểm tra xem Email hoặc Username đã tồn tại chưa
    @Query("SELECT EXISTS(SELECT * FROM users WHERE email = :email OR username = :username)")
    boolean isUserExists(String email, String username);

    @Update
    void update(User user);
}