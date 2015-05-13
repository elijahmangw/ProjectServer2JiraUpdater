package com.directv;

import java.util.Date;

/*
 * Public class to encapsulate the Project object information coming from ProjectServer
 * and populating it after transformation to Jira Global Project
 */
public class Project {
    //information from the sp site/report
	private String WBS; //Finance_Charge_Code
	private String Summary; //ProjectName;
	private String Description;
	private String Technical_Lead;
	private String Program_Manager;
	private String Project_Manager;
	private Date ProjectStartDate;
	private Date ProjectFinishDate;
	private Date PDRDate; //PDR_Finish;
	private Date CDRDate; //CDR_Finish;
	private Date InServiceDate; //Client_InService_Release;
	private Date Key_Release;
	private String Site_URL;
	private String Priority;

    public Project() {
    	
    }

    public Project(String ChargeCode, String projname, String descr, String TL, String PgM, String projmgr, Date PRJStart, Date PRJEnd, Date PDR, Date CDR,
    					Date InSvcDate, Date KeyRelease, String URL, String Priority) {
    	this.WBS = ChargeCode;
    	this.Summary = projname;
    	this.Description = descr;
    	this.Technical_Lead = TL;
    	this.Program_Manager = PgM;
    	this.Project_Manager = projmgr;
    	this.ProjectStartDate = PRJStart; 
    	this.ProjectFinishDate = PRJEnd;
    	this.PDRDate = PDR;
    	this.CDRDate = CDR;
    	this.InServiceDate = InSvcDate;
    	this.Key_Release = KeyRelease;
    	this.Site_URL = URL;
    	this.Priority = Priority;
    }

    /**
	 * @return the wBS
	 */
	public String getWBS() {
		return WBS;
	}

	/**
	 * @param wBS the wBS to set
	 */
	public void setWBS(String wBS) {
		WBS = wBS;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return Summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		Summary = summary;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * @return the technical_Lead
	 */
	public String getTechnical_Lead() {
		return Technical_Lead;
	}

	/**
	 * @param technical_Lead the technical_Lead to set
	 */
	public void setTechnical_Lead(String technical_Lead) {
		Technical_Lead = technical_Lead;
	}

	/**
	 * @return the program_Manager
	 */
	public String getProgram_Manager() {
		return Program_Manager;
	}

	/**
	 * @param program_Manager the program_Manager to set
	 */
	public void setProgram_Manager(String program_Manager) {
		Program_Manager = program_Manager;
	}

	/**
	 * @return the project_Manager
	 */
	public String getProject_Manager() {
		return Project_Manager;
	}

	/**
	 * @param project_Manager the project_Manager to set
	 */
	public void setProject_Manager(String projmgr) {
		Project_Manager = projmgr;
	}

	/**
	 * @return the projectStartDate
	 */
	public Date getProjectStartDate() {
		return ProjectStartDate;
	}

	/**
	 * @param projectStartDate the projectStartDate to set
	 */
	public void setProjectStartDate(Date projectStartDate) {
		ProjectStartDate = projectStartDate;
	}

	/**
	 * @return the projectFinishDate
	 */
	public Date getProjectFinishDate() {
		return ProjectFinishDate;
	}

	/**
	 * @param projectFinishDate the projectFinishDate to set
	 */
	public void setProjectFinishDate(Date projectFinishDate) {
		ProjectFinishDate = projectFinishDate;
	}

	/**
	 * @return the pDRDate
	 */
	public Date getPDRDate() {
		return PDRDate;
	}

	/**
	 * @param pDRDate the pDRDate to set
	 */
	public void setPDRDate(Date pDRDate) {
		PDRDate = pDRDate;
	}

	/**
	 * @return the cDRDate
	 */
	public Date getCDRDate() {
		return CDRDate;
	}

	/**
	 * @param cDRDate the cDRDate to set
	 */
	public void setCDRDate(Date cDRDate) {
		CDRDate = cDRDate;
	}

	/**
	 * @return the inServiceDate
	 */
	public Date getInServiceDate() {
		return InServiceDate;
	}

	/**
	 * @param inServiceDate the inServiceDate to set
	 */
	public void setInServiceDate(Date inServiceDate) {
		InServiceDate = inServiceDate;
	}

	/**
	 * @return the key_Release
	 */
	public Date getKey_Release() {
		return Key_Release;
	}

	/**
	 * @param key_Release the key_Release to set
	 */
	public void setKey_Release(Date key_Release) {
		Key_Release = key_Release;
	}

	/**
	 * @return the site_URL
	 */
	public String getSite_URL() {
		return Site_URL;
	}

	/**
	 * @param site_URL the site_URL to set
	 */
	public void setSite_URL(String site_URL) {
		Site_URL = site_URL;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return Priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		Priority = priority;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Project [WBS=" + WBS + ", Summary=" + Summary
				+ ", Description=" + Description + ", Technical_Lead="
				+ Technical_Lead + ", Program_Manager=" + Program_Manager
				+ ", Project_Manager=" + Project_Manager
				+ ", ProjectStartDate=" + ProjectStartDate
				+ ", ProjectFinishDate=" + ProjectFinishDate + ", PDRDate="
				+ PDRDate + ", CDRDate=" + CDRDate + ", InServiceDate="
				+ InServiceDate + ", Key_Release=" + Key_Release
				+ ", Site_URL=" + Site_URL + ", Priority=" + Priority
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
