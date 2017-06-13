package dao;

import java.util.List;

import model.Customer;
import model.Log;
import model.User;
import utils.AppException;

/**
 * User Data Access Layer Interface
 */
public interface UserDao {
	
	/**
	 * Verify whether exist user that  has the same name 
	 * 
	 * @param name  User name
	 * @return Return true if exist user that  has the same name 锛宱therwise false
	 * @throws AppException
	 */
	public boolean isExist(String name) throws AppException;
	
	/**
	 * Save user's information
	 * 
	 * @param user User object
	 * @return Return true if save successfully, otherwise false
	 * @throws AppException
	 */
	public boolean add(User user) throws AppException;
	
	/**
	 * Query  UserId according to user name and password
	 * @param name User name
	 * @param password 
	 * @throws AppException
	 */
	public int login(String name,String password) throws AppException;
	
	/**
	 * Query user's information according to id
	 * 
	 * @param id  User id
	 * @return User 
	 * @throws AppException
	 */
	public User getById(int id) throws AppException;
	
	/**
	 * Query user id set
	 * 
	 * @return User id set
	 * @throws AppException
	 */
	public List<Integer> getIds() throws AppException;
	
	//获取所有用户信息，返回User类型List
	public List<User> getUsers() throws AppException;
	
	//获取所有用户信息，返回User类型List
	public List<Customer> getCustomers() throws AppException;
		
	//根据邮箱地址，返回用户信息
	public User getByEmail(String email) throws AppException;
	
	//修改用户信息
	public boolean UpdateUser(User user) throws AppException;
	
	//给定email，判断是否有重复
	public boolean JudgeEmail(String email) throws AppException;
	
	//添加客户
	public boolean addCustomer(Customer customer) throws AppException;
	
	//禁用账号，Del属性设为1
	public boolean setUserDel(int user_id) throws AppException;
	
	//修改客户信息
	public boolean UpdateCustomer(Customer customer) throws AppException;
	
	//删除顾客，Del属性设为1
	public boolean setCustomerDel(int id) throws AppException;
	
	//获取日志
	public List<Log> getLogs() throws AppException;
}
