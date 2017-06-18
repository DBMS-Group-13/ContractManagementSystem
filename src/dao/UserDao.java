package dao;

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
 * User操纵层
 */
public class UserDao {

	/**
	 * 判断用户是否存在
	 * 
	 * @param name User name
	 * @return  Return true if there are users with same name,otherwise return false 
	 * @throws AppException
	 */
	public boolean isExist(String name) throws AppException {
		Connection conn = null; 
		PreparedStatement psmt = null;
		ResultSet rs = null;

		boolean flag = false;
		try {
			conn = DBUtil.getConnection();
			String sql = "select id from t_user where name = ? and del = 0";

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, name);

			rs = psmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.isExist");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}

	/**
	 * 添加用户
	 * 
	 * @param User object
	 * @return Return true if saved successfully,otherwise return false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(User user) throws AppException {
		Connection conn = null; 
		PreparedStatement psmt = null;
		
		boolean flag = false;
		try {
			conn = DBUtil.getConnection();

			String sql = "insert into t_user (name,password,sec_password,email,token,activatetime,createdate,status)"
					+ " values (?,?,?,?,?,?,?,?)";
			
			psmt = conn.prepareStatement(sql); 
			
			psmt.setString(1, user.getName());
			psmt.setString(2, user.getPassword());
			psmt.setString(3, user.getSecPassword());
			psmt.setString(4, user.getEmail());
			psmt.setString(5, user.getToken());
			psmt.setLong(6, user.getActivateTime());
			psmt.setString(7, user.getCreateDate());
			psmt.setInt(8, user.getStatus());
			flag = psmt.execute();
			//if (flag == true) {
				String content = "User" + user.getId() + "insert data into t_user";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); 
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				
				psmt.setInt(1, user.getId());
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			//}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return true;
	}
	
	/**
	 * 获取用户id
	 * 
	 * @param name 
	 * @param password 
	 * @return User number
	 * @throws AppException
	 */
	public int login(String name, String password) throws AppException {
		int userId = -1;  
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			
			conn = DBUtil.getConnection();
			
			String sql = "select id from t_user where name = ? and password = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, name);
			psmt.setString(2, password);
			
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				userId = rs.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.login");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return userId;
	}
	
	/**
	 * 根据用户id查询用户
	 * 
	 * @param id User id
	 * @return User User object
	 * @throws AppException
	 */
	public User getById(int id) throws AppException {
		 
		User user = null;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select *"
					+"from t_user "
					+"where id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, id);
			
			rs = psmt.executeQuery();
			
			
			if (rs.next()) {
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return user;
	}

	/**
	 * 查询id集
	 * 
	 * @return User id set
	 * @throws AppException
	 */
	public List<Integer> getIds() throws AppException {
		 
		List<Integer> ids = new ArrayList<Integer>();
		
		 
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try { 
			conn = DBUtil.getConnection();
			
			String sql = "select id from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.UserDaoImpl.getIds");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return ids;
	}
	
	/**
	 * 获取所有用户
	 * @return
	 * @throws AppException
	 */
	public List<User> getUsers() throws AppException {
		User user = null;
		
		List<User> users = new ArrayList<User>();
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return users;
	}
	
	/**
	 * 获取所有客户
	 * @return
	 * @throws AppException
	 */
	public List<Customer> getCustomers() throws AppException {
		Customer customer = null;
		
		List<Customer> customers = new ArrayList<Customer>();
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select * from t_customer where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return customers;
	}

	/**
	 * 根据邮件获取一会
	 * @param email
	 * @return
	 * @throws AppException
	 */
	public User getByEmail(String email) throws AppException {
		User user = null;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select * "
					+"from t_user "
					+"where email = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, email);
			rs = psmt.executeQuery();
			
			
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return user;
	}
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean UpdateUser(User user) throws AppException {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update t_user set name = ?,password = ?,sec_password = ?,"
					+ "email = ?,token = ? ,activateTime = ?,createdate = ?,status = ?,del = ? "
					+ "where id = ?";
			
			psmt = conn.prepareStatement(sql);
			
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
			
			flag = psmt.execute();
			
			
			//if (flag == true) {
				String content = "User" + user.getId() + "update data in t_user";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); 
				psmt.setInt(1, user.getId());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			//}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return true;
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 * @throws AppException
	 */
	public boolean JudgeEmail(String email) throws AppException {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
            String sql = "select email from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 添加客户
	 * @param customer
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean addCustomer(Customer customer) throws AppException {
		Connection conn = null;  
		PreparedStatement psmt = null; 
		
		boolean flag = false; 
		try {
			conn = DBUtil.getConnection(); 
			String sql = "insert into t_customer (num,name,address,tel,fax,code,bank,account)"
					+ " values (?,?,?,?,?,?,?,?)";
			
			psmt = conn.prepareStatement(sql); 
			psmt.setString(1, customer.getNum());
			psmt.setString(2, customer.getName());
			psmt.setString(3, customer.getAddress());
			psmt.setString(4, customer.getTel());
			psmt.setString(5, customer.getFax());
			psmt.setString(6, customer.getCode());
			psmt.setString(7, customer.getBank());
			psmt.setString(8, customer.getAccount());
			flag = psmt.execute(); 
			//if (flag == true) {
				String content = "User insert data into t_customer";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2);  
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
			    
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			//}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt); 
			DBUtil.closeConnection(conn); 
		}
		return true;
	}
	
	/**
	 * “删除”用户
	 * @param user_id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean setUserDel(int user_id) throws AppException {
		boolean flag = false; 
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			 
			conn = DBUtil.getConnection();
			 
			String sql = "update t_user set del = 1 where id = ?";
			 
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, user_id);

			 
			flag = psmt.execute();
			
			//if (flag == true) { 
				String content = "User" + user_id + " update data in t_user";
				String sql5 = "insert into t_log(time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql5);  
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setInt(1, user_id);
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			//}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.ContractDaoImpl.updateById");
		} finally { 
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return true;
	}
	
	/**
	 * 更新客户信息
	 * @param customer
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean UpdateCustomer(Customer customer) throws AppException {
		boolean flag = false; 
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
			
			 
			conn = DBUtil.getConnection();
			String sql = "update t_customer set num = ?, name = ?,"
					+ "address = ?,tel = ?,fax = ?,code = ?,"
					+ "bank = ?,account = ?,del = ? "
					+ "where id = ?";
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, num);
			psmt.setString(2, name);
			psmt.setString(3, address);
			psmt.setString(4, tel);
			psmt.setString(5, fax);
			psmt.setString(6, code);
			psmt.setString(7, bank);
			psmt.setString(8, account);
			psmt.setInt(9, del);
			psmt.setInt(10, id);
			 
			flag = psmt.execute();
			
			 
			//if (flag == true) {
				String content = "User update data in t_customer";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2);  
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(1, date);
				psmt.setString(2, content);
				psmt.executeUpdate();
			//}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.UserDaoImpl.getById");
		} finally { 
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return true;
	}
	
	/**
	 * “删除客户”
	 * @param id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean setCustomerDel(int id) throws AppException {
		boolean flag = false; 
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			 
			conn = DBUtil.getConnection();
			 
			String sql = "update t_customer set del = 1 "
					+ "where id = ?";
			 
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, id);
 
			flag = psmt.execute();
			
			//if (flag == true) { 
				String content = "User  update data in t_customer";
				String sql5 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql5); 
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(1, date);
				psmt.setString(2, content);
				psmt.executeUpdate();
			//}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.ContractDaoImpl.updateById");
		} finally { 
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return true;
	}
	
	/**
	 * 获取日志־
	 * @return
	 * @throws AppException
	 */
	public List<Log> getLogs() throws AppException {
		Log log = null;
		
		List<Log> logs = new ArrayList<Log>();
		 
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try { 
			conn = DBUtil.getConnection(); 
			String sql = "select * from t_log where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery(); 
			while (rs.next()) {
				log = new Log();
				log.setId(rs.getInt("id"));
				log.setUserId(rs.getInt("user_Id"));
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return logs;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public int JudgeUser(String name) throws AppException {
		int status = 0;
		 
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try { 
			conn = DBUtil.getConnection();
            String sql = "select name,status from t_user where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return status;
	}
}
