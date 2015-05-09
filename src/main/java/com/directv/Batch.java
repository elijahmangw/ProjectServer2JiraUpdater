package com.directv;

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
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Project>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "id", "name" });
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
				/*for(Project item : items){
					System.out.println("Sending to JIRA "+item);
				}*/
				BasicCredentials creds = new BasicCredentials("batman", "pow! pow!");
		        JiraClient jira = new JiraClient("https://jira.example.com/jira", creds);
				 Issue issue = jira.getIssue("TEST-123");
				 issue.update()
	                .fieldAdd(Field.LABELS, "baz")
	                .fieldRemove(Field.LABELS, "foo")
	                .execute();
		        System.out.println(issue);
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
                .writer(writer)
                .build();
    }


}
