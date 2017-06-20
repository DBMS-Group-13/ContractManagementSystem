package service;

import java.util.ArrayList;
import java.util.List;

import dao.RightDao;
import dao.RightDao;
import dao.RoleDao;
import dao.RoleDao;
import dao.UserDao;
import model.Customer;
import model.PermissionBusiModel;
import model.PermissionDetailModel;
import model.Right;
import model.Role;
import model.User;
import utils.AppException;

/**
 * 用户服务层
 */
public class UserService {

	private UserDao userDao = null; 
	private RoleDao roleDao = null; 
	private RightDao rightDao = null;

	/**
	 * 无参构造函数
	 */
	public UserService() {
		userDao = new UserDao();
		roleDao = new RoleDao();
		rightDao = new RightDao();
	}

	/**
	 *注册
	 * @param user User object
	 * @return Return true if registration is successful, otherwise return false
	 * @throws AppException
	 */
	public boolean register(User user) throws AppException {
		boolean flag = false;
		try {
			if (!userDao.isExist(user.getName())) {
				flag = userDao.add(user);
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
		int userId = -1; 
		try {
			userId = userDao.login(name, password); 
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("com.ruanko.service.UserService.login");
		}
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
		Role role = null;
		int roleId = -1;
		try {
			
			roleId = rightDao.getRoleIdByUserId(userId);
			if(roleId > 0){
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
		List<User> userList = new ArrayList<User>();
		List<Integer> userIds = null; 
		
		try {
			/*
			 * 1.权限表中获取拥有特定角色的用户ID
			 */
			userIds = rightDao.getUserIdsByRoleId(roleId);
			
			/*
			 * 2.获取用户信息
			 */ 
			for (int userId : userIds) {
				User user = userDao.getById(userId);
				if (user != null) {
					userList.add(user); 
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.getUserList");
		}
		return userList;
	}
	
	/**
	 * 获取用户-角色 匹配列表
	 * 
	 * @return permissionList  User permission list
	 * @throws AppException
	 */
	public List<PermissionBusiModel> getYhqxList() throws AppException {
		List<PermissionBusiModel> permissionList = new ArrayList<PermissionBusiModel>();
		List<Integer> userIds = null; 
		
		try {
			/*
			 * 1.获取所有用户id
			 */
			userIds = userDao.getIds();
			
			/*
			 * 2.获取权限信息
			 */
			for (int userId : userIds) {
			
				PermissionBusiModel permission = new PermissionBusiModel();
				
				User user = userDao.getById(userId); 
				int roleId = -1;
				roleId = rightDao.getRoleIdByUserId(userId); 
				
				if (roleId > 0) {
					Role role = roleDao.getById(roleId); 
					permission.setRoleId(roleId);
					permission.setRoleName(role.getName());
					permission.setfuncIds(role.getFuncIds());
				}
				
				permission.setUserId(userId);
				permission.setUserName(user.getName());
				
				permissionList.add(permission);
			}
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.UserService.getYhqxList");
		}	
		return permissionList;
	}
	
	/**
	 * 获取角色列表
	 * 
	 * @return Role object set
	 * @throws AppException
	 */
	public List<Role> getRoleList() throws AppException {	
		List<Role> roleList = new ArrayList<Role>();
		
		try {
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
		boolean flag = false;
		
		try {
			int roleId = -1; 
			
			roleId = rightDao.getRoleIdByUserId(right.getUserId());
			
			Role role = null;
			if (roleId > 0) {
				role = roleDao.getById(roleId);
			}
		
			
			if (role != null) {
				int rightId = rightDao.getIdByUserId(right.getUserId());
				
				right.setId(rightId);
				right.setDescription("update");
				
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
	
	/**
	 * 获取所有用户信息
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public List<User> getUsers() throws AppException{
		return userDao.getUsers();
	}
	
	/**
	 * 判断邮箱是否重复
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public boolean isEmailExist(String email) throws AppException
	{
		return userDao.JudgeEmail(email);
	}
	
	/**
	 * 通过邮箱获取用户信息
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public User loadByEmail(String email) throws AppException
	{
		return userDao.getByEmail(email);
	}
	
	/**
	 * 更新用户
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public void updateUser(User user) throws AppException
	{
		userDao.UpdateUser(user);
	}
	
	/**
	 * 获取所有客户
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public List<Customer> getCustomers() throws AppException{
		return userDao.getCustomers();
	}
	
	/**
	 * 删除用户
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public boolean deleteUser(int userId) throws AppException{
		if(userDao.setUserDel(userId))
			return true;
		else {
			return false;
		}
	}
	
	/**
	 * 添加客户
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public boolean add(Customer customer) throws AppException{
		userDao.addCustomer(customer);
		return true;
	}
	
	/**
	 * 更新客户信息
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public boolean update(Customer customer) throws AppException {
		if(userDao.UpdateCustomer(customer))
			return true;
		else {
			return false;
		}
	}
	
	/**
	 * 删除客户
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public boolean deleteCustomer(int customerId) throws AppException
	{
		if(userDao.setCustomerDel(customerId))
			return true;
		else {
			return false;
		}
	}
	
	/**
	 * 判断用户是否存在
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public boolean isExistUser(String name)  throws AppException{
		return userDao.isExist(name);
	}
	
	/**
	 * 判断用户是否激活账户
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public int isActivateUser(String name) throws AppException{
		return userDao.JudgeUser(name);
	}
	
	/**
	 * 删除角色
	 * @param role
	 * @return
	 * @throws AppException
	 */
	public boolean deleteRole(int roleId) throws AppException{
		return roleDao.setRoleDel(roleId);
	}
	
	/**
	 * 添加角色
	 * @param role
	 * @return
	 * @throws AppException
	 */
	public boolean addRole(Role role) throws AppException{
		return roleDao.add(role);
	}
	
	/**
	 * 更新角色
	 * @param role
	 * @return
	 * @throws AppException
	 */
	public boolean updateRole(Role role) throws AppException{
		return roleDao.UpdateRole(role);
	}
	
	/**
	 * 获取用户具体权限细节
	 * @param userId
	 * @return
	 */
	public PermissionDetailModel getPermissionDetail(int userId)
	{
		PermissionDetailModel permissionDetailModel=new PermissionDetailModel();
		try {
			int role_id=rightDao.getRoleIdByUserId(userId);
			Role role=roleDao.getById(role_id);
			permissionDetailModel=getPBMByRole(role);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return permissionDetailModel;
	}

	/**
	 * 获取会签员名单
	 * @return
	 */
	public List<User> getCsigners()
	{
		try {
			List<Integer> userIds=new ArrayList<Integer>();
			List<Role> roleList=roleDao.getAll();
			for(Role role:roleList)
			{
				PermissionDetailModel permissionDetailModel=getPBMByRole(role);
				if(permissionDetailModel.getCsign())
					userIds.addAll(rightDao.getUserIdsByRoleId(role.getId()));
			}
			
			for ( int i = 0 ; i < userIds.size() - 1 ; i ++ ) 
			{   //从左向右循环  
			     for ( int j = userIds.size() - 1 ; j > i; j -- ) 
			     {  //从右往左内循环  
			    	 if (userIds.get(j).equals(userIds.get(i))) {  
			    		 userIds.remove(j);                              //相等则移除  
			    	 }   
			     }   
			}   
			
			List<User> users=new ArrayList<User>();
			for(int userId:userIds)
			{
				users.add(userDao.getById(userId));
			}
			
			return users;
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取签订员名单
	 * @return
	 */
	public List<User> getSigners()
	{
		try {
			List<Integer> userIds=new ArrayList<Integer>();
			List<Role> roleList=roleDao.getAll();
			for(Role role:roleList)
			{
				PermissionDetailModel permissionDetailModel=getPBMByRole(role);
				if(permissionDetailModel.getSign())
					userIds.addAll(rightDao.getUserIdsByRoleId(role.getId()));
			}
			
			for ( int i = 0 ; i < userIds.size() - 1 ; i ++ ) 
			{   //从左向右循环  
			     for ( int j = userIds.size() - 1 ; j > i; j -- ) 
			     {  //从右往左内循环  
			    	 if (userIds.get(j).equals(userIds.get(i))) {  
			    		 userIds.remove(j);                              //相等则移除  
			    	 }   
			     }   
			}   
			
			List<User> users=new ArrayList<User>();
			for(int userId:userIds)
			{
				users.add(userDao.getById(userId));
			}
			
			return users;
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取审批员名单
	 * @return
	 */
	public List<User> getApprovers()
	{
		try {
			List<Integer> userIds=new ArrayList<Integer>();
			List<Role> roleList=roleDao.getAll();
			for(Role role:roleList)
			{
				PermissionDetailModel permissionDetailModel=getPBMByRole(role);
				if(permissionDetailModel.getApprove())
					userIds.addAll(rightDao.getUserIdsByRoleId(role.getId()));
			}
			
			for ( int i = 0 ; i < userIds.size() - 1 ; i ++ ) 
			{   //从左向右循环  
			     for ( int j = userIds.size() - 1 ; j > i; j -- ) 
			     {  //从右往左内循环  
			    	 if (userIds.get(j).equals(userIds.get(i))) {  
			    		 userIds.remove(j);                              //相等则移除  
			    	 }   
			     }   
			}   
			
			List<User> users=new ArrayList<User>();
			for(int userId:userIds)
			{
				users.add(userDao.getById(userId));
			}
			
			return users;
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取角色的权限细节
	 * @param role
	 * @return
	 */
	public PermissionDetailModel getPBMByRole(Role role)
	{
		PermissionDetailModel permissionDetailModel=new PermissionDetailModel();
		String functions_ids=role.getFuncIds();
		String[] split=functions_ids.split(",");
		for(String function:split)
		{
			int function_id=Integer.parseInt(function);
			switch(function_id)
			{
			case 1:
				permissionDetailModel.setSystemMge(true);
				break;
			case 2:
				permissionDetailModel.setContractMge(true);
				break;
			case 3:
				permissionDetailModel.setDraft(true);
				break;
			case 4:
				permissionDetailModel.setCsign(true);
				break;
			case 5:
				permissionDetailModel.setApprove(true);
				break;
			case 6:
				permissionDetailModel.setSign(true);
			default:
				break;
			}
			
		}
		return permissionDetailModel;
	}	
	public User getUserById(int id) throws AppException{
		return userDao.getById(id);
	}
}
