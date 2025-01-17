package info2413_PFMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class User {
	int userId;
	String username;
	String password;
	String email;
	float budget;
	
	// User can have multiple grocery inventories
	LinkedList<GroceryInventory> inventory = new LinkedList<GroceryInventory>();
	
	// Constructor
	User(String userId, String username, String password, String email, String budget) throws Exception {
		this.userId = Integer.parseInt(userId);
		this.username = username;
		this.password = password;
		this.email = email;
		this.budget = Float.parseFloat(budget);
	}
	
	// Setters and getters
	
	// Maybe don't need setters
	public void setUsername(String newUsername){
		username = newUsername;
		// Make change in databaase
	}
	
	public void setPassword(String newPwd) {
		password = newPwd;
		// Make change in database
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
		// Make change in database
	}
	
	public void setBudget(float newBudget) {
		budget = newBudget;
		// Make change in database
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	
	
	public LinkedList<GroceryInventory> getInventory() {
		return inventory;
	}
	
	// Create User
	// Add user to database
	public static void createUser(String username, String userPwd, String email) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = App.getConnection();
			stmt = conn.prepareStatement(
					"INSERT INTO UserInfo (Username, UserPwd, Email)" +
					"VALUES ('" + username + "','" + userPwd + "','" + email + "');");
			stmt.executeUpdate();
			
			new PopupFrame(PopupType.CREATE_USER_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			new PopupFrame(PopupType.CREATE_USER_ERROR);
		} finally {
			App.closeQueitly(stmt);
			App.closeQueitly(conn);
		}
	}
	
	// Set Budget
	public static void setBudget(User user, String budget) {
		int userId = user.getUserId();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = App.getConnection();
			stmt = conn.prepareStatement("UPDATE UserInfo SET Budget = " + budget + " WHERE UserId = " + userId + ";");
			stmt.executeUpdate();
			new PopupFrame(PopupType.SET_BUDGET_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			new PopupFrame(PopupType.SET_BUDGET_ERROR);
		} finally {
			App.closeQueitly(stmt);
			App.closeQueitly(conn);
		}
	}
	
	// Manage inventories
	public void addGroceryInventory(GroceryInventory newInventory) {
		inventory.add(newInventory);
	}
	
	public void deleteGroceryInventory(GroceryInventory newInventory) {
		inventory.remove(newInventory);
	}
	
	// Get user info from database
	public static ArrayList<String[]> getUserInfo() throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		try {
			conn = App.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM UserInfo");
			
			res = stmt.executeQuery();
			
			ArrayList<String[]> array = new ArrayList<>();
			while (res.next()) {
				String[] userInfo = new String[5];
				userInfo[0] = res.getString("UserId");
				userInfo[1] = res.getString("Username");
				userInfo[2] = res.getString("UserPwd");
				userInfo[3] = res.getString("Email");
				userInfo[4] = res.getString("Budget");
				
				array.add(userInfo);
			}
			return array;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			App.closeQueitly(res);
			App.closeQueitly(stmt);
			App.closeQueitly(conn);
		}
		
		return null;
	}
	
	// Get user budget
	public float getBudget(int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		float userBudget = 0;
		try {
			conn = App.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM UserInfo");
			
			res = stmt.executeQuery();
			while (res.next()) {
				userBudget = res.getFloat("Budget");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			App.closeQueitly(res);
			App.closeQueitly(stmt);
			App.closeQueitly(conn);
		}
		return userBudget;
	}
}
