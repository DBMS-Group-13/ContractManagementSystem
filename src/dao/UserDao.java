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
	 * @return Return true if exist user that  has the same name ，otherwise false
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
	
	//��ȡ�����û���Ϣ������User����List
	public List<User> getUsers() throws AppException;
	
	//��ȡ�����û���Ϣ������User����List
	public List<Customer> getCustomers() throws AppException;
		
	//���������ַ�������û���Ϣ
	public User getByEmail(String email) throws AppException;
	
	//�޸��û���Ϣ
	public boolean UpdateUser(User user) throws AppException;
	
	//����email���ж��Ƿ����ظ�
	public boolean JudgeEmail(String email) throws AppException;
	
	//��ӿͻ�
	public boolean addCustomer(Customer customer) throws AppException;
	
	//�����˺ţ�Del������Ϊ1
	public boolean setUserDel(int user_id) throws AppException;
	
	//�޸Ŀͻ���Ϣ
	public boolean UpdateCustomer(Customer customer) throws AppException;
	
	//ɾ���˿ͣ�Del������Ϊ1
	public boolean setCustomerDel(int id) throws AppException;
	
	//��ȡ��־
	public List<Log> getLogs() throws AppException;
}
