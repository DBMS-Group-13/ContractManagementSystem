package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import dao.ConProcessDao;
import dao.ConStateDao;
import dao.ContractDao;
import dao.UserDao;
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
import utils.MailUtil;

/**
 *	合同服务层
 */
public class ContractService {
	
	private ContractDao contractDao = null;    //ContractDao实例
	private ConStateDao conStateDao = null;    //conStateDao实例
	private ConProcessDao conProcessDao = null;//ConProcessDao实例
	private UserDao userDao = null;            //UserDao实例

	/**
	 * 构造函数
	 */
	public ContractService() {
		contractDao = new ContractDao();
		conStateDao = new ConStateDao();
		conProcessDao = new ConProcessDao();
		userDao = new UserDao();
	}
	
	/**
	 * 起草（Draft）合同
	 * 
	 * @param contract 
	 * @return boolean  Return true if successful , otherwise false
	 * @throws AppException
	 */
	public boolean draft(Contract contract) throws AppException {
		boolean flag = false;//成功标志
		
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
				//实例化一个状态为"STATE_DRAFTED"的ConState对象
				ConState conState = new ConState();
				conState.setConId(contract.getId());  
				conState.setType(Constant.STATE_DRAFTED);
				//将合同状态保存到数据库中
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
	public List<ConBusiModel> getUNDistributeList() throws AppException {
		//合同简略信息列表
		List<ConBusiModel> contractList = new ArrayList<ConBusiModel>();
	
		try {
			/*
			 * 1.从“t_contract”表中获取状态为“起草”的合同id集合 
			 */
			List<Integer> conIds = conStateDao.getConIdsByType(Constant.STATE_DRAFTED);
			

			/* 2.遍历合同id集, 判断其是否在“t_contract_process"表中有相关记录,
			 * 有记录说明以分配
			 */
			for (int conId : conIds) {
				
				/* 
				 * 3.如果合同没有被分配，把需要被分配的合同信息保存到“ConBusiModel”实例
				 * 并且把实体存入conList列表中
				 */
				if (!conProcessDao.isExist(conId)) {
					ConBusiModel conBusiModel=getConBusiModel(conId);
					contractList.add(conBusiModel); 
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getDfphtList");
		}
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
		Contract contract = null;
		
		try {
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
		boolean flag = false;
		try {
			if(type == 1){
				User user = new User();
				user = userDao.getById(userId);
				MailUtil.sendMail(user.getEmail(), "ContractSystem:合同提醒", "<p>用户:"+user.getName()+" 您好 O(∩_∩)O~~<br><br>您在ContractSystem有一份新合同正在等待您的会签。<br><br>请尽快登录系统完成操作！:)<br>");
			}
			if(type == 2){
				User user = new User();
				user = userDao.getById(userId);
				MailUtil.sendMail(user.getEmail(), "ContractSystem:合同提醒", "<p>用户:"+user.getName()+" 您好 O(∩_∩)O~~<br><br>您在ContractSystem有一份新合同正在等待您的审批。<br><br>请尽快登录系统完成操作！:)<br>");
			}
			if(type == 3){
				User user = new User();
				user = userDao.getById(userId);
				MailUtil.sendMail(user.getEmail(), "ContractSystem:合同提醒", "<p>用户:"+user.getName()+" 您好 O(∩_∩)O~~<br><br>您在ContractSystem有一份新合同正在等待您的签订。<br><br>请尽快登录系统完成操作！:)<br>");
			}
			ConProcess conProcess = new ConProcess();
			//分配合同流程
			conProcess.setConId(conId);
			conProcess.setType(type);
			//流程状态为未做
			conProcess.setState(Constant.UNDONE);
			conProcess.setUserId(userId);
			
			flag = conProcessDao.add(conProcess);
		} catch (AppException | MessagingException e) {
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
	public List<ConBusiModel> getUNCsignList(int userId) throws AppException {
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		try {
			/*
			 * 1.获取特定用户所有会签未完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(userId,Constant.PROCESS_CSIGN,Constant.UNDONE);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getDhqhtList");
		}
		
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
		List<ConDistribute> conList = new ArrayList<ConDistribute>();
		try {
			/*
			 * 1.从“t_contract”表中获取状态为“起草”的合同id集合 
			 */
			List<Integer> conIds = conStateDao.getConIdsByType(Constant.STATE_DRAFTED);
			

			/* 2.遍历合同id集, 判断其是否在“t_contract_process"表中有相关记录,
			 * 若有记录，则说明已经分配
			 */
			for (int conId : conIds) {
				
				/* 
				 * 3.如果合同已被分配，保存分配信息
				 */
				if (conProcessDao.isExist(conId)) {
					Contract contract = contractDao.getById(conId);
					ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
					
					ConDistribute conDistribute = new ConDistribute();
					if (contract != null) {
						conDistribute.setId(contract.getId());
						conDistribute.setConName(contract.getName());
					}
					if (conState != null) {
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
						names=userDao.getById(userId).getName()+",";
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
					
					conList.add(conDistribute); 
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
		
		try {
			/*
			 * 1.获取特定用户所有会签完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(userId,Constant.PROCESS_CSIGN,Constant.DONE);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
				
				//获取会签完成比
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
				
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_CounteredList");
		}
		
		return conList;
	}
	
	/**
	 * 会签合同，将会签内容保存到数据库里
	 * 判断未会签人数是否为0，若为0则添加“会签完成”的state
	 * 
	 * @param conProcess contract process object
	 * @return boolean Return true if operation successfully otherwise return false
	 * @throws AppException
	 */
	public boolean counterSign(ConProcess conProcess) throws AppException {
		boolean flag = false;// Define flag 
		
		
		conProcess.setType(Constant.PROCESS_CSIGN);
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) { //更新数据库记录
				
				//获取未会签完成的人数
				conProcess.setState(Constant.UNDONE);
				int totalCount = conProcessDao.getTotalCount(conProcess);
				
				//如果未会签人数为0 添加会签完成的状态
				if (totalCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					conState.setType(Constant.STATE_CSIGNED);
					
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
	public ConDetailBusiModel getContractDetail(int conId) throws AppException {
		
		ConDetailBusiModel conDetailBusiModel = null;
		
		try {
			//获取合同信息
			Contract contract = contractDao.getById(conId);
			//获取合同起草者信息
			User user = userDao.getById(contract.getUserId());

			conDetailBusiModel = new ConDetailBusiModel();
			conDetailBusiModel.setId(contract.getId());
			conDetailBusiModel.setNum(contract.getNum());
			conDetailBusiModel.setName(contract.getName());
			conDetailBusiModel.setCustomer(contract.getCustomer());
			conDetailBusiModel.setBeginTime(contract.getBeginTime());
			conDetailBusiModel.setEndTime(contract.getEndTime());
			conDetailBusiModel.setContent(contract.getContent());
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
	public List<ConBusiModel> getUNFinalizedList(int userId) throws AppException {
		
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		
		List<Integer> conIds = new ArrayList<Integer>();
		
		try {
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
				ConBusiModel conBusiModel=getConBusiModel(conId);
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getDdghtList");
		}
		
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
		boolean flag = false;

		try {
			
			if (contractDao.updateById(contract)) {
				// 定稿成功，添加状态为"STATE_FINALIZED"的合同状态信息
				ConState conState = new ConState();

				conState.setConId(contract.getId());
				conState.setType(Constant.STATE_FINALIZED);
				
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
				ConBusiModel conBusiModel=getConBusiModel(conId);
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_FinalizeList");
		}
		
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
		
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			
			/*
			 * 1.获取特点合同已会签的conProcess的id
			 */
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_CSIGN, Constant.DONE);
			/*
			 * 2.获取会签意见以及会签人信息
			 */ 
			for (int id : conProcessIds) {
				ConProcess conProcess = conProcessDao.getById(id);
				User user = userDao.getById(conProcess.getUserId());
				
				CSignatureOpinion csOpinion = new CSignatureOpinion();
				csOpinion.setConId(conId);
				if (conProcess != null) {
					csOpinion.setOpinion(conProcess.getContent());
				}
				if (user != null) {
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
	public List<ConBusiModel> getUNApprovetList(int userId) throws AppException {
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		List<Integer> conIds = new ArrayList<Integer>();
		
		
		try {
			/*
			 * 1.获取未审批的合同id（此时合同状态未定）
			 */
			List<Integer> myConIds = conProcessDao.getConIds(userId,Constant.PROCESS_APPROVE,Constant.UNDONE);

			/*
			 * 2.筛选出已定稿的的合同id
			 */
			for (int conId : myConIds) {
				if (conStateDao.isExist(conId, Constant.STATE_FINALIZED)) {
					conIds.add(conId);
				}
			}
			
			/*
			 * 3.获取合同简略信息
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getDshphtList");
		}
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
		
		try {
			/*
			 * 1.获取特定用户所有审批同意的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(userId,Constant.PROCESS_APPROVE,Constant.DONE);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
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

				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				conBusiModel.setIsRefuse(false);
				
				conList.add(conBusiModel);
			}
			
			
			
			/*
			 * 1.获取特定用户所有审批否定的合同id
			 */
			conIds = conProcessDao.getConIds(userId,Constant.PROCESS_APPROVE,Constant.VETOED);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
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
				
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				conBusiModel.setIsRefuse(true);
				
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_ApproveList");
		}
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
		
		try {
			/*
			 * 1.获取特定用户所有会签完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(userId,Constant.PROCESS_APPROVE,Constant.DONE);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
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
				
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_ApproveList");
		}
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
		
		try {
			/*
			 * 1.获取特定用户所有会签完成的合同id
			 */
			List<Integer> conIds = conProcessDao.getConIds(userId,Constant.PROCESS_APPROVE,Constant.VETOED);

			/* 
			 * 2.保存到conList数组
			 */
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
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
				
				conBusiModel.setDONENum(totalDoneCount);
				conBusiModel.setDistributeENum(totalCount);
				
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_ApproveList");
		}
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
		boolean flag = false;
		
		conProcess.setType(Constant.PROCESS_APPROVE);

		try {
			if (conProcessDao.update(conProcess)) {
				//审批完成，获取未审批人数以及否决人数
				conProcess.setState(Constant.UNDONE);
				int tbApprovedCount = conProcessDao.getTotalCount(conProcess);
				
				conProcess.setState(Constant.VETOED);
				int refusedCount = conProcessDao.getTotalCount(conProcess);

				
				if (tbApprovedCount == 0) {
					if(refusedCount == 0)  //如果所有人均审批通过，则添加审批完成的合同状态
					{
						ConState conState = new ConState();
						conState.setConId(conProcess.getConId());
						conState.setType(Constant.STATE_APPROVED);
						
						flag = conStateDao.add(conState);
					}else   //如果所有人都审批完成但有人投了否决，该合同作废
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
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			
			/*
			 * 1.获取特点合同已审批的conProcess的id
			 */
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.DONE);
			
			for (int id : conProcessIds) {
				ConProcess conProcess = conProcessDao.getById(id);
				User user = userDao.getById(conProcess.getUserId());
				
				CSignatureOpinion csOpinion = new CSignatureOpinion();
				csOpinion.setConId(conId);
				if (conProcess != null) {
					csOpinion.setOpinion(conProcess.getContent());
				}
				if (user != null) {
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
		
		List<CSignatureOpinion> csOpinionList = new ArrayList<CSignatureOpinion>();
		
		try {
			
			/*
			 * 1.获取特点合同已审批的conProcess的id
			 */
			List<Integer> conProcessIds = conProcessDao.getIds(conId, Constant.PROCESS_APPROVE, Constant.VETOED);
			
			for (int id : conProcessIds) {
				ConProcess conProcess = conProcessDao.getById(id);
				User user = userDao.getById(conProcess.getUserId());
				
				CSignatureOpinion csOpinion = new CSignatureOpinion();
				csOpinion.setConId(conId);
				if (conProcess != null) {
					csOpinion.setOpinion(conProcess.getContent());
				}
				if (user != null) {
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
	public List<ConBusiModel> getUNSignList(int userId) throws AppException {
		
		List<ConBusiModel> conList = new ArrayList<ConBusiModel>();
		List<Integer> conIds = new ArrayList<Integer>();
		
		ConProcess conProcess = new ConProcess();
		conProcess.setUserId(userId);
		conProcess.setType(Constant.PROCESS_SIGN);
		conProcess.setState(Constant.UNDONE);
		
		try {
			/*
			 * 1.获取未签订的合同id
			 */
			List<Integer> myConIds = conProcessDao.getConIds(userId,Constant.PROCESS_SIGN,Constant.UNDONE);

			//筛选已审批完成的
			for (int conId : myConIds) {
				if (conStateDao.isExist(conId, Constant.STATE_APPROVED)) {
					conIds.add(conId);
				}
			}
			
			
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException(
					"service.ContractService.getDqdhtList");
		}
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
		boolean flag = false;
		conProcess.setType(Constant.PROCESS_SIGN);
		conProcess.setState(Constant.DONE);
		
		try {
			if (conProcessDao.update(conProcess)) {
				//签订成功，获取已签订人数
				conProcess.setState(Constant.UNDONE);
                int totalCount = conProcessDao.getTotalCount(conProcess);
				
                //若所有人已签订，添加签到完成状态
				if (totalCount == 0) {
					ConState conState = new ConState();
					conState.setConId(conProcess.getConId());
					conState.setType(Constant.STATE_SIGNED);
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
		List<Integer> conIds = conProcessDao.getConIds(userId, Constant.PROCESS_SIGN, Constant.DONE);
		
		try {
			for (int conId : conIds) {
				ConBusiModel conBusiModel=getConBusiModel(conId);
				
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
				
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_SignedList");
		}
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
				ConBusiModel conBusiModel=getConBusiModel(conId);
				
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
			
				conList.add(conBusiModel);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getProcess_SignedList");
		}
		return conList;
	}
	
	/**
	 * 根据合同名称查询合同简略信息
	 * @param list
	 * @param userName
	 * @return
	 */
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
	
	/**
	 * 根据姓名查询用户信息
	 * @param list
	 * @param userName
	 * @return
	 */
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
	
	/**
	 * 根据姓名查询客户信息
	 * @param list
	 * @param userName
	 * @return
	 */
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
	 * 生成合同编号
	 */
	public String generateConNum() {
		Date date = new Date();
		SimpleDateFormat sft = new SimpleDateFormat("yyyyMMddhhmmss");
		
		
		int rd = new Random().nextInt(99999);
		String rand = "00000" + rd;
		rand = rand.substring(rand.length() - 5);
		
		String contractNum = sft.format(date) + rand;
		return contractNum;
	}
	
	/**
	 * 获取日志
	 * @return
	 * @throws AppException
	 */
	public List<Log> getLog() throws AppException{
		UserDao ud = new UserDao();
		List<Log> logList=new ArrayList<Log>();
		try {
			logList = ud.getLogs();
			return logList;
		} catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getLog");
		}
	}
	
	/**
	 * 获取所有合同的细节
	 * @return
	 * @throws AppException
	 */
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
	
	/**
	 * 删除合同
	 * @param conId
	 * @return
	 * @throws AppException
	 */
	public boolean deleteCon(int conId) throws AppException{
		try{
		return contractDao.setDel(conId);
		}catch (AppException e) {
			e.printStackTrace();
			throw new AppException("service.ContractService.getConBusis");
		}
	}
	
	/**
	 * 获取合同简略信息，包括（id、名称、起草时间）
	 * @param conId
	 * @return
	 * @throws AppException
	 */
	public ConBusiModel getConBusiModel(int conId) throws AppException{
		Contract contract = contractDao.getById(conId);
		ConState conState = conStateDao.getConState(conId, Constant.STATE_DRAFTED);
		ConBusiModel conBusiModel = new ConBusiModel();
		if (contract != null) {
			conBusiModel.setConId(contract.getId());
			conBusiModel.setConName(contract.getName());
		}
		if (conState != null) {
			conBusiModel.setDrafTime(conState.getTime()); 
		}
		return conBusiModel;
		
	}
}
