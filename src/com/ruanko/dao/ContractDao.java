package com.ruanko.dao;

import java.util.List;

import com.ruanko.model.Contract;
import com.ruanko.utils.AppException;

/**
 *	Contract Data Access Layer Interface
 */
public interface ContractDao {

	/**
	 * Add contract information
	 * 
	 * @param contract  Contract object
	 * @return boolean  Return true if successful , otherwise false
	 * @throws AppException
	 */
	public boolean add(Contract contract) throws AppException;
	
	/**
	 * Query contract information according to contract id
	 * 
	 * @param id Contract id
	 * @return Contract object
	 * @throws AppException
	 */
	public Contract getById(int id) throws AppException;
	
	/**
	 * Query contract id set according to user is
	 * 
	 * @param id User id
	 * @return Contract id set
	 * @throws AppException
	 */
	public List<Integer> getIdsByUserId(int userId) throws AppException;
	
	/**
	 * Update contract's content according to contract id,passing parameters through entity object 
	 * 
	 * @param conId Contract id
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	public boolean updateById(Contract contract) throws AppException;
}
