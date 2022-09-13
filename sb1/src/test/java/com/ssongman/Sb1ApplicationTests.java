package com.ssongman;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.ssongman.domain.Dept;
import com.ssongman.domain.DeptRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class Sb1ApplicationTests {

	@Autowired
	DeptRepository deptRepository;

	@Test
	void contextLoads() {
	}
	
	@Test
	@Commit
	public void dept01() {
		log.info("dept01 Test Start");
		for (int i=1; i<101; i++) {
			log.info(String.valueOf(i));
			deptRepository.save(new Dept(i, "dName_" + String.valueOf(i), "loc_" + String.valueOf(i)));
		}
	}

}
