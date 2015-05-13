package com.directv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class Batch {

    @Bean
    public ItemReader<Project> reader() {
        FlatFileItemReader<Project> reader = new FlatFileItemReader<Project>();
        reader.setResource(new ClassPathResource("ProjectInfoSharePoint_1.csv"));
        reader.setLineMapper(new DefaultLineMapper<Project>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] {"wbs", "summary", "description",
            			"technicalLead", "programManager","projectManager",
            			"projectStartDate", "projectFinishDate","pdrDate","cdrDate", "inServiceDate", "keyRelease","siteUrl","priority"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Project>() {{
                setTargetType(Project.class);
            }});
        }});
        return reader;
    }

    
    @Bean
    public ItemWriter<Project> writer() {
        return new ItemWriter<Project>(){

			@Override
			public void write(List<? extends Project> items) throws Exception {
				BasicCredentials creds = new BasicCredentials("457172", "Tolivia000");
		        JiraClient jira = new JiraClient("http://jirctsdv-msdc01.ds.dtveng.net:8080", creds);
				//Issue issue;
				
				for(Project item : items){
					//issue = jira.getIssue(item.getIssue("PRJ-256");
					System.out.println("WBS: " + item.getWbs());
					Issue.SearchResult sr = jira.searchIssues("wbs ~ "+item.getWbs());
					  System.out.println("WBS: " + sr.total);
					  if (sr.issues.size() > 1){ 
						  System.out.println("Duplicate Record for : " + item.getWbs());
					  }
					  else if (sr.issues.size() == 0){ 
						  //Create new Project
						  System.out.println("Project not in Jira: " + item.getWbs());
					  }
					  else{//Edit existing Project
						  final Issue issue = sr.issues.get(0);
				          issue.update().field(Field.ASSIGNEE, "457172").execute();
				          issue.update().field(Field.LABELS, new ArrayList() {{
				        	  addAll(issue.getLabels());
				        	  add("BAU");}}).execute();
				          issue.update().field(Field.PRIORITY, "Medium").execute();
						  System.out.println("Result: " + issue);
					  }
				}
				//BasicCredentials creds = new BasicCredentials("XXX", "atitelovoydicir");
				//BasicCredentials creds = new BasicCredentials("457172", "Tolivia000");
		        //JiraClient jira = new JiraClient("http://jirctsdv-msdc01.ds.dtveng.net:8080", creds);
				//Issue issue = jira.getIssue("PRJ-256");
				//issue.update().field(Field.SUMMARY, "Tolivia ye lo meyor").execute();
	            //issue.update().field(Field.PRIORITY, "Critical").execute();
	            //issue.update().field(Field.ASSIGNEE, "457172").execute();
				//.fieldRemove(Field.LABELS, "foo")
	            //.execute();
		        //System.out.println(issue);
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
            ItemWriter<Project> writer) {
        return stepBuilderFactory.get("step1")
        		.allowStartIfComplete(true)
                .<Project, Project> chunk(10)
                .reader(reader)
                //.processor(processor);
                .writer(writer)
                .build();
    }


}
