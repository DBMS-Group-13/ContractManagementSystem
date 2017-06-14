package dao;

import java.util.List;

import model.Role;
import utils.AppException;

/**
 * Role Data Access Layer Interface
 */
public interface RoleDao {

	/**
	 * Query role's information according to id
	 * 
	 * @param id RoleId
	 * @return Role 
	 * @throws AppException
	 */
	public Role getById(int id) throws AppException;
	
	/**
	 * Query all role object set
	 * 
	 * @return Role object set
	 * @throws AppException
	 */
	public List<Role> getAll() throws AppException;
	
	//添加角色
	public boolean add(Role role) throws AppException;
	
	//删除角色，Del属性设为1
	public boolean setRoleDel(int role_id) throws AppException;
	
	//修改角色信息
	public boolean UpdateRole(Role role) throws AppException;
	
}
