package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.ConProcessDao;
import model.ConProcess;
import utils.AppException;
import utils.DBUtil;

/**
 * t_conProcess表sql操作层
 */
public class ConProcessDao{
	
	/**
	 * 判断conprocess表中是否存在该合同
	 * 若存在则说明该合同已分配流程
	 * 
	 * @param conId Contract id
	 * @return boolean Return true if exist,otherwise return false
	 * @throws AppException
	 */
	public boolean isExist(int conId) throws AppException{
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select count(id) as n from t_contract_process where con_id = ? and del = 0";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conId);
			rs = psmt.executeQuery();
			
			rs.next();
			int n =  rs.getInt("n");
			if (n > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"com.ruanko.dao.impl.ConProcessDaoImpl.isExist");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 添加合同流程
	 * 
	 * @param  conProcess Contract process object
	 * @return boolean Return true if exist,otherwise return false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(ConProcess conProcess)  throws AppException{	
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "insert into t_contract_process(con_id,user_id,type,state,content) values(?,?,?,?,?)";
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conProcess.getConId());
			psmt.setInt(2, conProcess.getUserId());
			psmt.setInt(3, conProcess.getType());
			psmt.setInt(4, conProcess.getState());
			psmt.setString(5, conProcess.getContent());
		
            int result = psmt.executeUpdate();
			
            //成功则写日志
			if(result > 0){
				flag = true;
				String content = "User" + conProcess.getUserId() + "insert data into t_contract_process";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2);
				psmt.setInt(1, conProcess.getUserId());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConProcessDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 获取用户id为userId，流程类型为type，流程状态为state的所有合同id
	 * 
	 * @param Contract process object
	 * @return Contract id set
	 * @throws AppException
	 */
	public List<Integer> getConIds(int userId,int type,int state) throws AppException {
		List<Integer> conIds = new ArrayList<Integer>();
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select con_id from t_contract_process " +
					"where user_id= ? and type = ? and state = ? and del=0";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, userId);
			psmt.setInt(2, type);
			psmt.setInt(3, state);
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				conIds.add(rs.getInt("con_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConProcessDaoImpl.getConIds");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return conIds;
	}
	
	/**
	 * 更新conProcess
	 * 
	 * @param  userId User id
	 * @param  conId Contract id
	 * @param  type Operation type
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean update(ConProcess conProcess) throws AppException {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update t_contract_process set state = ?, content = ?, time = ? " 
					+"where user_id = ? and con_id = ? and type = ?";
 
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conProcess.getState());
			psmt.setString(2, conProcess.getContent());
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.sql.Timestamp time = new java.sql.Timestamp(conProcess.getTime().getTime());
			df.format(time);
			psmt.setTimestamp(3, time);
			psmt.setInt(4, conProcess.getUserId());
			psmt.setInt(5, conProcess.getConId());
			psmt.setInt(6, conProcess.getType());
			psmt.executeUpdate();
			
			//记录日志
			//if (flag == true) {
				String content = "User" + conProcess.getUserId() + "update data in t_contract_process";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); 

				psmt.setInt(1, conProcess.getUserId());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			//}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.ConProcessDaoImpl.update");
		} finally { 
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return true;
	}
	
	/**
	 * 获取某流程的完成或未完成或否定人数
	 * 
	 * @param con_id Contract id
	 * @param type Operation type
	 * @param state State corresponding to the operation type
	 * @return Total number of eligible records
	 * @throws AppException
	 */
	public int getTotalCount(ConProcess conProcess) throws AppException{
		int totalCount = 0; 
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(id) as n from t_contract_process "
				 +"where con_id = ? and type = ? and state = ?";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conProcess.getConId());
			psmt.setInt(2, conProcess.getType());
			psmt.setInt(3, conProcess.getState());
			rs = psmt.executeQuery();
			rs.next();
			totalCount =  rs.getInt("n");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConProcessDaoImpl.getTotalCount");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return totalCount;
	}
	
	/**
	 * 获取合同ID为conId，流程类型为type，流程状态为state的所有流程id
	 * 
	 * @param conId Contract id
	 * @param type Operation type
	 * @param state Operation state that corresponding operation type
	 * @return Contract process id set
	 * @throws AppException
	 */
	public List<Integer> getIds(int conId, int type, int state) throws AppException {
		List<Integer> ids = new ArrayList<Integer>();
		
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id from t_contract_process " +
					"where con_id= ? and type = ? and state = ? and del=0";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conId);
			psmt.setInt(2, type);
			psmt.setInt(3, state);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConProcessDaoImpl.getIds");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return ids;
	}
	
	/**
	 * 通过id获取流程信息
	 * 
	 * @param id Contract id
	 * @return  Contract process object
	 * @throws AppException
	 */
	public ConProcess getById(int id) throws AppException {
		ConProcess conProcess = null;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,con_id,user_id,type,state,content,time "
					+"from t_contract_process "
					+"where id = ? and del = 0";

			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, id); 
			rs = psmt.executeQuery();

			if(rs.next()) {
				conProcess = new ConProcess();
				conProcess.setId(rs.getInt("id"));
				conProcess.setConId(rs.getInt("con_id"));
				conProcess.setUserId(rs.getInt("user_id"));
				conProcess.setType(rs.getInt("type"));
				conProcess.setState(rs.getInt("state"));
				conProcess.setContent(rs.getString("content"));
				conProcess.setTime(rs.getTimestamp("time"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.ConProcessDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return conProcess;
	}
}
