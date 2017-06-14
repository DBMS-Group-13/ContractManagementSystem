package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.UserDao;
import model.User;
import model.Customer;
import model.Log;
import utils.AppException;
import utils.DBUtil;

/**
 * User data access layer implementation class
 */
public class UserDaoImpl implements UserDao {

	/**
	 *  Verify whether exist user that has the same name
	 * 
	 * @param name User name
	 * @return  Return true if there are users with same name,otherwise return false 
	 * @throws AppException
	 */
	public boolean isExist(String name) throws AppException {
		Connection conn = null; // Define database connection object
		PreparedStatement psmt = null;// Define PreparedStatement object
		ResultSet rs = null;// Define ResultSet object

		boolean flag = false;// Operation flag
		try {
			conn = DBUtil.getConnection();// Create database connection
			// Declare operation statement, query records based on user name, "?" is a placeholder
			String sql = "select id from t_user where name = ? and del = 0";

			psmt = conn.prepareStatement(sql);//  Pre-compiled sql
			psmt.setString(1, name);// Set values for the placeholder 

			rs = psmt.executeQuery();// Execute the query, return the query results
			if (rs.next()) {// Determine whether there are values in results set
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.isExist");
		} finally {
			DBUtil.closeResultSet(rs);// Close database query result set
			DBUtil.closeStatement(psmt);//  Close database object pretreatment
			DBUtil.closeConnection(conn);// Close database connection object
		}
		return flag;
	}

	/**
	 * Save user information
	 * 
	 * @param User object
	 * @return Return true if saved successfully,otherwise return false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(User user) throws AppException {
		Connection conn = null; // Define database connection object
		PreparedStatement psmt = null;// Define PreparedStatement object
		
		boolean flag = false;// Operation flag
		try {
			conn = DBUtil.getConnection();// Create database connection
			// Declare operation statement,save user information into database, "?" is a placeholder
			String sql = "insert into t_user (name,password,sec_password,email,token,activatetime,createdate,status)"
					+ " values (?,?,?,?,?,?,?,?)";
			
			psmt = conn.prepareStatement(sql);// Pre-compiled sql
			// Set values for the placeholder 
			psmt.setString(1, user.getName());
			psmt.setString(2, user.getPassword());
			psmt.setString(3, user.getSecPassword());
			psmt.setString(4, user.getEmail());
			psmt.setString(5, user.getToken());
			psmt.setLong(6, user.getActivateTime());
			psmt.setString(7, user.getCreateDate());
			psmt.setInt(8, user.getStatus());
			flag = psmt.execute();// Execute the update operation,return the affected rows
			if (flag == true) {
				String content = "User" + user.getId() + "insert data into t_user";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); // pre-compiled sql
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				// Set values for the placeholder
				psmt.setInt(1, user.getId());
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);//  Close database object pretreatment
			DBUtil.closeConnection(conn);// Close database connection object
		}
		return flag;
	}
	
	/**
	 * Query user number according to the user name and password
	 * 
	 * @param name 
	 * @param password 
	 * @return User number
	 * @throws AppException
	 */
	public int login(String name, String password) throws AppException {
		int userId = -1; // Initialize userId
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user id according to the user name and password , "?" is a placeholder
			String sql = "select id from t_user where name = ? and password = ? and del = 0";
			//  pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder
			psmt.setString(1, name);
			psmt.setString(2, password);
			// Execute the query operation
			rs = psmt.executeQuery();
			// Query record and get  user id
			if (rs.next()) {
				userId = rs.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.login");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return userId;
	}
	
	/**
	 * Query user information according to id
	 * 
	 * @param id User id
	 * @return User User object
	 * @throws AppException
	 */
	public User getById(int id) throws AppException {
		// Declare user object
		User user = null;
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user information according to the user id , "?" is a placeholder
			String sql = "select *"
					+"from t_user "
					+"where id = ? and del = 0";
			// pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder
			psmt.setInt(1, id);
			// Query resultSet
			rs = psmt.executeQuery();
			
			// Save user information in Pole entity object when queried out resultSet
			if (rs.next()) {
				user = new User(); // Instantiate user objects
				// Set value to user object
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setSecPassword(rs.getString("sec_password"));
				user.setEmail(rs.getString("email"));
				user.setToken(rs.getString("token"));
				user.setActivateTime(rs.getLong("activateTime"));
				user.setCreateDate(rs.getString("createdate"));
				user.setStatus(rs.getInt("status"));
				user.setDel(rs.getInt("del"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return user;
	}

	/**
	 * Query user id set
	 * 
	 * @return User id set
	 * @throws AppException
	 */
	public List<Integer> getIds() throws AppException {
		// Initialiaze ids
		List<Integer> ids = new ArrayList<Integer>();
		
		//Declare Connection object,PreparedStatement object and ResultSet object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user id set,"?" is a placeholder
			String sql = "select id from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.getIds");
		} finally {
			// Close database operation object, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return ids;
	}
	
	//获取所有用户信息，返回User类型List
	public List<User> getUsers() throws AppException {
		User user = null;
		
		List<User> users = new ArrayList<User>();
		
		//Declare Connection object,PreparedStatement object and ResultSet object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user id set,"?" is a placeholder
			String sql = "select * from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setSecPassword(rs.getString("sec_password"));
				user.setEmail(rs.getString("email"));
				user.setToken(rs.getString("token"));
				user.setActivateTime(rs.getLong("activateTime"));
				user.setCreateDate(rs.getString("createdate"));
				user.setStatus(rs.getInt("status"));
				user.setDel(rs.getInt("del"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.getIds");
		} finally {
			// Close database operation object, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return users;
	}
	
	//获取所有用户信息，返回User类型List
	public List<Customer> getCustomers() throws AppException {
		Customer customer = null;
		
		List<Customer> customers = new ArrayList<Customer>();
		
		//Declare Connection object,PreparedStatement object and ResultSet object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user id set,"?" is a placeholder
			String sql = "select * from t_customer where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				customer = new Customer();
				customer.setId(rs.getInt("id"));
				customer.setNum(rs.getString("num"));
				customer.setName(rs.getString("name"));
				customer.setAddress(rs.getString("address"));
				customer.setTel(rs.getString("tel"));
				customer.setFax(rs.getString("fax"));
				customer.setCode(rs.getString("code"));
				customer.setBank(rs.getString("bank"));
				customer.setAccount(rs.getString("account"));
				customer.setDel(rs.getInt("del"));
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.getIds");
		} finally {
			// Close database operation object, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return customers;
	}

	//根据邮箱地址，返回用户信息
	public User getByEmail(String email) throws AppException {
		// Declare user object
		User user = null;
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			
			// Declare operation statement:query user information according to the user id , "?" is a placeholder
			String sql = "select * "
					+"from t_user "
					+"where email = ? and del = 0";
			// pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder
			psmt.setString(1, email);
			// Query resultSet
			rs = psmt.executeQuery();
			
			// Save user information in Pole entity object when queried out resultSet
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setSecPassword(rs.getString("sec_password"));
				user.setEmail(rs.getString("email"));
				user.setToken(rs.getString("token"));
				user.setActivateTime(rs.getLong("activatetime"));
				user.setCreateDate(rs.getString("createdate"));
				user.setStatus(rs.getInt("status"));
				user.setDel(rs.getInt("del"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return user;
	}
	
	//修改用户信息
	@SuppressWarnings("resource")
	public boolean UpdateUser(User user) throws AppException {
		boolean flag = false;// Operation flag
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user information according to the user id , "?" is a placeholder
			String sql = "update t_user set name = ?,password = ?,sec_password = ?,"
					+ "email = ?,token = ? ,activateTime = ?,createdate = ?,status = ?,del = ? "
					+ "where id = ?";
			// pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder
			psmt.setString(1, user.getName());
			psmt.setString(2, user.getPassword());
			psmt.setString(3, user.getSecPassword());
			psmt.setString(4, user.getEmail());
			psmt.setString(5, user.getToken());
			psmt.setLong(6, user.getActivateTime());
			psmt.setString(7, user.getCreateDate());
			psmt.setInt(8, user.getStatus());
			psmt.setInt(9, user.getDel());
			psmt.setInt(10, user.getId());
			// Query resultSet
			flag = psmt.execute();
			
			// Save user information in Pole entity object when queried out resultSet
			if (flag == true) {
				String content = "User" + user.getId() + "update data in t_user";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); // pre-compiled sql
				// Set values for the placeholder
				psmt.setInt(1, user.getId());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	//给定email，判断是否有重复
	public boolean JudgeEmail(String email) throws AppException {
		boolean flag = false;// Operation flag
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
            String sql = "select email from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				String formal_email = rs.getString("email");
				if(formal_email != null){
					if(formal_email.compareTo(email) == 0){
						flag = true;
						}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	//添加客户
	@SuppressWarnings("resource")
	public boolean addCustomer(Customer customer) throws AppException {
		Connection conn = null; // Define database connection object
		PreparedStatement psmt = null;// Define PreparedStatement object
		
		boolean flag = false;// Operation flag
		try {
			conn = DBUtil.getConnection();// Create database connection
			// Declare operation statement,save user information into database, "?" is a placeholder
			String sql = "insert into t_customer (id,num,name,address,tel,fax,code,bank,account,del)"
					+ " values (?,?,?,?,?,?,?,?,?,?)";
			
			psmt = conn.prepareStatement(sql);// Pre-compiled sql
			// Set values for the placeholder 
			psmt.setInt(1, customer.getId());
			psmt.setString(2, customer.getNum());
			psmt.setString(3, customer.getName());
			psmt.setString(4, customer.getAddress());
			psmt.setString(5, customer.getTel());
			psmt.setString(6, customer.getFax());
			psmt.setString(7, customer.getCode());
			psmt.setString(8, customer.getBank());
			psmt.setString(9, customer.getAccount());
			psmt.setInt(10, customer.getDel());
			flag = psmt.execute();// Execute the update operation,return the affected rows
			if (flag == true) {
				String content = "User insert data into t_customer";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2); // pre-compiled sql
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				// Set values for the placeholder
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);//  Close database object pretreatment
			DBUtil.closeConnection(conn);// Close database connection object
		}
		return flag;
	}
	
	//禁用账号，Del属性设为1
	@SuppressWarnings("resource")
	public boolean setUserDel(int user_id) throws AppException {
		boolean flag = false;// Operation flag
		// Declare database connection object, pre-compiled object
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare sql:update contract information according to contract id
			String sql = "update t_user set del = 1 where id = ?";
			// Pre-compiled sql, and set the parameter values
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, user_id);

			// Execute update,return affected rows
			flag = psmt.execute();
			
			if (flag == true) {// If affected lines greater than 0, so update success
				String content = "User" + user_id + " update data in t_user";
				String sql5 = "insert into t_log(time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql5); // pre-compiled sql
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setInt(1, user_id);
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.ContractDaoImpl.updateById");
		} finally {
			// Close database operation object
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	//修改客户信息
	@SuppressWarnings("resource")
	public boolean UpdateCustomer(Customer customer) throws AppException {
		boolean flag = false;// Operation flag
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			int id = customer.getId();
			String num = customer.getNum();
			String name = customer.getName();
			String address = customer.getAddress();
			String tel = customer.getTel();
			String fax = customer.getFax();
			String code = customer.getCode();
			String bank = customer.getBank();
			String account = customer.getAccount();
			int del = customer.getDel();
			
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user information according to the user id , "?" is a placeholder
			String sql = "update t_customer set num = ?, name = ?,"
					+ "address = ?,tel = ?,fax = ?,code = ?,"
					+ "bank = ?,account = ?,del = ? "
					+ "where id = ?";
			// pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder
			psmt.setString(1, num);
			psmt.setString(2, name);
			psmt.setString(3, address);
			psmt.setString(4, tel);
			psmt.setString(5, fax);
			psmt.setString(5, code);
			psmt.setString(5, bank);
			psmt.setString(5, account);
			psmt.setInt(6, del);
			psmt.setInt(7, id);
			// Query resultSet
			flag = psmt.execute();
			
			// Save user information in Pole entity object when queried out resultSet
			if (flag == true) {
				String content = "User update data in t_customer";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2); // pre-compiled sql
				// Set values for the placeholder
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(1, date);
				psmt.setString(2, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	//删除顾客，Del属性设为1
	@SuppressWarnings("resource")
	public boolean setCustomerDel(int id) throws AppException {
		boolean flag = false;// Operation flag
		// Declare database connection object, pre-compiled object
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare sql:update contract information according to contract id
			String sql = "update t_customer set del = 1 "
					+ "where id = ?";
			// Pre-compiled sql, and set the parameter values
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, id);

			// Execute update,return affected rows
			flag = psmt.execute();
			
			if (flag == true) {// If affected lines greater than 0, so update success
				String content = "User  update data in t_customer";
				String sql5 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql5); // pre-compiled sql
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(1, date);
				psmt.setString(2, content);
				psmt.executeUpdate();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.ContractDaoImpl.updateById");
		} finally {
			// Close database operation object
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	//获取所有日志
	public List<Log> getLogs() throws AppException {
		Log log = null;
		
		List<Log> logs = new ArrayList<Log>();
		
		//Declare Connection object,PreparedStatement object and ResultSet object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user id set,"?" is a placeholder
			String sql = "select * from t_log where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				log = new Log();
				log.setId(rs.getInt("id"));
				log.setUserId(rs.getInt("userId"));
				log.setContent(rs.getString("content"));
				log.setTime(rs.getDate("time"));
				log.setDel(rs.getInt("del"));
				logs.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.getLogs");
		} finally {
			// Close database operation object, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return logs;
	}
	
	//给定用户名和密码，判断是否激活
	public int JudgeUser(String name) throws AppException {
		int status = 0;
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
            String sql = "select name,status from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				String formal_name = rs.getString("name");
				if(formal_name != null){
					if(formal_name.compareTo(name) == 0){
						status = rs.getInt("status");
						}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			// Close database object operation, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return status;
	}
}
