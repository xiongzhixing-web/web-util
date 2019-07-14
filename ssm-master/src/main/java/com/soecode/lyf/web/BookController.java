package com.soecode.lyf.web;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import com.soecode.lyf.annotation.DocAnnotation;
import com.soecode.lyf.entity.People;
import com.soecode.lyf.vo.BookVo;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.soecode.lyf.dto.AppointExecution;
import com.soecode.lyf.dto.Result;
import com.soecode.lyf.entity.Book;
import com.soecode.lyf.enums.AppointStateEnum;
import com.soecode.lyf.exception.NoNumberException;
import com.soecode.lyf.exception.RepeatAppointException;
import com.soecode.lyf.service.BookService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/book") // url:/模块/资源/{id}/细分 /seckill/list
public class BookController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookService bookService;

	@RequestMapping(method=RequestMethod.POST,value = "/list")
	@ResponseBody
	@DocAnnotation(comment="查询列表方法")
	public Result<List<Book>> list(@RequestBody @Validated BookVo book,BindingResult result) {
		Result<List<Book>> res = new Result<>();
		if(result.hasErrors()){
			res.setErrMessage(result.getAllErrors().stream().filter(error -> error != null).map(error -> error.getDefaultMessage()).collect(Collectors.toList()).toString());
			return res;
		}
		List<Book> list = bookService.getList();
		res.setData(list);
		return res;
	}

	@DocAnnotation(comment = "查询详情方法")
	@RequestMapping(method = RequestMethod.GET,value = "/detail")
	@ResponseBody
	private Result<Book> detail(@RequestParam(value = "bookId") @NotNull(message = "bookId不能为空") Long bookId) {
		Book book = bookService.getById(bookId);
		return Result.success(book);
	}

	// ajax json
	@RequestMapping(value = "/{bookId}/appoint")
	@ResponseBody
	@DocAnnotation(comment="订阅书的方法")
	private Result<List<AppointExecution>> appoint(@PathVariable("bookId") Long bookId, @RequestParam("studentId") Long studentId) {
		if (studentId == null || studentId.equals("")) {
			return null;
		}
		AppointExecution execution = null;
		try {
			execution = bookService.appoint(bookId, studentId);
		} catch (NoNumberException e1) {
			execution = new AppointExecution(bookId, AppointStateEnum.NO_NUMBER);
		} catch (RepeatAppointException e2) {
			execution = new AppointExecution(bookId, AppointStateEnum.REPEAT_APPOINT);
		} catch (Exception e) {
			execution = new AppointExecution(bookId, AppointStateEnum.INNER_ERROR);
		}
		return null;
	}

}
