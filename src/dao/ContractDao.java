package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.ContractDao;
import model.Contract;
import utils.AppException;
import utils.DBUtil;

/**
 * Contract data access layer implementation class
 */
public class ContractDao{

	/**
	 * 娣诲姞鍚堝悓淇℃伅
	 * 
	 * @param contract 
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean add(Contract contract) throws AppException{
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "insert into t_contract" 
				+"(user_id,customer,num,name,beginTime,endTime,content) "
				+"values(?,?,?,?,?,?,?)";
				
            psmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); 

			psmt.setInt(1, contract.getUserId());
			psmt.setString(2, contract.getCustomer());
			psmt.setString(3, contract.getNum());
			psmt.setString(4, contract.getName());
			
			java.sql.Date beginTime = new java.sql.Date(contract.getBeginTime().getTime());
			java.sql.Date endTime = new java.sql.Date(contract.getEndTime().getTime());
			psmt.setDate(5, beginTime);
			psmt.setDate(6, endTime);
			psmt.setString(7, contract.getContent());
			
			psmt.executeUpdate(); 
			rs = psmt.getGeneratedKeys();  
			
			if (rs.next()) {
				contract.setId(rs.getInt(1));
				flag = true; 
				String content = "User" + contract.getUserId() + "insert data into t_contract";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); 
				
				psmt.setInt(1, contract.getUserId());
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				psmt.setString(2, date);
				psmt.setString(3, content);
				psmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
			"dao.impl.ContractDaoImpl.add");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 閫氳繃id鑾峰彇鍚堝悓
	 * 
	 * @param id Contract id
	 * @return Contract object
	 * @throws AppException
	 */
	public Contract getById(int id) throws AppException {
		Contract contract = null;
		
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,name,user_id,customer,num,beginTime,endTime,content "
					+"from t_contract "
					+"where id = ?";

			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, id); 
			

			rs = psmt.executeQuery();

			
			if(rs.next()) {
				contract = new Contract();
				contract.setId(rs.getInt("id"));
				contract.setName(rs.getString("name"));
				contract.setUserId(rs.getInt("user_id"));
				contract.setCustomer(rs.getString("customer"));
				contract.setNum(rs.getString("num"));
				contract.setBeginTime(rs.getDate("beginTime"));
				contract.setEndTime(rs.getDate("endTime"));
				contract.setContent(rs.getString("content"));	
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.ContractDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return contract;
	}
	
	/**
	 *閫氳繃鐢ㄦ埛id鑾峰彇鎵�鏈夊悎鍚宨d
	 * 
	 * @param id Contract id
	 * @return Contract id set
	 * @throws AppException
	 */
	public List<Integer> getIdsByUserId(int userId) throws AppException {
		List<Integer> ids = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select id "
					+"from t_contract "
					+"where user_id = ? and del = 0";
			
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, userId);
			
			rs = psmt.executeQuery();
			
			
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException("dao.impl.ContractDaoImpl.getIdsByUserId");
		} finally {
			
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return ids;
	}
	
	/**
	 * 鏇存柊鍚堝悓
	 * 
	 * @param conId Contract id
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean updateById(Contract contract) throws AppException {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBUtil.getConnection();
			
			String sql = "update t_contract set name = ?, customer = ?, beginTime = ?, endTime = ?, content = ? " 
					+"where id = ? and del = 0";

			
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, contract.getName());
			psmt.setString(2, contract.getCustomer());
			
			java.sql.Date beginTime = new java.sql.Date(contract.getBeginTime().getTime());
			java.sql.Date endTime = new java.sql.Date(contract.getEndTime().getTime());
			psmt.setDate(3, beginTime);
			psmt.setDate(4, endTime);
			psmt.setString(5, contract.getContent());
			psmt.setInt(6, contract.getId());

		
			flag = psmt.execute();
			
			//鍐欐棩蹇�
			//if (flag == true) {
				String content = "User" + contract.getUserId() + "update data in t_contract";
				String sql2 = "insert into t_log(user_id,time,content)values(?,?,?)";
				psmt = conn.prepareStatement(sql2); 
				
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");   
				String date = sDateFormat.format(new java.util.Date());  
				
				psmt.setInt(1, contract.getUserId());
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
	 * 鑾峰彇鎵�鏈夊悎鍚宨d
	 * @return
	 * @throws AppException
	 */
	public List<Integer> getIds() throws AppException {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<Integer> List = new ArrayList<Integer>();
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select id from t_contract where del = 0";

			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();

			while(rs.next()) {
				List.add(rs.getInt("id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(
					"dao.impl.ContractDaoImpl.getById");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closeStatement(psmt);
			DBUtil.closeConnection(conn);
		}
		return List;
	}
	
	/**
	 * "鍒犻櫎"鍚堝悓
	 * @param con_id
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("resource")
	public boolean setDel(int con_id) throws AppException {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBUtil.getConnection();
			
			String sql = "update t_contract set del = 1 "
					+ "where id = ?";
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, con_id);
			flag =psmt.execute();
			
			String sql3 = "update t_contract_process set del = 1 "
					+ "where id = ?";
			
			psmt = conn.prepareStatement(sql3);
			psmt.setInt(1, con_id);
			flag =psmt.execute();
			String sql4 = "update t_contract_state set del = 1 "
					+ "where id = ?";
			
			psmt = conn.prepareStatement(sql4);
			psmt.setInt(1, con_id);

			
			flag = psmt.execute();
			
			//if (flag == true) {
				String content = "User update data in t_contract,t_contract_attachment,"
						+ "t_contract_process,t_contract_state";
				String sql5 = "insert into t_log(time,content)values(?,?)";
				psmt = conn.prepareStatement(sql5); // pre-compiled sql
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
}
