package com.directv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Field.ValueTuple;
import net.rcarz.jiraclient.Field.ValueType;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.Priority;
import net.rcarz.jiraclient.Status;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class Batch {
	
	@Value("${com.directv.priorityfield}")
	String priorityfield;
	@Value("${com.directv.wbsfield}")
	String wbsfield;
	@Value("${com.directv.projecturl}")
	String projecturl;
	@Value("${com.directv.projectmgr}")
	String projectmgr;
	@Value("${com.directv.techlead}")
	String techlead;
	@Value("${com.directv.inservicedate}")
	String inservicedate;
	@Value("${com.directv.cpvdate}")
	String cpvdate;
	@Value("${com.directv.cdrdate}")
	String cdrdate;
	@Value("${com.directv.pdrdate}")
	String pdrdate;
	@Value("${com.directv.startdate}")
	String startdate;
	@Value("${com.directv.enddate}")
	String enddate;
	@Value("${com.directv.url}")
	String url;	
	@Value("${com.directv.usuario}")
	String usuario;
	@Value("${com.directv.contra}")
	String contrasena;
	String username;

	SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	/*Not needed since Jira priorities changed to be same as in EPMO: HP, P2, P3, P4
	 * Map<String,String> priorities = new HashMap<String,String>(){{
		put("HP","Critical");
		put("P1","High");
		put("P2","Medium");
		put("P3","Low");
	}};*/
	
    @Bean
    public ItemReader<Project> reader() {
        FlatFileItemReader<Project> reader = new FlatFileItemReader<Project>();
      /*
       * The idea is make the reader connecting directly by REST APIs to Project Server.
       */
       //**PROD**
        //reader.setResource(new ClassPathResource("ExecProj.csv"));
        //**DEV ENV**
        //String pathrsc = new String("\\\\common1\\common\\TEMPLATE\\ExecProj_150520.csv");
        //﻿Finance_Charge_Code,ProjectName,Project_Status_Summary,Project_Description,
        //Technical_Lead,Program_Manager,ProjectOwnerName,ProjectStartDate,ProjectFinishDate,PDR_Finish,CDR_Finish,
        //Client_HE_Release,Key_Release,Project_Priority,ProjectWorkspaceInternalHRef

        String pathrsc = new String("ExecutionProjectDetails150612.csv");
        reader.setResource(new ClassPathResource(pathrsc));
        reader.setLineMapper(new DefaultLineMapper<Project>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                /*setNames(new String[] {"wbs", "summary", "epmosummary", "description",
            			"technicalLead", "programManager","projectManager",
            			"projectStartDate", "projectFinishDate","pdrDate","cdrDate", "inServiceDate", "keyRelease",
            			"priority","siteUrl"});*/
            	setNames(new String[] {"wbs", "summary", "description",
            			"technicalLead", "programManager","projectManager",
            			"projectStartDate", "projectFinishDate","pdrDate","cdrDate", "inServiceDate", "keyRelease",
            			"priority","siteUrl"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Project>() {{
                setTargetType(Project.class);
            }});
        }});
        return reader;
    }

    
    @Bean
    public ItemProcessor<Project,Project> processor() {
    	return new ItemProcessor<Project,Project>(){
    	    @Override
    	    public Project process(final Project project) throws Exception {
    	    	//Project otroproj = new Project();
    	    	//project.setPriority(project.getPriority());
    	    	project.setSummary(project.getSummary().trim());
    	    	//--project.setEpmosummary(project.getEpmosummary().trim());
    	    	project.setDescription(project.getDescription().trim());
    	    	
    	    	if (project.getSiteUrl().isEmpty())
    	    		project.setDescription(project.getDescription() + "\n\r" + URIUtil.encodeQuery("http://blank"));
    	    	else
    	    		project.setDescription(project.getDescription() + "\n\r" + URIUtil.encodeQuery(project.getSiteUrl()));
    	    	//Quering Active Directory for username/userid
    	    	//Needed to know the domain
    	    	//username = do the LDAP query here from project.getprojectmanager()  and techlead
    	    	project.setProjectManager(project.getProjectManager());
    	    	project.setTechnicalLead(project.getTechnicalLead());
    	    	return project;
    	    }
    	};
    }
    
    @Bean
    public ItemWriter<Project> writer() {
        return new ItemWriter<Project>(){

			@Override
			public void write(List<? extends Project> items) throws Exception {
				//**DEV ENV**
				BasicCredentials creds = new BasicCredentials(usuario, contrasena);
				JiraClient jira = new JiraClient(url, creds);
				//JiraClient jira = new JiraClient("http://jirctsdv-msdc01.ds.dtveng.net:8080", creds);
				
				//**PROD ENV**
				//BasicCredentials creds = new BasicCredentials("jira2psintegration", "Directv2015");
				//JiraClient jira = new JiraClient("http://jirapppr-labc01.ds.dtvops.net", creds);
				
				//creamos el bufer de salida
			    StringBuffer bufferExitos = new StringBuffer();
			    StringBuffer bufferFallos = new StringBuffer();
			    bufferExitos.append("*******JIRA-PROJECT SERVER SYNCRONIZER*******");
			    bufferExitos.append("\n\r"); 
			    bufferExitos.append("[UPDATE STARTED]-START TIME: " + new Date() + " ");
			    bufferFallos.append("[UPDATE STARTED]-START TIME: " + new Date() + " ");
			    bufferExitos.append("\n\r");
			    bufferFallos.append("\n\r");
			    
			    
			    /* Create a new issue. For later when we have PRJ in ProjectServer */
	            /*Issue newIssue = jira.createIssue("TEST", "Bug")
	                .field(Field.SUMMARY, "Bat signal is broken")
	                .field(Field.DESCRIPTION, "Commissioner Gordon reports the Bat signal is broken.")
	                .field(Field.REPORTER, "batman")
	                .field(Field.ASSIGNEE, "robin")
	                .execute();
	            System.out.println(newIssue);
	            */
		        for(Project item : items){
					System.out.println("WBS=" + item.getWbs());
					if ((item.getWbs() != null) && !(item.getWbs().isEmpty())){
						Issue.SearchResult sr = jira.searchIssues("wbs ~ " + item.getWbs());
					    System.out.println(" (0) :: " + sr.total + " Projects with WBS= " + item.getWbs());
						  if (sr.issues.size() > 1){ 
							  System.out.println(" (1) :: Duplicate Entry for Project: " + item.getWbs());
							  bufferFallos.append(" " + sr.total + " Projects with WBS as : " + item.getWbs() + " ");
							  bufferFallos.append("\n\r");
							  bufferFallos.append("[DUPLICATED]-Duplicate Entry for Project: " + item.getWbs() + " ");
							  bufferFallos.append("\n\r");
						  }
						  else if (sr.issues.size() == 0){ 
							  //Create new Project
							  System.out.println(" (2) :: Project Not Created in Jira: " + item.getWbs() + ": " + item.getSummary());
							  bufferFallos.append("[ERROR]-PROJECT NOT FOUND IN JIRA with WBS=" + item.getWbs() + ": " + item.getSummary() + " ");
							  bufferFallos.append("\n\r");
						  }
						  else{//Edit existing Project
							  final Issue issue = sr.issues.get(0);
							  //System.out.println("[STATUS]-" + issue.getStatus().getDescription());
							  System.out.println("[STATUS]-" + issue.getStatus());
					          if (!issue.getStatus().toString().toUpperCase().equals("CLOSED"))
					          {
					        	  //Getting current issue priority, to compare and don't change in case equals
					        	  String prevprior = ((JSONObject)(issue.getField(priorityfield))).get("value").toString();
					        	  System.out.println("Previous Priority: " + prevprior);
					        	  ValueTuple valorP;
					        	  if ((item.getPriority() != null) && (!item.getPriority().isEmpty()))
					        		  valorP = new ValueTuple(ValueType.VALUE, item.getPriority());
					              else
					        		  valorP = new ValueTuple(ValueType.VALUE, "P4");
					        	  
					        	  System.out.println(item.getSiteUrl());
					        	  String urlsite = URIUtil.encodeQuery(item.getSiteUrl());
					        	  if (urlsite.isEmpty()) urlsite = new String("http://blank");
					        	  System.out.println(urlsite);
					        	  //***Fields updates - We need to do 1by1 so we can compare previous value and update only if changed
					        	  //Getting current issue priority, to compare and don't change in case equals
					        	  //JSONObject prioridad = (JSONObject)issue.getField(priorityfield);
					        	  //String valora = (String)prioridad.get("value");
					        	  					        	  
					        	  if (!(valorP.value.toString().trim().equals(prevprior.trim()))){
					        		  issue.update().field(priorityfield, valorP).execute();   		//priority update
						        	  System.out.println(" (3) :: Project " + issue.getKey() + " Priority Updated " + prevprior + " to: " + item.getPriority());
						        	  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " Priority Updated from " + prevprior + " to: " + item.getPriority());
						        	  bufferExitos.append("\n\r");
					        	  }
					        	  if (!(item.getSummary().toString().trim().equals(issue.getSummary().toString().trim()))){
					        		  issue.update().field(Field.SUMMARY, item.getSummary()).execute();   	//summary update
						        	  System.out.println(" (4) :: Project " + issue.getKey() + " Name Updated from " + issue.getSummary() + "  to: " + item.getSummary());
						        	  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " Name Updated from " + issue.getSummary() + "  to: " + item.getSummary());
						        	  bufferExitos.append("\n\r");
					        	  }
					        	  if (!(item.getProjectManager().toString().trim().equals(issue.getField(projectmgr).toString().trim()))){
					        		  issue.update().field(projectmgr, item.getProjectManager()).execute();   	//project manager update
					        		  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " Project Manager Updated from " + issue.getField(projectmgr).toString() + " to: " + item.getProjectManager());
						        	  System.out.println("[UPDATED]-Project " + issue.getKey() + " Project Manager Updated from " + issue.getSummary() + "  to: " + item.getSummary());
						        	  bufferExitos.append("\n\r");
					        	  }
					        	  if (!(item.getTechnicalLead().toString().trim().equals(issue.getField(techlead).toString().trim()))){
					        		  issue.update().field(techlead, item.getTechnicalLead()).execute();   	//project manager update
					        		  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " Technical Lead Updated from " + issue.getField(techlead).toString() + "  to: " + item.getTechnicalLead());
						        	  System.out.println(" (6) :: Project " + issue.getKey() + " Technical Lead Updated from " + issue.getField(techlead).toString() + "  to: " + item.getTechnicalLead());
						        	  bufferExitos.append("\n\r");
					        	  }
					        	  if (!(urlsite.trim().equals(issue.getField(projecturl).toString().trim()))){
					        		  issue.update().field(projecturl, urlsite).execute();   	//project manager update
					        		  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " Project URL Updated from " + issue.getField(projecturl).toString() + "  to: " + urlsite);
						        	  System.out.println(" (7) :: Project " + issue.getKey() + " Project URL Updated from " + issue.getField(projecturl).toString() + "  to: " + urlsite);
						        	  bufferExitos.append("\n\r");
					        	  }
					        	  /*issue.update().field(priorityfield, valorP)		//priority update
					        	  	.field(Field.SUMMARY, item.getSummary())
					        	  	.field(projectmgr, item.getProjectManager())
					        	  	.field(techlead, item.getTechnicalLead())
					        	  	.field(projecturl, urlsite).execute();
					        	  */
					        	  //.field(Field.DESCRIPTION, item.getDescription()).execute();
					        	  	
					        	  if ((item.getInServiceDate() != null) && (!item.getInServiceDate().isEmpty()))
					        		  if (!(item.getInServiceDate().equals(issue.getField(inservicedate)))){
					        			  issue.update().field(inservicedate, dateformat.parse(item.getInServiceDate())).execute();
					        			  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " In-Service Date Updated from " + issue.getField(inservicedate).toString() + " to: " + item.getInServiceDate());
							        	  System.out.println(" (8) :: Project " + issue.getKey() + " In-Service Date Updated from " + issue.getField(inservicedate).toString() + " to: " + item.getInServiceDate());
							        	  bufferExitos.append("\n\r");
					        		  }
					        	  else{ 
						        	  bufferFallos.append("[WARNING]-PROJECT INSERVICEDATE IS NULL=" + item.getWbs());
					        	  	  bufferFallos.append("\n\r");
					        	  }
					        	  if ((item.getCdrDate() != null) && (!item.getCdrDate().isEmpty()))
					        		  if (!(item.getCdrDate().equals(issue.getField(cdrdate)))){
					        			  issue.update().field(cdrdate, dateformat.parse(item.getCdrDate())).execute();
					        			  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " CDR Date Updated from " + issue.getField(cdrdate).toString() + " to: " + item.getCdrDate());
							        	  System.out.println(" (9) :: Project " + issue.getKey() + " CDR Date Updated from " + issue.getField(cdrdate).toString() + " to: " + item.getCdrDate());
							        	  bufferExitos.append("\n\r");
					        		  }
					        	  else{ 
					        		  bufferFallos.append("[WARNING]-PROJECT CDR DATE IS NULL=" + item.getWbs());
					        	  	  bufferFallos.append("\n\r");
					          	  }
					        	  if ((item.getPdrDate() != null) && (!item.getPdrDate().isEmpty()))
					        		  if (!(item.getPdrDate().equals(issue.getField(pdrdate)))){
					        			  issue.update().field(pdrdate, dateformat.parse(item.getPdrDate())).execute();
					        			  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " PDR Date Updated from " + issue.getField(pdrdate).toString() + " to: " + item.getPdrDate());
							        	  System.out.println(" (10) :: Project " + issue.getKey() + " PDR Date Updated from " + issue.getField(pdrdate).toString() + " to: " + item.getPdrDate());
							        	  bufferExitos.append("\n\r");
					        		  }
					        	  else{
					        		  bufferFallos.append("[WARNING]-PROJECT PDR DATE IS NULL=" + item.getWbs());
					        		  bufferFallos.append("\n\r");
					        	  }
					        	  if ((item.getProjectStartDate() != null) && (!item.getProjectStartDate().isEmpty()))
					        		  if (!(item.getProjectStartDate().equals(issue.getField(startdate)))){
					        			  issue.update().field(startdate, dateformat.parse(item.getProjectStartDate())).execute();
					        			  bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " Start Date Updated from " + issue.getField(startdate).toString() + " to: " + item.getProjectStartDate());
							        	  System.out.println(" (11) :: Project " + issue.getKey() + " Start Date Updated from " + issue.getField(startdate).toString() + " to: " + item.getProjectStartDate());
							        	  bufferExitos.append("\n\r");
					        		  }
					        	  else{
					        		  bufferFallos.append("[WARNING]-PROJECT START DATE IS NULL=" + item.getWbs());
					        		  bufferFallos.append("\n\r");
					        	  }
					        	  if ((item.getProjectFinishDate() != null) && (!item.getProjectFinishDate().isEmpty()))
					        		  if (!(item.getProjectFinishDate().equals(issue.getField(enddate)))){
					        			  	issue.update().field(enddate, dateformat.parse(item.getProjectFinishDate())).execute();
						        			bufferExitos.append("[UPDATED]-Project " + issue.getKey() + " End Date Updated from " + issue.getField(enddate).toString() + " to: " + item.getProjectFinishDate());
								        	System.out.println(" (12) :: Project " + issue.getKey() + " End Date Updated from " + issue.getField(enddate).toString() + " to: " + item.getProjectFinishDate());
								        	bufferExitos.append("\n\r");
					        		  }
					        	  else{ 
					        		  bufferFallos.append("[WARNING]-PROJECT END DATE IS NULL=" + item.getWbs());
					        		  bufferFallos.append("\n\r");
					        	  }					        	  
					          }
					          else{
					        	  bufferFallos.append("[ERROR]-PROJECT IS CLOSED=" + item.getWbs() + "-" + issue.getKey());
					        	  bufferFallos.append("\n\r");
					          }
						  }
					}
					else{
						System.out.println("[ERROR]-WBS FIELD EMPTY= " + item.getSummary());
						bufferFallos.append("[ERROR]-WBS FIELD EMPTY= " + item.getSummary());
						bufferFallos.append("\n\r");
						
					}
		        }
			    bufferExitos.append("[UPDATE ENDED]-END TIME: " + new Date() + " ");
			    bufferExitos.append("\n\r");
			    bufferFallos.append("[UPDATE ENDED]-END TIME: " + new Date() + " ");
			    bufferFallos.append("\n\r");
				BufferedWriter writerlogros = new BufferedWriter( new FileWriter("logros.log"));
				BufferedWriter writerfallos = new BufferedWriter( new FileWriter("fallos.log"));
				writerlogros.write(bufferExitos.toString());
				writerlogros.newLine();
				writerfallos.write(bufferFallos.toString());
				writerfallos.newLine();
				writerlogros.flush();
				writerfallos.flush();
				writerlogros.close();
				writerfallos.close();
			}
        };
    }

    @Bean
    public Job updateJira(JobBuilderFactory jobs, Step s1) {
        return jobs.get("updateJira")
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Project> reader,
            ItemWriter<Project> writer, ItemProcessor<Project,Project> processor) {
        return stepBuilderFactory.get("step1")
        		.allowStartIfComplete(true)
                .<Project, Project> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
