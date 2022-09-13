package com.ssongman.batch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssongman.domain.Dept;
import com.ssongman.domain.Dept2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob2 {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	
	private int chunkSize = 10;
	
	@Bean
	public Job JpaPageJob2_batchBuild() {
		return jobBuilderFactory.get("JpaPageJob2")
				.start(JpaPageJob2_step1())
				.build();
	}
	
	@Bean
	public Step JpaPageJob2_step1() {
		return stepBuilderFactory.get("JpaPageJob2_step1")
				.<Dept, Dept2>chunk(chunkSize)
				.reader(JpaPageJob2_dbItemReader())
				.processor(JpaPageJob2_processor())
				.writer(jpaPageJob2_printItemWriter())
				.build();
		
	}

	private ItemProcessor<Dept, Dept2> JpaPageJob2_processor() {
		log.debug("ItemProcessor");
		// TODO Auto-generated method stub
		return dept -> {
			return new Dept2(dept.getDeptNo(), "New_" + dept.getDName(), "New_" + dept.getLoc());
		};
	}

	@Bean
	public JpaPagingItemReader<Dept> JpaPageJob2_dbItemReader() {
		log.debug("JpaPagingItemReader");
		return new JpaPagingItemReaderBuilder<Dept>()
				.name("JpaPageJob2_dbItemReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(chunkSize)
				.queryString("select d from Dept d order by dept_no asc")
				.build();
	}
	
//	@Bean
//	public JpaItemWriter<Dept2> JpaPageJob2_dbItemWriter() {
//		JpaItemWriter<Dept2> jpaItemWriter = new JpaItemWriter<>();
//		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
//		log.debug("itemWriter");
//		return jpaItemWriter;
//	}

	@Bean
	public ItemWriter<Dept2> jpaPageJob2_printItemWriter() {
		log.debug("ItemWriter");
		return list -> {
			for(Dept2 dept: list) {
				log.debug(dept.toString());
			}
		};
	}
	

}
