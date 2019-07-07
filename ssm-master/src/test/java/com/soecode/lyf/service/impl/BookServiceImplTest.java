package com.soecode.lyf.service.impl;

import com.soecode.lyf.BaseTest;
import com.soecode.lyf.dto.AppointExecution;
import com.soecode.lyf.entity.People;
import com.soecode.lyf.service.BookService;
import org.junit.Test;

import javax.annotation.Resource;

public class BookServiceImplTest extends BaseTest {

	@Resource
	private BookService bookService;

	@Test
	public void testAppoint() throws Exception {
		long bookId = 1001;
		long studentId = 12345678910L;
		AppointExecution execution = bookService.appoint(bookId, studentId);
		System.out.println(execution);
	}

	@Test
	public void testBookId() throws Exception {
		long bookId = 1001;
		bookService.getById(bookId);
	}

	@Test
	public void test() throws Exception {
		People people = new People();
		people.setAge(19);
		people.setPhone("151889009");
		people.setSex(3);
		people.setName("张三");

		bookService.test(people);
	}

}
