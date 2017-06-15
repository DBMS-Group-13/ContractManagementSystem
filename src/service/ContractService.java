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
import model.ConDistribute;
import model.ConProcess;
import model.ConState;
import model.Contract;
import model.Customer;
import model.Log;
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
	 * 起草（Draft）合同
	 * 
	 * @param contract 
	 * @return boolean  Return true if successful , otherwise false
	 * @throws AppException
	 */
	public boolean draft(Contract contract) throws AppException {
		boolean flag = false;// Define flag
		
		/*
		 * 1.调用generateConNum()函数生成合同编号（contract number）,将其导入到contract对象中
		 */ 
		contract.setNum(generateConNum());
		
		try {
			/*
			 * 2.如果contract成功保存在数据库中, 将该合同的状态信息（draft contract state）保存在数据库中
			 * 注：保存合同信息的同时会更新合同id（详情可见contractDao.add（）函数中的psmt.getGeneratedKeys()语句）
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
	 * 提取所有未被分配的合同信息
	 * 
	 * @return Query all contracts that need to be allocated; Otherwise return null
	 * @throws AppException
	 */
	public List<ConBusiModel> getDfphtList() throws AppException {
		// Initialize contractList
		List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
	
		try {
			/*
			 * 1.从“t_contract”表中获取状态为“起草”的合同id集合 
			 */
			List<Integer> conIds = conStateDao.getConIdsByType(Constant.STATE_DRAFTED);
			

			/* 2.遍历合同id集, 判断其是否在“t_contract_process"表中有相关记录,
			 * If have records, means the contract has been allocated, otherwise, means have not been allocated
			 */
			for (int conId : conIds) {
				
				/* 
				 * 3.如果合同没有被分配，把需要被分配的合同信息保存到“contract business entity”
				 * 并且把实体存入conList列表中
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
	 * 通过id获取contract实体信息
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
	 * 分配合同
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
	 * 通过会签人id获取所有未会签的合同信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
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
			 * 1.获取特定用户所有会签未完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// 閼撅拷 Get information from  specified contract
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
	 * 管理员获取分配信息
	 * 
	 * @param userId User id
	 * @return Query all contracts that to be countersigned
	 * @throws AppException
	 */
	public List<ConDistribute> getConDistributeList() throws AppException {
		// Initialize  conList
		List<ConDistribute> conList = new ArrayList<ConDistribute>();
		try {
			/*
			 * 1.从“t_contract”表中获取状态为“起草”的合同id集合 
			 */
			List<Integer> conIds = conStateDao.getConIdsByType(Constant.STATE_DRAFTED);
			

			/* 2.遍历合同id集, 判断其是否在“t_contract_process"表中有相关记录,
			 * If have records, means the contract has been allocated, otherwise, means have not been allocated
			 */
			for (int conId : conIds) {
				
				/* 
				 * 3.如果合同已被分配，保存分配信息
				 */
				if (conProcessDao.isExist(conId)) {
					// Get information of designated contract
					Contract contract = contractDao.getById(conId);
					// Get status of designated contract
					ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
					// Instantiate  conBusiModel object
					ConDistribute conDistribute = new ConDistribute();
					if (contract != null) {
						//Set contract id and name to conBusiModel object
						conDistribute.setId(contract.getId());
						conDistribute.setConName(contract.getName());
					}
					if (conState != null) {
						//Set drafting time to conBusiModel object
						conDistribute.setDrafTime(conState.getTime()); 
					}
					
					//会签人名单
					List<Integer> processIds=conProcessDao.getIds(conId, Constant.PROCESS_CSIGN, Constant.DONE);
					processIds.addAll(conProcessDao.getIds(conId, Constant.PROCESS_CSIGN, Constant.UNDONE));
					String names="";
					for(int id:processIds)
					{
						int userId=conProcessDao.getById(id).getUserId();
						names=userDao.getById(userId).getName()+",";
					}
					if(names.length() > 0)
						names=names.substring(0, names.length()-1);
					conDistribute.setCsign(names);
					
					//会签人名单
					processIds=conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.DONE);
					processIds.addAll(conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.UNDONE));
					processIds.addAll(conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.VETOED));
					names="";
					for(int id:processIds)
					{
						int userId=conProcessDao.getById(id).getUserId();
						if(userDao.getById(userId) != null && userDao.getById(userId).getName() != null)
							names= userDao.getById(userId).getName()+",";
					}
					if(names.length() > 0)
						names=names.substring(0, names.length()-1);
					conDistribute.setApprove(names);
					
					//会签人名单
					processIds=conProcessDao.getIds(conId, Constant.PROCESS_SIGN, Constant.DONE);
					processIds.addAll(conProcessDao.getIds(conId, Constant.PROCESS_SIGN, Constant.UNDONE));
					names="";
					for(int id:processIds)
					{
						int userId=conProcessDao.getById(id).getUserId();
						names=userDao.getById(userId).getName()+",";
					}
					if(names.length() > 0)
						names=names.substring(0, names.length()-1);
					conDistribute.setSign(names);
					
					conList.add(conDistribute); // Add conBusiModel to contractList
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getConDistributeList");
		}
		// Return contractList
		return conList;
		
		
	}
	
	/**
	 * 通过会签人id获取所有已会签的合同信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
	 * @throws AppException
	 */
	public List<ConBusiModel> getProcess_CounteredList(int userId)throws AppException{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_CSIGN);
		// Set corresponding state of "PROCESS_CSIGN" type  is "DONE"
		conProcess.setState(Constant.DONE);
		
		try {
			/*
			 * 1.获取特定用户所有会签完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// 该合同已会签人数
				ConProcess CSIGN_Done=new ConProcess();
				CSIGN_Done.setConId(conId);
				CSIGN_Done.setState(Constant.DONE);
				CSIGN_Done.setType(Constant.PROCESS_CSIGN);
				int totalDoneCount=conProcessDao.getTotalCount(CSIGN_Done);
				
				ConProcess CSIGN_UNDone=new ConProcess();
				CSIGN_UNDone.setConId(conId);
				CSIGN_UNDone.setState(Constant.UNDONE);
				CSIGN_UNDone.setType(Constant.PROCESS_CSIGN);
				// 该合同会签人数
				int totalCount=totalDoneCount+conProcessDao.getTotalCount(CSIGN_UNDone);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_CounteredList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	/**
	 * 会签合同，将会签内容保存到数据库里
	 * 判断未会签人数是否为0，若为0则添加“会签完成”的state
	 * 
	 * @param conProcess contract process object
	 * @return boolean Return true if operation successfully閿涘therwise return false
	 * @throws AppException
	 */
	public boolean counterSign(ConProcess conProcess) throws AppException {
		boolean flag = false;// Define flag 
		
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_CSIGN);
		// Set corresponding state of "PROCESS_CSIGN" type  is "DONE"
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) { //更新数据库记录
				/*
				 * After countersign successful, statistics total number of persons to be countersigned, if the total number is 0, then all people have completed countersign
				 * and set contract process state to "STATE_CSIGNED"
				 */
				// Pass parameters  through conProcess to statistics the number of persons to be countersigned,set state to "UNDONE"
				conProcess.setState(Constant.UNDONE);

				// 未会签人数
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
	 * 通过合同id获取合同细节
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
			if(user != null &&user.getName() != null)
				conDetailBusiModel.setDraftsman(user.getName());
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getContractDetail");
		}
		return conDetailBusiModel;
	}
	
	/**
	 * 获取所有合同细节
	 * 
	 * @param id Contract id
	 * @return Contract details business entity
	 * @throws AppException
	 */
	public List<ConDetailBusiModel> getContractDetailList() throws AppException {
		// Declare conDetailBusiModel
		List<ConDetailBusiModel> conList=new ArrayList<ConDetailBusiModel>();
		
		List<Integer> conIds=contractDao.getIds();
		
		for(int id:conIds)
		{
			conList.add(getContractDetail(id));
		}
		
		return conList;
	}
	
	/**
	 * 从特定用户所起草的所有合同里抽取会签完成但并未定稿的合同信息
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
			 * 1.获取特定用户的起草的所有合同id
			 */
			List<Integer> drafConIds = contractDao.getIdsByUserId(userId);
			
			/*
			 * 2.从特定用户所起草的所有合同里抽取会签完成但并未定稿的合同
			 */
			for (int dConId : drafConIds) {
				if (conStateDao.isExist(dConId, Constant.STATE_CSIGNED)
						&& !conStateDao.isExist(dConId,Constant.STATE_FINALIZED)) {
					conIds.add(dConId);
				}
			}
			
			/* 
			 * 3.获取合同信息
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
	 * 定稿合同 
	 * 
	 * @param contract Contract object
	 * @return boolean Return true if operation successfully閿涘therwise return false 
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
	 * 通过起草员id 获取所有已定稿的合同信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
	 * @throws AppException
	 */
	public List<ConBusiModel> getProcess_FinalizeList(int userId)throws AppException{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();  //要返回的合同简略信息列表
		List<Integer> drafConIds = contractDao.getIdsByUserId(userId);
		
		try {
			/*
			 * 1.获取特定用户所有定稿的合同id
			 */
			List<Integer> conIds= new ArrayList<Integer>();
			for (int conId : drafConIds) {
				if(conStateDao.isExist(conId, Constant.STATE_FINALIZED))
					conIds.add(conId);
			}

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
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
			throw new AppException("service.ContractService.getProcess_FinalizeList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	/**
	 * 显示特定合同的会签意见
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
			 * 1.获取特点合同已会签的conProcess的id
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
	 * 获取审批人（userId）   所有未审批的合同信息
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
	 * 获取审批人（userId）   所有已审批的合同信息
	 * 
	 * @param userId User id
	 * @return Query all contracts to be approved,otherwise return null
	 * @throws AppException
	 */
	public List<ConBusiModel> getProcess_ApproveList(int userId)throws AppException{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_APPROVE);
		// Set corresponding state of "PROCESS_CSIGN" type  is "DONE"
		conProcess.setState(Constant.DONE);
		
		try {
			/*
			 * 1.获取特定用户所有审批同意的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// 该合同已会签人数
				ConProcess APPROVE_Done=new ConProcess();
				APPROVE_Done.setConId(conId);
				APPROVE_Done.setState(Constant.DONE);
				APPROVE_Done.setType(Constant.PROCESS_APPROVE);
				int totalDoneCount=conProcessDao.getTotalCount(APPROVE_Done);
				
				ConProcess APPROVE_UNDone=new ConProcess();
				APPROVE_UNDone.setConId(conId);
				APPROVE_UNDone.setState(Constant.UNDONE);
				APPROVE_UNDone.setType(Constant.PROCESS_APPROVE);
				
				ConProcess APPROVE_VETOED=new ConProcess();
				APPROVE_VETOED.setConId(conId);
				APPROVE_VETOED.setState(Constant.VETOED);
				APPROVE_VETOED.setType(Constant.PROCESS_APPROVE);
				// 该合同会签人数
				int totalCount=totalDoneCount+conProcessDao.getTotalCount(APPROVE_UNDone)+conProcessDao.getTotalCount(APPROVE_VETOED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				conBusiModel.setIsRefuse(false);
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
			
			
			
			/*
			 * 1.获取特定用户所有审批否定的合同id
			 */
			conProcess.setState(Constant.VETOED);
			conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// 该合同已会签人数
				ConProcess APPROVE_Done=new ConProcess();
				APPROVE_Done.setConId(conId);
				APPROVE_Done.setState(Constant.DONE);
				APPROVE_Done.setType(Constant.PROCESS_APPROVE);
				int totalDoneCount=conProcessDao.getTotalCount(APPROVE_Done);
				
				ConProcess APPROVE_UNDone=new ConProcess();
				APPROVE_UNDone.setConId(conId);
				APPROVE_UNDone.setState(Constant.UNDONE);
				APPROVE_UNDone.setType(Constant.PROCESS_APPROVE);
				
				ConProcess APPROVE_VETOED=new ConProcess();
				APPROVE_VETOED.setConId(conId);
				APPROVE_VETOED.setState(Constant.VETOED);
				APPROVE_VETOED.setType(Constant.PROCESS_APPROVE);
				// 该合同会签人数
				int totalCount=totalDoneCount+conProcessDao.getTotalCount(APPROVE_UNDone)+conProcessDao.getTotalCount(APPROVE_VETOED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				conBusiModel.setIsRefuse(true);
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_ApproveList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	/**
	 * 通过审批员id获取所有审批通过的合同信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
	 * @throws AppException
	 */
	public List<ConBusiModel> getProcess_ApproveDONEList(int userId)throws AppException{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_APPROVE);
		// Set corresponding state of "PROCESS_CSIGN" type  is "DONE"
		conProcess.setState(Constant.DONE);
		
		try {
			/*
			 * 1.获取特定用户所有会签完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// 该合同已会签人数
				ConProcess APPROVE_Done=new ConProcess();
				APPROVE_Done.setConId(conId);
				APPROVE_Done.setState(Constant.DONE);
				APPROVE_Done.setType(Constant.PROCESS_APPROVE);
				int totalDoneCount=conProcessDao.getTotalCount(APPROVE_Done);
				
				ConProcess APPROVE_UNDone=new ConProcess();
				APPROVE_UNDone.setConId(conId);
				APPROVE_UNDone.setState(Constant.UNDONE);
				APPROVE_UNDone.setType(Constant.PROCESS_APPROVE);
				
				ConProcess APPROVE_VETOED=new ConProcess();
				APPROVE_VETOED.setConId(conId);
				APPROVE_VETOED.setState(Constant.VETOED);
				APPROVE_VETOED.setType(Constant.PROCESS_APPROVE);
				// 该合同会签人数
				int totalCount=totalDoneCount+conProcessDao.getTotalCount(APPROVE_UNDone)+conProcessDao.getTotalCount(APPROVE_VETOED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_ApproveList");
		}
		// Return the set of storage contract business entities
		return conList;
	}

	/**
	 * 通过审批员id获取所有审批否定的合同信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
	 * @throws AppException
	 */
	public List<ConBusiModel> getProcess_ApproveVETOEDList(int userId)throws AppException{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		ConProcess conProcess = new ConProcess();
		// Set values to contract process object
		conProcess.setUserId(userId);
		// Set process's operation type to "PROCESS_CSIGN"
		conProcess.setType(Constant.PROCESS_APPROVE);
		// Set corresponding state of "PROCESS_CSIGN" type  is "DONE"
		conProcess.setState(Constant.VETOED);
		
		try {
			/*
			 * 1.获取特定用户所有会签完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(conProcess);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// 该合同已会签人数
				ConProcess APPROVE_Done=new ConProcess();
				APPROVE_Done.setConId(conId);
				APPROVE_Done.setState(Constant.DONE);
				APPROVE_Done.setType(Constant.PROCESS_APPROVE);
				int totalDoneCount=conProcessDao.getTotalCount(APPROVE_Done);
				
				ConProcess APPROVE_UNDone=new ConProcess();
				APPROVE_UNDone.setConId(conId);
				APPROVE_UNDone.setState(Constant.UNDONE);
				APPROVE_UNDone.setType(Constant.PROCESS_APPROVE);
				
				ConProcess APPROVE_VETOED=new ConProcess();
				APPROVE_VETOED.setConId(conId);
				APPROVE_VETOED.setState(Constant.VETOED);
				APPROVE_VETOED.setType(Constant.PROCESS_APPROVE);
				// 该合同会签人数
				int totalCount=totalDoneCount+conProcessDao.getTotalCount(APPROVE_UNDone)+conProcessDao.getTotalCount(APPROVE_VETOED);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_ApproveList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	/**
	 * 审批合同
	 * 
	 * @param conProcess Contract process object  
	 * @return boolean Return true if operation successfully閿涘therwise return false 
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
				if (tbApprovedCount == 0) {
					if(refusedCount == 0)
					{
						ConState conState = new ConState();
						conState.setConId(conProcess.getConId());
						// Set contract state type to "STATE_APPROVED"
						conState.setType(Constant.STATE_APPROVED);
						// Save contract state information
						flag = conStateDao.add(conState);
					}else
					{
						contractDao.setDel(conProcess.getConId());
					}
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
	 * 显示特定合同的审批同意意见
	 * 
	 * @param conId Contract id
	 * @return Contract state object set
	 * @throws AppException
	 */
	public List<CSignatureOpinion> showAPOpinion(int conId) throws AppException {
		// Initialize csOpinionList
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			
			/*
			 * 1.获取特点合同已审批的conProcess的id
			 */
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.DONE);
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
					"service.ContractService.showAPOpinion");
		}
		return csOpinionList;
	}
	
	/**
	 * 显示特定合同的审批拒绝意见
	 * 
	 * @param conId Contract id
	 * @return Contract state object set
	 * @throws AppException
	 */
	public List<CSignatureOpinion> showAPVETOEDOpinion(int conId) throws AppException {
		// Initialize csOpinionList
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			
			/*
			 * 1.获取特点合同已审批的conProcess的id
			 */
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.VETOED);
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
					"service.ContractService.showAPVETOEDOpinion");
		}
		return csOpinionList;
	}
	
	/**
	 * 查询 签订员 所有未签订（审批完成）的合同
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
	 * 签订合同
	 * 
	 * @param conProcess Contract process object
	 * @return boolean Return true if operation successfully閿涘therwise return false 
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
				
				// Instantiation conState object, for encapsulate contract state information
				conProcess.setState(Constant.UNDONE);
                int totalCount = conProcessDao.getTotalCount(conProcess);
				
				if (totalCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					// Set contract state to "STATE_CSIGNED"
					conState.setType(Constant.STATE_SIGNED);
					// Save contract state information
					flag = conStateDao.add(conState);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.sign");
		}
		return flag;
	}
	
	/**
	 * 通过起草员id 获取所有已签订完成的合同信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
	 * @throws AppException
	 */
	public List<ConBusiModel> getProcess_SignedList(int userId)throws AppException
	{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();  //要返回的合同简略信息列表
		List<Integer> drafConIds = contractDao.getIdsByUserId(userId);
		
		try {
			/*
			 * 1.获取特定用户所有签订完成的合同id
			 */
			List<Integer> conIds= new ArrayList<Integer>();
			for (int conId : drafConIds) {
				if(conStateDao.isExist(conId, Constant.STATE_SIGNED))
					conIds.add(conId);
			}

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				
				ConProcess SIGN_Done=new ConProcess();
				SIGN_Done.setConId(conId);
				SIGN_Done.setState(Constant.DONE);
				SIGN_Done.setType(Constant.PROCESS_SIGN);
				int totalDoneCount=conProcessDao.getTotalCount(SIGN_Done);
				
				ConProcess SIGN_UNDone=new ConProcess();
				SIGN_UNDone.setConId(conId);
				SIGN_UNDone.setState(Constant.UNDONE);
				SIGN_UNDone.setType(Constant.PROCESS_SIGN);

				// 该合同签订人数
				int totalCount=totalDoneCount+conProcessDao.getTotalCount(SIGN_UNDone);
				// Initialize conBusiModel
				ConBusiModel conBusiModel = new ConBusiModel();
				if (contract != null) {
					// Set contract id and name into conBusiModel object
					conBusiModel.setConId(contract.getId());
					conBusiModel.setConName(contract.getName());
				}
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				if (conState != null) {
					// Set Drafting time into conBusiModel object
					conBusiModel.setDrafTime(conState.getTime()); 
				}
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_SignedList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	/**
	 * 通过起草员id(如果id==-1，则为管理员，获取所以合同的状态信息) 获取该用户起草的所有合同的状态信息
	 * 
	 * @param userId User id
	 * @return Query all countersigned contracts 
	 * @throws AppException
	 */
	public List<ConBusiModel> getContract_StateList(int userId)throws AppException
	{
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();  //要返回的合同简略信息列表
		List<Integer> drafConIds;
		if(userId!=-1)
			drafConIds= contractDao.getIdsByUserId(userId);
		else {
			//获取所有合同id
			drafConIds= contractDao.getIds();
		}
		
		try {
			for (int conId : drafConIds) {
				ConBusiModel conBusiModel = new ConBusiModel();
				// Get information from  specified contract
				Contract contract = contractDao.getById(conId);
				// Get status of designated contract
				ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
				// get state of contract
				int state=-1;
				if(conStateDao.isExist(conId, Constant.STATE_SIGNED))
				{
					state=Constant.STATE_SIGNED;
					conBusiModel.setState("Signed");
				}else if(conStateDao.isExist(conId, Constant.STATE_APPROVED))
				{
					state=Constant.STATE_APPROVED;
					conBusiModel.setState("Signing");
				}else if(conStateDao.isExist(conId, Constant.STATE_FINALIZED))
				{
					state=Constant.STATE_FINALIZED;
					conBusiModel.setState("Approving");
				}else if(conStateDao.isExist(conId, Constant.STATE_CSIGNED))
				{
					state=Constant.STATE_CSIGNED;
					conBusiModel.setState("Finalizing");
				}else if(conStateDao.isExist(conId, Constant.STATE_DRAFTED))
				{
					state=Constant.STATE_DRAFTED;
					conBusiModel.setState("csigning");
				}
				
				//根据状态获取完成人数比例
				if(state==Constant.STATE_APPROVED||state==Constant.STATE_SIGNED)
				{
					ConProcess SIGN_Done=new ConProcess();
					SIGN_Done.setConId(conId);
					SIGN_Done.setState(Constant.DONE);
					SIGN_Done.setType(Constant.PROCESS_SIGN);
					int totalDoneCount=conProcessDao.getTotalCount(SIGN_Done);
					
					ConProcess SIGN_UNDone=new ConProcess();
					SIGN_UNDone.setConId(conId);
					SIGN_UNDone.setState(Constant.UNDONE);
					SIGN_UNDone.setType(Constant.PROCESS_SIGN);

					// 该合同签订人数
					int totalCount=totalDoneCount+conProcessDao.getTotalCount(SIGN_UNDone);
					conBusiModel.setDONENum(totalDoneCount);
					conBusiModel.setDistributeENum(totalCount);
				}else if(state==Constant.STATE_FINALIZED)
				{
					ConProcess APPROVE_Done=new ConProcess();
					APPROVE_Done.setConId(conId);
					APPROVE_Done.setState(Constant.DONE);
					APPROVE_Done.setType(Constant.PROCESS_APPROVE);
					int totalDoneCount=conProcessDao.getTotalCount(APPROVE_Done);
					
					ConProcess APPROVE_UNDone=new ConProcess();
					APPROVE_UNDone.setConId(conId);
					APPROVE_UNDone.setState(Constant.UNDONE);
					APPROVE_UNDone.setType(Constant.PROCESS_APPROVE);
					
					ConProcess APPROVE_VETOED=new ConProcess();
					APPROVE_VETOED.setConId(conId);
					APPROVE_VETOED.setState(Constant.VETOED);
					APPROVE_VETOED.setType(Constant.PROCESS_APPROVE);
					// 该合同会签人数
					int totalCount=totalDoneCount+conProcessDao.getTotalCount(APPROVE_UNDone)+conProcessDao.getTotalCount(APPROVE_VETOED);
					conBusiModel.setDONENum(totalDoneCount);
					conBusiModel.setDistributeENum(totalCount);
					
					ConProcess vetoed=new ConProcess();
					vetoed.setConId(conId);
					vetoed.setType(Constant.PROCESS_APPROVE);
					vetoed.setState(Constant.VETOED);
					//判断审批是否存在否定的情况
					if(conProcessDao.getTotalCount(vetoed)!=0)
					{
						conBusiModel.setIsRefuse(true);
					}
				}else if(state==Constant.STATE_DRAFTED)
				{
					ConProcess CSIGN_Done=new ConProcess();
					CSIGN_Done.setConId(conId);
					CSIGN_Done.setState(Constant.DONE);
					CSIGN_Done.setType(Constant.PROCESS_CSIGN);
					int totalDoneCount=conProcessDao.getTotalCount(CSIGN_Done);
					
					ConProcess CSIGN_UNDone=new ConProcess();
					CSIGN_UNDone.setConId(conId);
					CSIGN_UNDone.setState(Constant.UNDONE);
					CSIGN_UNDone.setType(Constant.PROCESS_CSIGN);
					// 该合同会签人数
					int totalCount=totalDoneCount+conProcessDao.getTotalCount(CSIGN_UNDone);
					conBusiModel.setDONENum(totalDoneCount);
					conBusiModel.setDistributeENum(totalCount);
				}
				
				
				// Initialize conBusiModel
				
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
			throw new AppException("service.ContractService.getProcess_SignedList");
		}
		// Return the set of storage contract business entities
		return conList;
	}
	
	public List<ConBusiModel> SearchConBusiModel(List<ConBusiModel> list,String conName)
	{
		List<ConBusiModel> CBMList=new ArrayList<ConBusiModel>();
		for(ConBusiModel conBusiModel:list)
		{
			if(conBusiModel.getConName().equals(conName))
			{
				CBMList.add(conBusiModel);
			}
		}
		return CBMList;
	}
	
	public List<User> SearchUser(List<User> list,String userName)
	{
		List<User> userList=new ArrayList<User>();
		for(User user:list)
		{
			if(user.getName().equals(userName))
			{
				userList.add(user);
			}
		}
		return userList;
	}
	
	public List<Customer> SearchCustomer(List<Customer> list,String userName)
	{
		List<Customer> customerList=new ArrayList<Customer>();
		for(Customer customer:list)
		{
			if(customer.getName().equals(userName))
			{
				customerList.add(customer);
			}
		}
		return customerList;
	}
	
	/**
	 * Generated contract number, the rule is: year month day hour minute second+5 random numbers when drafting contract,
	 * Will generate a unique number stored in the database, but the contract number is not the primary key in the table.
	 */
	public String generateConNum() {
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
	
	public List<Log> getLog() throws AppException{
		UserDao ud = new UserDaoImpl();
		List<Log> logList=new ArrayList<Log>();
		try {
			logList = ud.getLogs();
			return logList;
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getLog");
		}
	}
	
	public List<ConDetailBusiModel> getConBusis() throws AppException{
		List<Integer> conIds = new ArrayList<Integer>();
		List<ConDetailBusiModel> conList = new ArrayList<ConDetailBusiModel>();
		try {
			conIds = contractDao.getIds();
			for(int conId:conIds){
				conList.add(getContractDetail(conId));
			}
			return conList;
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getConBusis");
		}
	}
	
	public boolean deleteCon(int conId) throws AppException{
		try{
		return contractDao.setDel(conId);
		}catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getConBusis");
		}
	}
}
