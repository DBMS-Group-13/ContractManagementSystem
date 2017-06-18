package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.RightDao;
import model.Right;
import utils.AppException;
import utils.DBUtil;

/**
 * right操纵层
 */
public class RightDao{

	/**
	 * 通过合同id获取角色id
	 * 
	 * @param userId 
	 * @return roleId 
	 * @throws AppException
	 */
	public int getRoleIdByUserId(int userId) throws AppException {
		int roleId = -1; 
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select role_id "
					+"from t_right "
					+"where user_id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, userId);
			
			rs = psmt.executeQuery();
			
			
			if (rs.next()) {
				roleId = rs.getInt("role_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.RightDaoImpl.getRoleIdByUserId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return roleId;
	}
	
	/**
	 * 根据角色id获取所有拥有该角色的用户id
	 * 
	 * @param roleId Role id
	 * @return Query user id that meet the meet the conditions,otherwise return null
	 * @throws AppException
	 */
	public List<Integer> getUserIdsByRoleId(int roleId) throws AppException  {
		
		List<Integer> userIds = new ArrayList<Integer>();
		
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select user_id from t_right where role_id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, roleId);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				userIds.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.RightDaoImpl.getUserIdsByRoleId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return userIds;
	}
	
	/**
	 * 根据用户id获取权限id
	 * 
	 * @param userId User id
	 * @return id Permission id
	 * @throws AppException
	 */
	public int getIdByUserId(int userId) throws AppException {
		int id = -1; 
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select id "
					+"from t_right "
					+"where user_id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, userId);
			
			rs = psmt.executeQuery();
			
			
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.RightDaoImpl.getIdByUserId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return id;
	}
	
	/**
	 * 权限更新
	 * pass parameter though entity object
	 * 
	 * @param id permission id
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean updateById(Right right) throws AppException {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			
			conn = DBUtil.getConnection();
			
			String sql = "update t_right set user_id = ?, role_id = ?, description = ? " 
					+"where id = ? and del = 0";

			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, right.getUserId());
			psmt.setInt(2, right.getRoleId());
			psmt.setString(3, right.getDescription());
			psmt.setInt(4, right.getId());
			
			
			int count = psmt.executeUpdate();
			
			if (count > 0) {
				flag = true;
				String content = "User" + right.getUserId() + "update data in t_contract";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); // 
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				
				psmt.setInt(1, right.getUserId());
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.RightDaoImpl.updateById");
		} finally {
			
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 添加权限
	 * 
	 * @param right permission object
	 * @return Return true if successful , otherwise false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(Right right) throws AppException {
		boolean flag = false;
		
		Connection conn = null; 
		PreparedStatement psmt = null;
		
		int result = -1;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into t_right (user_id,role_id,description)"
					+ " values (?,?,?)";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, right.getUserId());
			psmt.setInt(2, right.getRoleId());
			psmt.setString(3, right.getDescription());

			result = psmt.executeUpdate();
			if (result > 0) {
				flag = true;
				String content = "User" + right.getUserId() + "insert data into t_contract";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2);
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				
				psmt.setInt(1, right.getUserId());
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.RightDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}

}
