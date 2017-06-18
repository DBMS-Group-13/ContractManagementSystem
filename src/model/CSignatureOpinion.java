package model;


public class CSignatureOpinion {

	private int conId; 			// Contract id
	private String csOperator; 	// Countersign operator
	private String opinion;		// Countersign opinion
	
	
	public CSignatureOpinion() {
		this.conId = 0;
		this.csOperator = "";
		this.opinion = "";
	}

	
	public int getConId() {
		return conId;
	}

	public void setConId(int conId) {
		this.conId = conId;
	}

	public String getCsOperator() {
		return csOperator;
	}

	public void setCsOperator(String csOperator) {
		this.csOperator = csOperator;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
}
