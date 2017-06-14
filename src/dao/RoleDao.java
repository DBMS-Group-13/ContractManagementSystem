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
	
	//��ӽ�ɫ
	public boolean add(Role role) throws AppException;
	
	//ɾ����ɫ��Del������Ϊ1
	public boolean setRoleDel(int role_id) throws AppException;
	
	//�޸Ľ�ɫ��Ϣ
	public boolean UpdateRole(Role role) throws AppException;
	
}
