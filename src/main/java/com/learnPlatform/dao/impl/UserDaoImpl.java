package com.learnPlatform.dao.impl;

import com.learnPlatform.dao.UserDao;
import com.learnPlatform.model.entity.User;
import com.learnPlatform.model.type.UserRole;
import com.learnPlatform.model.type.UserStatus;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final DataSource dataSource;

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM fintrack.users WHERE login = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по логину", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE fintrack.users " +
                "SET login = ?, password = ?, name = ?, user_role = ?, user_status = ?, updated_at = now() " +
                "WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                user.onUpdate();
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getName());
                stmt.setObject(4, user.getUserRole().name());
                stmt.setObject(5, user.getUserStatus().name());
                stmt.setLong(6, user.getId());

                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при обновлении пользователя", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с базой данных", e);
        }
    }


    @Override
    public void delete(Long userId) {
        String sql = "DELETE FROM fintrack.users WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при удалении пользователя", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с базой данных", e);
        }
    }


    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM fintrack.users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM fintrack.users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }
        return users;
    }

    @Override
    public User save(User entity) {
        String sql = "INSERT INTO fintrack.users (login, password, name, user_role, user_status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now(), now()) RETURNING id";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, entity.getLogin());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getName());
                stmt.setObject(4, entity.getUserRole().name());
                stmt.setObject(5, entity.getUserStatus().name());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при сохранении пользователя", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с базой данных", e);
        }

        return entity;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .name(rs.getString("name"))
                .userRole(UserRole.valueOf(rs.getString("user_role")))
                .userStatus(UserStatus.valueOf(rs.getString("user_status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

}


