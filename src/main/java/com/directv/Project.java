package com.directv;


/*
 * Public class to encapsulate the Project object information coming from ProjectServer
 * and populating it after transformation to Jira Global Project
 */
public class Project {

	//information from ProjectServer site/report
	private String wbs; //Finance_Charge_Code
	private String summary; //ProjectName;
	private String epmosummary; //EPMO Summary;
	private String description;
	private String technicalLead;
	private String programManager;
	private String projectManager;
	private String projectStartDate;
	private String projectFinishDate;
	private String pdrDate; //PDR_Finish;
	private String cdrDate; //CDR_Finish;
	private String inServiceDate; //Client_InService_Release;
	private String keyRelease;
	private String siteUrl;
	private String priority;

    public Project() {
    	
    }

	public Project(String wbs, String summary, String description,
			String technicalLead, String programManager, String projectManager,
			String projectStartDate, String projectFinishDate, String pdrDate,
			String cdrDate, String inServiceDate, String keyRelease, String siteUrl,
			String priority) {
		super();
		this.wbs = wbs;
		this.summary = summary;
		this.description = description;
		this.technicalLead = technicalLead;
		this.programManager = programManager;
		this.projectManager = projectManager;
		this.projectStartDate = projectStartDate;
		this.projectFinishDate = projectFinishDate;
		this.pdrDate = pdrDate;
		this.cdrDate = cdrDate;
		this.inServiceDate = inServiceDate;
		this.keyRelease = keyRelease;
		this.siteUrl = siteUrl;
		this.priority = priority;
	}

	/**
	 * @return the wbs
	 */
	public String getWbs() {
		return wbs;
	}

	
	/**
	 * @param wbs the wbs to set
	 */
	public void setWbs(String wbs) {
		this.wbs = wbs;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the epmosummary
	 */
	/*
	public String getEpmosummary() {
		return epmosummary;
	}
	*/
	/**
	 * @param epmosummary the epmosummary to set
	 */
	/*public void setEpmosummary(String epmosummary) {
		this.epmosummary = epmosummary;
	}*/


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the technicalLead
	 */
	public String getTechnicalLead() {
		return technicalLead;
	}

	/**
	 * @param technicalLead the technicalLead to set
	 */
	public void setTechnicalLead(String technicalLead) {
		this.technicalLead = technicalLead;
	}

	/**
	 * @return the programManager
	 */
	public String getProgramManager() {
		return programManager;
	}

	/**
	 * @param programManager the programManager to set
	 */
	public void setProgramManager(String programManager) {
		this.programManager = programManager;
	}

	/**
	 * @return the projectManager
	 */
	public String getProjectManager() {
		return projectManager;
	}

	/**
	 * @param projectManager the projectManager to set
	 */
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	/**
	 * @return the projectStartDate
	 */
	public String getProjectStartDate() {
		return projectStartDate;
	}

	/**
	 * @param projectStartDate the projectStartDate to set
	 */
	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	/**
	 * @return the projectFinishDate
	 */
	public String getProjectFinishDate() {
		return projectFinishDate;
	}

	/**
	 * @param projectFinishDate the projectFinishDate to set
	 */
	public void setProjectFinishDate(String projectFinishDate) {
		this.projectFinishDate = projectFinishDate;
	}

	/**
	 * @return the pdrDate
	 */
	public String getPdrDate() {
		return pdrDate;
	}

	/**
	 * @param pdrDate the pdrDate to set
	 */
	public void setPdrDate(String pdrDate) {
		this.pdrDate = pdrDate;
	}

	/**
	 * @return the cdrDate
	 */
	public String getCdrDate() {
		return cdrDate;
	}

	/**
	 * @param cdrDate the cdrDate to set
	 */
	public void setCdrDate(String cdrDate) {
		this.cdrDate = cdrDate;
	}

	/**
	 * @return the inServiceDate
	 */
	public String getInServiceDate() {
		return inServiceDate;
	}

	/**
	 * @param inServiceDate the inServiceDate to set
	 */
	public void setInServiceDate(String inServiceDate) {
		this.inServiceDate = inServiceDate;
	}

	/**
	 * @return the keyRelease
	 */
	public String getKeyRelease() {
		return keyRelease;
	}

	/**
	 * @param keyRelease the keyRelease to set
	 */
	public void setKeyRelease(String keyRelease) {
		this.keyRelease = keyRelease;
	}

	/**
	 * @return the siteUrl
	 */
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * @param siteUrl the siteUrl to set
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	  /* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
	@Override
	public String toString() {
		return "Project [wbs=" + wbs + ", summary=" + summary
				+ ", description=" + description + ", technicalLead="
				+ technicalLead + ", programManager=" + programManager
				+ ", projectManager=" + projectManager + ", projectStartDate="
				+ projectStartDate + ", projectFinishDate=" + projectFinishDate
				+ ", pdrDate=" + pdrDate + ", cdrDate=" + cdrDate
				+ ", inServiceDate=" + inServiceDate + ", keyRelease="
				+ keyRelease + ", siteUrl=" + siteUrl + ", priority="
				+ priority + ", toString()=" + super.toString() + "]";
		}
 

}
