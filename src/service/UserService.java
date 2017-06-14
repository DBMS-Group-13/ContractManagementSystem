package service;

import java.util.ArrayList;
import java.util.List;

import dao.RightDao;
import dao.RoleDao;
import dao.UserDao;
import dao.impl.RightDaoImpl;
import dao.impl.RoleDaoImpl;
import dao.impl.UserDaoImpl;
import model.Customer;
import model.PermissionBusiModel;
import model.Right;
import model.Role;
import model.User;
import utils.AppException;

/**
 * User business logic class
 */
public class UserService {

	private UserDao userDao = null;//  Define a userDao interface object
	private RoleDao roleDao = null;// Define a roleDao interface object
	private RightDao rightDao = null;// Define a userDao rightDao object

	/**
	 * No-arg constructor method is used to initialize instance in data access layer
	 */
	public UserService() {
		userDao = new UserDaoImpl();
		roleDao = new RoleDaoImpl();
		rightDao = new RightDaoImpl();
	}

	/**
	 *注册
	 * @param user User object
	 * @return Return true if registration is successful, otherwise return false
	 * @throws AppException
	 */
	public boolean register(User user) throws AppException {
		boolean flag = false;//  Define flag 
		try {
			if (!userDao.isExist(user.getName())) {// Execute save operation when the user does not exist
				flag = userDao.add(user);// Return the operation result back to flag
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.register");
		}
		return flag;
	}
	
	/**
	 * 登录，返回用户id(数据库中t_user表id字段)
	 * 
	 * @param name 
	 * @param password 
	 * @return Query the matched user number according to the condition , otherwise it returns 0
	 * @throws AppException
	 */
	public int login(String name, String password) throws AppException {
		int userId = -1; // Declare userId
		try {
			//Get userId
			userId = userDao.login(name, password); 
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.login");
		}
		// Return userId
		return userId;
	}
	
	/**
	 * 获取用户的角色信息
	 * 
	 * @param userId 
	 * @return Role object
	 * @throws AppException
	 */
	public Role getUserRole(int userId) throws AppException {	
		Role role = null;// Declare role
		int roleId = -1; // Initialize roleId
		try {
			// Get the roleId that corresponding to the user
			roleId = rightDao.getRoleIdByUserId(userId);
			if(roleId > 0){
				// Get role information
				role = roleDao.getById(roleId); 
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.getUserRole");
		}
		return role;
	}
	
	/**
	 * 获取角色为roleID的用户列表
	 * 
	 * @param roleId 
	 * @return User list
	 * @throws AppException
	 */
	public List<User> getUserListByRoleId(int roleId) throws AppException {
		// Initialize  userList
		List<User> userList = new ArrayList<User>();
		// Declare userIds
		List<Integer> userIds = null; 
		
		try {
			/*
			 * 1.Get designated user's userIds
			 */
			userIds = rightDao.getUserIdsByRoleId(roleId);
			
			/*
			 * 2.Get user information list according to userIds
			 */ 
			for (int userId : userIds) {
				// Get user's information
				User user = userDao.getById(userId);
				if (user != null) {
					userList.add(user); 
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.getUserList");
		}	
		// Return userList
		return userList;
	}
	
	/**
	 * 获取用户-角色 匹配列表
	 * 
	 * @return permissionList  User permission list
	 * @throws AppException
	 */
	public List<PermissionBusiModel> getYhqxList() throws AppException {
		// Initialize permissionList
		List<PermissionBusiModel> permissionList = new ArrayList<PermissionBusiModel>();
		// Declare userIds
		List<Integer> userIds = null; 
		
		try {
			/*
			 * 1.Get user id set
			 */
			userIds = userDao.getIds();
			
			/*
			 * 2.Get user permission information: user information and corresponding role information
			 */
			for (int userId : userIds) {
			
				// Initialize business entity class object
				PermissionBusiModel permission = new PermissionBusiModel();
				
				User user = userDao.getById(userId); // Get user information according to user id
				int roleId = -1;
				roleId = rightDao.getRoleIdByUserId(userId); // Get role id according to user id
				
				if (roleId > 0) {
					Role role = roleDao.getById(roleId); // Get role information according to role id
					// Save role information to permission
					permission.setRoleId(roleId);
					permission.setRoleName(role.getName());
				}
				
				// Save user information to permission
				permission.setUserId(userId);
				permission.setUserName(user.getName());
				
				permissionList.add(permission);
			}
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.getYhqxList");
		}	
		// Permission business entity set
		return permissionList;
	}
	
	/**
	 * 获取角色列表
	 * 
	 * @return Role object set
	 * @throws AppException
	 */
	public List<Role> getRoleList() throws AppException {	
		// Initialize role set
		List<Role> roleList = new ArrayList<Role>();
		
		try {
			// Get all role object set
			roleList = roleDao.getAll();
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.getRoleList");
		}
		return roleList;
	}
	
	/**
	 * 配置权限
	 *  
	 * @param right Permission object
	 * @return boolean Return true if operation successful,otherwise return false
	 * @throws AppException
	 */
	public boolean assignPermission(Right right) throws AppException {
		boolean flag = false;// Define flag
		
		try {
			//  Get user's role 
			int roleId = -1; // Initialize roleId
			// Get user's role id
			roleId = rightDao.getRoleIdByUserId(right.getUserId());
			// Declare role object
			Role role = null;
			if (roleId > 0) {
				// Get role information
				role = roleDao.getById(roleId);
			}
		
			/*
			 * Judgement role of user owned before,if user has a role before,so change the role,otherwise add a new role
			 */
			if (role != null) {
				// Get user's permission
				int rightId = rightDao.getIdByUserId(right.getUserId());
				// Set permission id to right object
				right.setId(rightId);
				right.setDescription("update");
				// Update permission inforx	mation
				flag = rightDao.updateById(right);
			} else {
				flag = rightDao.add(right);
			}
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.UserService.assignPermission");
		}
		return flag;
	}
	
	public List<User> getUsers() throws AppException{
		return userDao.getUsers();
	}
	
	public boolean isEmailExist(String email) throws AppException
	{
		return userDao.JudgeEmail(email);
	}
	
	public User loadByEmail(String email) throws AppException
	{
		return userDao.getByEmail(email);
	}
	
	public void updateUser(User user) throws AppException
	{
		userDao.UpdateUser(user);
	}
	
	public List<Customer> getCustomers() throws AppException{
		return userDao.getCustomers();
	}
	
	public boolean deleteUser(int userId) throws AppException{
		if(userDao.setUserDel(userId))
			return true;
		else {
			return false;
		}
	}
	
	public boolean add(Customer customer) throws AppException{
		userDao.addCustomer(customer);
		return true;
	}
	
	public boolean update(Customer customer) throws AppException {
		if(userDao.UpdateCustomer(customer))
			return true;
		else {
			return false;
		}
	}
	
	public boolean deleteCustomer(int customerId) throws AppException
	{
		if(userDao.setCustomerDel(customerId))
			return true;
		else {
			return false;
		}
	}

}
