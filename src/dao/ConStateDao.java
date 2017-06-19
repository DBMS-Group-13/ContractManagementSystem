package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.ConStateDao;
import model.ConState;
import utils.AppException;
import utils.DBUtil;

/**
 * t_conState琛╯ql鎿嶄綔灞�
 */
public class ConStateDao{

	/**
	 * 娣诲姞鍚堝悓鐘舵��
	 *  
	 * @param  conState Contract status object
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(ConState conState) throws AppException{	
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into t_contract_state(con_id,type) values(?,?)";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conState.getConId());
			psmt.setInt(2, conState.getType());
		
            int result = psmt.executeUpdate();
            
            //鍐欐棩蹇�
			if(result > 0){
				flag = true;
				String content = "User insert data into t_contract_state";
				String sql2 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql2); 
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(1, date);
				psmt.setString(2, content);
				psmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConStateDaoImpl.add");
		} finally {
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 鏍规嵁鐘舵�佺被鍨嬭幏鍙栧悎鍚岀紪鍙�
	 * 
	 * @param type  Operation type
	 * @return Contract ids
	 * @throws AppException
	 */
	public List<Integer> getConIdsByType(int type) throws AppException {
		List<Integer> conIds = new ArrayList<Integer>();
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select con_id from t_contract_state where type=? and del=0";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, type);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				conIds.add(rs.getInt("con_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConStateDaoImpl.getConIdsByType");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return conIds;
	}

	/**
	 * 鏍规嵁conId,type鑾峰彇鍚堝悓鐘舵�佷俊鎭�
	 * 
	 * @param conId Contract id
	 * @param type Operation type
	 * @return Contract state object
	 * @throws AppException
	 */
	public ConState getConState(int conId, int type) throws AppException {
		ConState conState = null;

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,con_id,type,time "
					+"from t_contract_state "
					+"where con_id = ? and type = ? and del = 0";

			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, conId); 
			psmt.setInt(2, type);
			
			rs = psmt.executeQuery();

			if(rs.next()) {
				conState = new ConState();
				conState.setId(rs.getInt("id"));
				conState.setConId(rs.getInt("con_id"));
				conState.setType(rs.getInt("type"));
				conState.setTime(rs.getDate("time"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.ConStateDaoImpl.getByConId");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return conState;
	}
	
	/**
	 * 鏍规嵁con_id,type鍒ゆ柇鍚堝悓鐘舵�佹槸鍚﹀瓨鍦�
	 * 
	 * @param con_id Countract id
	 * @param type Operation type
	 * @return boolean Exist return true锛宱therwise return false
	 * @throws AppException
	 */
	public boolean isExist(int con_id, int type) throws AppException {
		boolean flag = false;

		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(id) as n from t_contract_state "
				 +"where con_id = ? and type = ? and del = 0";
				
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, con_id);
			psmt.setInt(2, type);


			rs = psmt.executeQuery();
			rs.next();
			int n = rs.getInt("n"); 
			if (n > 0) {
				flag = true; 
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ConStateDaoImpl.isExist");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}

}
