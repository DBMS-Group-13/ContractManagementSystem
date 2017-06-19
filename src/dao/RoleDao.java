package dao;

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
 * Role鎿嶇旱灞�
 */
public class RoleDao{

	/**
	 * 鏌ヨ瑙掕壊
	 * 
	 * @param id 
	 * @return Role 
	 * @throws AppException
	 */
	public Role getById(int id) throws AppException {
		
		Role role = null;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			
			conn = DBUtil.getConnection();
			
			String sql = "select id,name,description,function_ids "
					+"from t_role "
					+"where id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
		
			psmt.setInt(1, id);
		
			rs = psmt.executeQuery();
			
			
			if (rs.next()) {
				role = new Role(); 
				
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
				role.setFuncIds(rs.getString("function_ids"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.RoleDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return role;
	}
	
	/**
	 * 鏌ヨ鎵�鏈夎鑹�
	 * 
	 * @return Role object set
	 * @throws AppException
	 */
	public List<Role> getAll() throws AppException {
		List<Role> roleList = new ArrayList<Role>();
		
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select id,name,description,function_ids from t_role where del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			while (rs.next()) {
				Role role = new Role(); 
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
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return roleList;
	}

	/**
	 * 娣诲姞瑙掕壊
	 * @param role
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(Role role) throws AppException {
		Connection conn = null; 
		PreparedStatement psmt = null;
		
		boolean flag = false;
		try {
			conn = DBUtil.getConnection();
			
			String sql = "insert into t_role (name,description,function_ids)"
					+ " values (?,?,?)";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, role.getName());
			psmt.setString(2, role.getDescription());
			psmt.setString(3, role.getFuncIds());
			flag = psmt.execute();
			//if (flag == true) {
				String content = "User insert data into t_role";
				String sql2 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2); 
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				
				psmt.setString(1, date);
				psmt.setString(2, content);
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
	 * 鈥滃垹闄も�濊鑹�
	 * @param role_id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean setRoleDel(int role_id) throws AppException {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			
			conn = DBUtil.getConnection();
			
			
			String sql = "update t_role set del = 1 where id = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, role_id);
			
			flag = psmt.execute();
			
			String sql2 = "update t_right set del = 1 where role_id = ?";
			
			psmt = conn.prepareStatement(sql2);
			psmt.setInt(1, role_id);
			
			flag = psmt.execute();
			
			//if (flag == true) {
				String content = "User  update data in t_role,t_right";
				String sql3 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql3);
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
	 * 鏇存柊瑙掕壊
	 * @param role
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean UpdateRole(Role role) throws AppException {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			
			conn = DBUtil.getConnection();
			String sql = "update t_role set name = ?, description = ?,funcIds = ?,del = ? "
					+ "where id = ?";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, role.getId());
			psmt.setString(2, role.getName());
			psmt.setString(3, role.getDescription());
			psmt.setString(4, role.getFuncIds());
			psmt.setInt(5, role.getDel());
			
			flag = psmt.execute();
			
			
			//if (flag == true) {
				String content = "User update data in t_role";
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
}
