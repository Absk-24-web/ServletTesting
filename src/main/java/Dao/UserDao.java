package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DataBase.JDBCUtils;
import Model.User;

public class UserDao {

	public void registerUser(User user) {
		String INSERT_USERS_SQL = "INSERT INTO users(name,email, password) VALUES(?, ?, ?);";
		try (Connection connection = JDBCUtils.getConnection();
				// Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());

			System.out.println(preparedStatement);
			// Step 3: Execute the query or update query
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// process sql exception
			JDBCUtils.printSQLException(e);
		}
	}
	
	
	public User getUser(int id) {
		User user = new User();
		String getUser = "select * from users where id ="+id;
		try (Connection connection = JDBCUtils.getConnection()){
				Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(getUser);
				while(rs.next()) {
				user.setId(id);
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				}
		} catch (SQLException e) {
			// process sql exception
			JDBCUtils.printSQLException(e);
		}
		return user ;
	}
	
	
	public List<User> getAllUser() throws SQLException {
		String getUser = "select * from users";
		List<User> listOfUser = new ArrayList<>();
		try (Connection connection = JDBCUtils.getConnection()){
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(getUser);
		while(rs.next()) {
			User user = new User(rs.getInt("id"),  rs.getString("name"), rs.getString("email"),
					rs.getString("password"));
			listOfUser.add(user);
		}
		}
		return listOfUser;	
	}


	public void updateUser(User user) {
		String id = String.valueOf(user.getId());
		String sql = "update users set name=?,email=?,password=? where id=?";
		try (Connection connection = JDBCUtils.getConnection()){
			PreparedStatement updateUser = connection.prepareStatement(sql);
			updateUser.setString(1, user.getName());
			updateUser.setString(2, user.getEmail());
			updateUser.setString(3, user.getPassword());
			updateUser.setString(4, id);
			updateUser.executeUpdate();
		} catch (SQLException e) {
			JDBCUtils.printSQLException(e);
		}	
	}


	public void deleteUser(int id) throws SQLException {
				
		String sql = "delete from users where id="+id;
		try (Connection connection = JDBCUtils.getConnection()){
			Statement st = connection.createStatement();
			st.executeUpdate(sql);
		}
	}
	
}
