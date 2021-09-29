package com.cos.blogapp2.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp2.domain.board.Board;
import com.cos.blogapp2.domain.board.BoardRepository;
import com.cos.blogapp2.domain.user.User;
import com.cos.blogapp2.handler.exception.MyAsyncNotFoundException;
import com.cos.blogapp2.handler.exception.MyNotFoundException;
import com.cos.blogapp2.util.Script;
import com.cos.blogapp2.web.dto.BoardSaveReqDto;
import com.cos.blogapp2.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

	private final BoardRepository boardRepository;
	private final HttpSession session;

	@DeleteMapping("/board/{id}") // 삭제 요청은 비동기 요청(fetch)
	public @ResponseBody CMRespDto deleteById(@PathVariable int id, @Valid @RequestBody BoardSaveReqDto dto, BindingResult bindingResult) {
		
		// 인증이 된 사람만 함수 접근 가능!! (로그인 된 사람)
		User principal = (User) session.getAttribute("principal");

		if (principal == null) { // 로그인 안됨
			throw new MyAsyncNotFoundException("인증이 되지 않았습니다.");
		}
		
		// 권한
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(() -> new MyAsyncNotFoundException("해당 글을 찾을 수가 없습니다."));
		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 글을 삭제할 권한이 없습니다.");
		}		
		
		// 유효성 검사
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		
		try {
			boardRepository.deleteById(id);
		} catch (Exception e) {
			throw new MyAsyncNotFoundException(id + "의 글을 찾을 수 없어 삭제할 수 없습니다.");
		}

		return new CMRespDto(1, "삭제 성공", null);
	}

	@PutMapping("/board/{id}")
	public @ResponseBody CMRespDto<?> update(@PathVariable int id, @RequestBody BoardSaveReqDto dto) { 
		
		// 인증
		User principal = (User) session.getAttribute("principal");

		if (principal == null) { // 로그인 안됨
			throw new MyAsyncNotFoundException("인증이 되지 않았습니다.");
		}
		
		// 권한
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(() -> new MyAsyncNotFoundException("해당 글을 찾을 수가 없습니다."));
		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 글의 작성자가 아닙니다.");
		}

		Board board = dto.toEntity();
		board.setId(id);
		board.setUser(principal);
		boardRepository.save(board);

		return new CMRespDto<>(1, "성공", null);
	}

	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("boardEntity", boardRepository.findById(id).get());
		return "board/updateForm"; // ViewResolver : file의 prefix와 suffix - yml
	}

	@PostMapping("/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) { // title=제목&content=내용

		User principal = (User) session.getAttribute("principal");

		// 인증 체크(공통로직)
		if (principal == null) { // 로그인 안됨
			return Script.href("/loginForm", "잘못된 접근입니다");
		}
		
		// 유효성 검사 - But Board 생성 시 title(제목) 공란 불가 설정하지 않음
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}

		Board board = dto.toEntity();
		board.setUser(principal);
		boardRepository.save(board);
		return "redirect:/";
	}

	@GetMapping("/board")
	public String list(Model model) {

		List<Board> boardsEntity = boardRepository.findAll();
		model.addAttribute("boardsEntity", boardsEntity);

		return "board/list";
	}

	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		// Board boardEntity = boardRepository.findById(id).get(); // get() 옵셔널안에 있는 걸
		// 그대로 꺼내오는것
		// 존재하지 않는 글일 시 상세보기 오류 처리 -> 비동기 처리가 아니므로 MyNotFoundException 처리 & 람다식으로 작성
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(() -> new MyNotFoundException(id + "는 존재하지 않는 글입니다."));
		model.addAttribute("boardEntity", boardEntity);
		return "board/detail";
	}

	@GetMapping("/board/saveForm")
	public String boardSaveForm() {
		return "board/saveForm";
	}

}
