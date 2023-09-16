package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class SimpleJDBCRepository {
    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String CREATE_USER_SQL = "insert into myusers (firstname, lastname, age) values ((?), (?), (?))";
    private static final String UPDATE_USER_SQL = "update myusers set id = (?), firstname = (?), lastname = (?), age = (?) where id = (?)";
    private static final String DELETE_USER = "delete from myusers where id = (?)";
    private static final String FIND_USER_BY_ID_SQL = "select * from myusers where id = (?)";
    private static final String FIND_USER_BY_NAME_SQL = "select * from myusers where firstname = (?)";
    private static final String FIND_ALL_USER_SQL = "select * from myusers";

    public Long createUser(User user) {
        Long id = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public User findUserById(Long userId) {
        User resultUser = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_ID_SQL)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultUser = new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    public User findUserByName(String userName) {
        User resultUser = null;
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_NAME_SQL)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultUser = new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultUser;
    }

    public List<User> findAllUser() {
        List<User> resultList = new ArrayList<>();
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_ALL_USER_SQL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultList.add(new User(rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public User updateUser(User user) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL)) {
            ps.setLong(1, user.getId());
            ps.setLong(5, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setInt(4, user.getAge());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUser(Long userId) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
