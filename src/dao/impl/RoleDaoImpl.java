package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.RoleDao;
import model.Role;
import utils.AppException;
import utils.DBUtil;

/**
 * Role data access layer implementation class
 */
public class RoleDaoImpl implements RoleDao {

	/**
	 * Query role's information according to id
	 * 
	 * @param id 
	 * @return Role 
	 * @throws AppException
	 */
	public Role getById(int id) throws AppException {
		// Declare role object
		Role role = null;
		//Declare Connection object,PreparedStatement object and ResultSet object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement,query role's information based on role id, "?" is a placeholder
			String sql = "select id,name,description,function_ids "
					+"from t_role "
					+"where id = ? and del = 0";
			// Pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder  '?'
			psmt.setInt(1, id);
			// Query result set
			rs = psmt.executeQuery();
			
			// Save user's information by using Pole entity object when queried the results set 
			if (rs.next()) {
				role = new Role(); // Instantiate role object
				// Set values for role object
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
				role.setFuncIds(rs.getString("function_ids"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.RoleDaoImpl.getById");
		} finally {
			// Close the database operation object, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return role;
	}
	
	/**
	 * Query all role object set
	 * 
	 * @return Role object set
	 * @throws AppException
	 */
	public List<Role> getAll() throws AppException {
		// Initialiaze roleList
		List<Role> roleList = new ArrayList<Role>();
		
		//Declare Connection object,PreparedStatement object and ResultSet object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query all role object set,"?" is a placeholder
			String sql = "select id,name,description,function_ids from t_role where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();// Return result set
			// Loop to get information in result set,and save in ids
			while (rs.next()) {
				Role role = new Role(); // Instantiate role object
				// Set value to role
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
				role.setFuncIds(rs.getString("function_ids"));
				
				roleList.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.RoleDaoImpl.getAll");
		} finally {
			// Close the database operation object, release resources
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return roleList;
	}

	//添加角色
	@SuppressWarnings("resource")
	public boolean add(Role role) throws AppException {
		Connection conn = null; // Define database connection object
		PreparedStatement psmt = null;// Define PreparedStatement object
		
		boolean flag = false;// Operation flag
		try {
			conn = DBUtil.getConnection();// Create database connection
			// Declare operation statement,save user information into database, "?" is a placeholder
			String sql = "insert into t_role (name,description,function_ids)"
					+ " values (?,?,?)";
			
			psmt = conn.prepareStatement(sql);// Pre-compiled sql
			// Set values for the placeholder 
			psmt.setString(1, role.getName());
			psmt.setString(2, role.getDescription());
			psmt.setString(3, role.getFuncIds());
			flag = psmt.execute();// Execute the update operation,return the affected rows
			if (flag == true) {
				String content = "User insert data into t_role";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2); // pre-compiled sql
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				// Set values for the placeholder
				psmt.setString(1, date);
				psmt.setString(2, content);
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
	
	//删除角色，Del属性设为1
	@SuppressWarnings("resource")
	public boolean setRoleDel(int role_id) throws AppException {
		boolean flag = false;// Operation flag
		// Declare database connection object, pre-compiled object
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			// Create database connection
			conn = DBUtil.getConnection();
			
			// Declare sql:update contract information according to contract id
			String sql = "update t_role set del = 1 where id = ?";
			// Pre-compiled sql, and set the parameter values
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, role_id);
			// Execute update,return affected rows
			flag = psmt.execute();
			
			String sql2 = "update t_right set del = 1 where role_id = ?";
			// Pre-compiled sql, and set the parameter values
			psmt = conn.prepareStatement(sql2);
			psmt.setInt(1, role_id);
			// Execute update,return affected rows
			flag = psmt.execute();
			
			if (flag == true) {// If affected lines greater than 0, so update success
				String content = "User  update data in t_role,t_right";
				String sql3 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql3); // pre-compiled sql
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
	
	//修改角色信息
	@SuppressWarnings("resource")
	public boolean UpdateRole(Role role) throws AppException {
		boolean flag = false;// Operation flag
		//Declare database connection object, pre-compiled object and result set object
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			
			// Create database connection
			conn = DBUtil.getConnection();
			// Declare operation statement:query user information according to the user id , "?" is a placeholder
			String sql = "update t_role set name = ?, description = ?,funcIds = ?,del = ? "
					+ "where id = ?";
			// pre-compiled sql
			psmt = conn.prepareStatement(sql);
			// Set values for the placeholder
			psmt.setInt(1, role.getId());
			psmt.setString(2, role.getName());
			psmt.setString(3, role.getDescription());
			psmt.setString(4, role.getFuncIds());
			psmt.setInt(5, role.getDel());
			// Query resultSet
			flag = psmt.execute();
			
			// Save user information in Pole entity object when queried out resultSet
			if (flag == true) {
				String content = "User update data in t_role";
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
}
