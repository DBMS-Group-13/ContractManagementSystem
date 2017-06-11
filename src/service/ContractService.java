package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import dao.ConProcessDao;
import dao.ConStateDao;
import dao.ContractDao;
import dao.UserDao;
import dao.impl.ConProcessDaoImpl;
import dao.impl.ConStateDaoImpl;
import dao.impl.ContractDaoImpl;
import dao.impl.UserDaoImpl;
import model.CSignatureOpinion;
import model.ConBusiModel;
import model.ConDetailBusiModel;
import model.ConProcess;
import model.ConState;
import model.Contract;
import model.User;
import utils.AppException;
import utils.Constant;

/**
 *	Contract business logic class
 */
public class ContractService {
	
	private ContractDao contractDao = null;// Define a contractDao interface object
	private ConStateDao conStateDao = null;// Define a conStateDao interface object
	private ConProcessDao conProcessDao = null;// Define a conProcessDao interface object
	private UserDao userDao = null;//Define a userDao interface object

	/**
	 * No-arg constructor method is used to initialize instance in data access layer
	 */
	public ContractService() {
		contractDao = new ContractDaoImpl();
		conStateDao = new ConStateDaoImpl();
		conProcessDao = new ConProcessDaoImpl();
		userDao = new UserDaoImpl();
	}
	
	/**
	 * ��ݣ�Draft����ͬ
	 * 
	 * @param contract 
	 * @return boolean  Return true if successful , otherwise false
	 * @throws AppException
	 */
	public boolean draft(Contract contract) throws AppException {
		boolean flag = false;// Define flag
		
		/*
		 * 1.����generateConNum()�������ɺ�ͬ��ţ�contract number��,���䵼�뵽contract������
		 */ 
		contract.setNum(generateConNum());
		
		try {
			/*
			 * 2.���contract�ɹ����������ݿ���, ���ú�ͬ��״̬��Ϣ��draft contract state�����������ݿ���
			 * ע�������ͬ��Ϣ��ͬʱ����º�ͬid������ɼ�contractDao.add���������е�psmt.getGeneratedKeys()��䣩
			 */
			if (contractDao.add(contract)) {
				// Instantiate conState object
				ConState conState = new ConState();
				conState.setConId(contract.getId());  // Get contract ID, and set it into conState object
				// Set type of contract status to "STATE_DRAFTED"
				conState.setType(Constant.STATE_DRAFTED);
				// Save contract status information, the operating result is assigned to flag
				flag = conStateDao.add(conState);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.draft");
		}
		return flag;
	}
	
	/**
	 * ��ȡ����δ������ĺ�ͬ��Ϣ
	 * 
	 * @return Query all contracts that need to be allocated; Otherwise return null
	 * @throws AppException
	 */
	public List<ConBusiModel> getDfphtList() throws AppException {
		// Initialize contractList
		List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
	
		try {
			/*
			 * 1.�ӡ�t_contract�����л�ȡ״̬Ϊ����ݡ��ĺ�ͬid���� 
			 */
			List<Integer> conIds = conStateDao.getConIdsByType(Constant.STATE_DRAFTED);
			

			/* 2.������ͬid��, �ж����Ƿ��ڡ�t_contract_process"��������ؼ�¼,
			 * If have records, means the contract has been allocated, otherwise, means have not been allocated
			 */
			for (int conId : conIds) {
				
				/* 
				 * 3.�����ͬû�б����䣬����Ҫ������ĺ�ͬ��Ϣ���浽��contract business entity��
				 * ���Ұ�ʵ�����conList�б���
				 */
				if (!conProcessDao.isExist(conId)) {
					// Get information of designated contract
					Contract contract = contractDao.getById(conId);
					// Get status of designated contract
					ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
					// Instantiate  conBusiModel object
					ConBusiModel conBusiModel = new ConBusiModel();
					if (contract != null) {
						//Set contract id and name to conBusiModel object
						conBusiModel.setConId(contract.getId());
						conBusiModel.setConName(contract.getName());
					}
					if (conState != null) {
						//Set drafting time to conBusiModel object
						conBusiModel.setDrafTime(conState.getTime()); 
					}
					contractList.add(conBusiModel); // Add conBusiModel to contractList
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getDfphtList");
		}
		// Return contractList
		return contractList;
	}
	
	/**
	 * ͨ��id��ȡcontractʵ����Ϣ
	 * 
	 * @param id 
	 * @return contract entity
	 * @throws AppException
	 */
	public Contract getContract(int id) throws AppException {
		// Declare contract
		Contract contract = null;
		
		try {
			// Get designated contract's information 
			contract = contractDao.getById(id);
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getContract");
		}
		return contract;
	}
	
	/**
	 * �����ͬ
	 * 
	 * @param conId 
	 * @param userIds 
	 * @param type 
	 * @return boolean Return true if successful , otherwise false
	 * @throws AppException
	 */
	public boolean distribute(int conId, int userId, int type)
			throws AppException {
		boolean flag = false;// Define flag
		try {
			ConProcess conProcess = new ConProcess();
			// Assign value to contract process object
			conProcess.setConId(conId);
			conProcess.setType(type);
			// Set status to "UNDONE"
			conProcess.setState(Constant.UNDONE);
			conProcess.setUserId(userId);
			// Save contract information,return operation result to flag
			flag = conProcessDao.add(conProcess);
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.distribute");
		}
		return flag;
	}
	
	/**
	 * ͨ����ǩ��id��ȡ����δ��ǩ�ĺ�ͬ��Ϣ
	 * 
	 * @param userId User id
	 * @return Query all contracts that to be countersigned
	 * @throws AppException
	 */
	public List<ConBusiModel> getDhqhtList(int userId) throws AppException {
		// Initialize  conList
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_CSIGN);
		// Set corresponding state of "PROCESS_CSIGN" type  is "UNDONE"
		conProcess.setState(Constant.UNDONE);
		try {
			/*
			 * 1.��ȡ�ض��û����л�ǩδ��ɵĺ�ͬid
			 */
			List<Integer> conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.���浽conList����
			 */
			for (int conId : conIds) {
				// 鑾� Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getDhqhtList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	/**
	 * ��ǩ��ͬ������ǩ���ݱ��浽���ݿ���
	 * �ж�δ��ǩ�����Ƿ�Ϊ0����Ϊ0����ӡ���ǩ��ɡ���state
	 * 
	 * @param conProcess contract process object
	 * @return boolean Return true if operation successfully锛宱therwise return false
	 * @throws AppException
	 */
	public boolean counterSign(ConProcess conProcess) throws AppException {
		boolean flag = false;// Define flag 
		
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_CSIGN);
		// Set corresponding state of "PROCESS_CSIGN" type  is "DONE"
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) { //�������ݿ��¼
				/*
				 * After countersign successful, statistics total number of persons to be countersigned, if the total number is 0, then all people have completed countersign
				 * and set contract process state to "STATE_CSIGNED"
				 */
				// Pass parameters  through conProcess to statistics the number of persons to be countersigned,set state to "UNDONE"
				conProcess.setState(Constant.UNDONE);

				// δ��ǩ����
				int totalCount = conProcessDao.getTotalCount(conProcess);
				
				// if the number of persons to be countersigned is 0, then all people have completed countersign
				if (totalCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					// Set contract state to "STATE_CSIGNED"
					conState.setType(Constant.STATE_CSIGNED);
					// Save contract state information
					flag = conStateDao.add(conState);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.counterSign");
		}
		return flag;
	}
	
	/**
	 * ͨ����ͬid��ȡ��ͬϸ��
	 * 
	 * @param id Contract id
	 * @return Contract details business entity
	 * @throws AppException
	 */
	public ConDetailBusiModel getContractDetail(int id) throws AppException {
		// Declare conDetailBusiModel
		ConDetailBusiModel conDetailBusiModel = null;
		
		try {
			// Get details of designated contract
			Contract contract = contractDao.getById(id);
			// Get draftman's information that corresponding to the contract
			User user = userDao.getById(contract.getUserId());

			conDetailBusiModel = new ConDetailBusiModel();
			// Set basic information to conDetailBusiModel object
			conDetailBusiModel.setId(contract.getId());
			conDetailBusiModel.setNum(contract.getNum());
			conDetailBusiModel.setName(contract.getName());
			conDetailBusiModel.setCustomer(contract.getCustomer());
			conDetailBusiModel.setBeginTime(contract.getBeginTime());
			conDetailBusiModel.setEndTime(contract.getEndTime());
			conDetailBusiModel.setContent(contract.getContent());
			// Set draftman's name to conDetailBusiModel object
			conDetailBusiModel.setDraftsman(user.getName());
			
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getContractDetail");
		}
		return conDetailBusiModel;
	}
	
	/**
	 * ���ض��û�����ݵ����к�ͬ���ȡ��ǩ��ɵ���δ����ĺ�ͬ��Ϣ
	 * 
	 * @param userId User id
	 * @return Query all contracts that to be finalized
	 * @throws AppException
	 */
	public List<ConBusiModel> getDdghtList(int userId) throws AppException {
		// Initialize conList
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		// Initialize conIds,for saving id set of contracts that to be finalized
		List<Integer> conIds = new ArrayList<Integer>();
		
		try {
			/*
			 * Get drafted and to be finalized contract ,contract to be finalized exist "STATE_CSIGNED" state
			 * And do not exist "STATE_FINALIZED" state at the same time
			 */
			/*
			 * 1.��ȡ�ض��û�����ݵ����к�ͬid
			 */
			List<Integer> drafConIds = contractDao.getIdsByUserId(userId);
			
			/*
			 * 2.���ض��û�����ݵ����к�ͬ���ȡ��ǩ��ɵ���δ����ĺ�ͬ
			 */
			for (int dConId : drafConIds) {
				if (conStateDao.isExist(dConId, Constant.STATE_CSIGNED)
						&& !conStateDao.isExist(dConId,Constant.STATE_FINALIZED)) {
					conIds.add(dConId);
				}
			}
			
			/* 
			 * 3.��ȡ��ͬ��Ϣ
			 */
			for (int conId : conIds) {
				// Get information of designated contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name to conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					// Set draft time to conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getDdghtList");
		}
		// Return conList
		return conList;
	}
	
	/**
	 * �����ͬ 
	 * 
	 * @param contract Contract object
	 * @return boolean Return true if operation successfully锛宱therwise return false 
	 * @throws AppException
	 */
	public boolean finalize(Contract contract) throws AppException {
		boolean flag = false;// Define flag 

		try {
			// Finalize contract:update contract's content
			if (contractDao.updateById(contract)) {
				/*
				 * After finalize contract successfully, set contract's state to "STATE_FINALIZED"
				 */
				// Instantiation conState object, for encapsulate contract state information
				ConState conState = new ConState();

				conState.setConId(contract.getId());
				// Set contract state type to "STATE_FINALIZED"
				conState.setType(Constant.STATE_FINALIZED);
				
				// Save contract state information,assign result to flag
				flag = conStateDao.add(conState);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.finalize");
		}
		return flag;
	}
	
	/**
	 * ��ʾ�ض���ͬ�Ļ�ǩ���
	 * 
	 * @param conId Contract id
	 * @return Contract state object set
	 * @throws AppException
	 */
	public List<CSignatureOpinion> showHQOpinion(int conId) throws AppException {
		// Initialize csOpinionList
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			
			/*
			 * 1.��ȡ�ص��ͬ�ѻ�ǩ��conProcess��id
			 */
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_CSIGN, Constant.DONE);
			/*
			 * 2.Get countersign people and countersign ideas, and designate contract process type to "PROCESS_CSIGN",corresponding "STATE_FINALIZED" state
			 */ 
			for (int id : conProcessIds) {
				// Get contract process information
				ConProcess conProcess = conProcessDao.getById(id);
				// Get countersign people's information
				User user = userDao.getById(conProcess.getUserId());
				// Initialize csOpinion
				CSignatureOpinion csOpinion = new CSignatureOpinion();
				// Set contract id to csOpinion object 
				csOpinion.setConId(conId);
				if (conProcess != null) {
					// Set signature opinion to conBusiModel object
					csOpinion.setOpinion(conProcess.getContent());
				}
				if (user != null) {
					// Set countersign people to csOpinion object
					csOpinion.setCsOperator(user.getName());
				}
				csOpinionList.add(csOpinion);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.showHQOpinion");
		}
		return csOpinionList;
		
	}
	
	/**
	 * ��ȡ�ض� �����ˣ�userId��   ����δ�����ĺ�ͬ��Ϣ
	 * 
	 * @param userId User id
	 * @return Query all contracts to be approved,otherwise return null
	 * @throws AppException
	 */
	public List<ConBusiModel> getDshphtList(int userId) throws AppException {
		// Initialize conList
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		// Initialize conList for saving id set of contract to be approved
		List<Integer> conIds = new ArrayList<Integer>();
		
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_APPROVE"
		conProcess.setType(Constant.PROCESS_APPROVE);
		// Set corresponding state of "PROCESS_APPROVE" type  is "UNDONE"
		conProcess.setState(Constant.UNDONE);
		
		try {
			/*
			 * 1. Get contract id set that to be approved
			 */
			List<Integer> myConIds = conProcessDao.getConIds(conProcess);

			/*
			 * 2.Screen out id set of contract to be approved from distributed contract,and save to conIds
			 * Contract to be approved: exist "STATE_FINALIZED" state in t_contract_state
			 */
			for (int conId : myConIds) {
				if (conStateDao.isExist(conId, Constant.STATE_FINALIZED)) {
					conIds.add(conId);
				}
			}
			
			/*
			 * 3.Get approve conteact's information,and save to contract business entity object,and put entity class to conList
			 */
			for (int conId : conIds) {
				// Get information of designated contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// Initialize conBusiModel object
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id to conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					// Set draft time to conBusiModel object
					conBusiModel.setDrafTime(conState.getTime());
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getDshphtList");
		}
		// Return conList
		return conList;
	}
	
	/**
	 * ������ͬ
	 * 
	 * @param conProcess Contract process object  
	 * @return boolean Return true if operation successfully锛宱therwise return false 
	 * @throws AppException
	 */
	public boolean approve(ConProcess conProcess) throws AppException {
		boolean flag = false;// Define flag
		
		// Set process's operation type to "PROCESS_APPROVE"
		conProcess.setType(Constant.PROCESS_APPROVE);

		try {
			/*
			 * First to do approve operation,then count all the number of persons to be approved and persons approved as "refuse",
			 * if the number of persons to be approved is 0,and the number of persons approved as "refuse" is 0,
			 * so all the approver have complete the approval and pass the approval,
			 * and now set contract process state to "STATE_APPROVED"
			 */
			if (conProcessDao.update(conProcess)) { // To approve contract,enter approval information 
				// Pass Parameter through conProcess to count number of approver,set state to "UNDONE"
				conProcess.setState(Constant.UNDONE);
				// Get total number of persons to be approved
				int tbApprovedCount = conProcessDao.getTotalCount(conProcess);
				
				// Pass Parameter through conProcess to count number of refused approver,set state to "VETOED"
				conProcess.setState(Constant.VETOED);
				// Get total number of persons approved as "refuse"
				int refusedCount = conProcessDao.getTotalCount(conProcess);

				/*
				 * If the number of persons to be approved is 0, then all the approver have been complete approval,
				 * and all passed approval, so save contract state as "STATE_APPROVED"
				 */
				if (tbApprovedCount == 0 && refusedCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					// Set contract state type to "STATE_APPROVED"
					conState.setType(Constant.STATE_APPROVED);
					// Save contract state information
					flag = conStateDao.add(conState);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.approve");
		}
		return flag;
	}
	
	/**
	 * ��ѯ�ص��û�������������δǩ���ĺ�ͬ
	 * 
	 * @param userId User id
	 * @return Query all contracts to be signed,otherwise return false
	 * @throws AppException
	 */
	public List<ConBusiModel> getDqdhtList(int userId) throws AppException {
		// Initialize conList
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		// nitialize conIds for saving contract id set that to be signed
		List<Integer> conIds = new ArrayList<Integer>();
		
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_SIGN"
		conProcess.setType(Constant.PROCESS_SIGN);
		// Set corresponding state of "PROCESS_SIGN" type  is "UNDONE"
		conProcess.setState(Constant.UNDONE);
		
		try {
			/*
			 * 1.Get contract id set that to be approved
			 */
			List<Integer> myConIds = conProcessDao.getConIds(conProcess);

			/*
			 * 2.Screen out id set of contract to be signed from distributed contract,and save to conIds
			 * Contract to be signed: exist "STATE_APPROVED" state in t_contract_state
			 */
			for (int conId : myConIds) {
				if (conStateDao.isExist(conId, Constant.STATE_APPROVED)) {
					conIds.add(conId);
				}
			}
			
			/*
			 * 3. Get information of signed contract,and save to contract business entity object,and put the entity class to conList
			 */
			for (int conId : conIds) {
				// Get information of designated contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name to conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				if (conState != null) {
					// Set draft time to conBusiModel object
					conBusiModel.setDrafTime(conState.getTime());
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getDqdhtList");
		}
		// Return conList
		return conList;
	}
	
	/**
	 * ǩ����ͬ
	 * 
	 * @param conProcess Contract process object
	 * @return boolean Return true if operation successfully锛宱therwise return false 
	 * @throws AppException
	 */
	public boolean sign(ConProcess conProcess) throws AppException {
		boolean flag = false;// Define flag
		
		// Set process's operation type to "PROCESS_SIGN"
		conProcess.setType(Constant.PROCESS_SIGN);
		// Set "PROCESS_SIGN" type corresponding state to "DONE"
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) {// Sign contract:update contract process information
				/*
				 * Sign successful,save contract state information
				 */
				// Instantiation conState object, for encapsulate contract state information
				ConState conState = new ConState();
				conState.setConId(conProcess.getConId());
				// Set contract state type to "STATE_SIGNED"
				conState.setType(Constant.STATE_SIGNED);
				// Save contract state information
				flag = conStateDao.add(conState);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.sign");
		}
		return flag;
	}
	
	/**
	 * Generated contract number, the rule is: year month day hour minute second+5 random numbers when drafting contract,
	 * Will generate a unique number stored in the database, but the contract number is not the primary key in the table.
	 */
	private String generateConNum() {
		// Initialize date
		Date date = new Date();
		// Define date format
		SimpleDateFormat sft = new SimpleDateFormat("yyyyMMddhhmmss");
		
		// Generate a number make up by 5 random numbers
		int rd = new Random().nextInt(99999);
		String rand = "00000" + rd;
		rand = rand.substring(rand.length() - 5);
		
		// Generate contract number is current date and time + 5 random numbers
		String contractNum = sft.format(date) + rand;
		return contractNum;
	}

}
