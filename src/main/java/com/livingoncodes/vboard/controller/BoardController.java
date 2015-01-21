package com.livingoncodes.vboard.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.livingoncodes.vboard.dto.RegistrationForm;
import com.livingoncodes.vboard.entity.Board;
import com.livingoncodes.vboard.repository.BoardRepository;

@Controller
public class BoardController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoardController.class);
	
	@Autowired
	private BoardRepository boardRepository;

	
	@RequestMapping(value = "/{boardName}", method = RequestMethod.GET)
	public String showBoard(@PathVariable("boardName") String boardName, Model model) {
		
		LOGGER.debug("Rendering view for board: " + boardName);
		
		Board board = boardRepository.findByName(boardName); 
		
		if( board == null ) {
			LOGGER.debug("Board not found: " + boardName);
			return "newboard";
		}
		
		model.addAttribute("boardName", boardName);
		model.addAttribute("boardContent", board.getContent());
		
		return "board";
	}

	@RequestMapping(value = "/{boardName}/edit", method = RequestMethod.GET)
	public String editBoard(@PathVariable("boardName") String boardName, Model model) {
		
		LOGGER.debug("Rendering edit view for board: " + boardName);
		
		Board board = boardRepository.findByName(boardName); 
		
		if( board == null ) {
			LOGGER.debug("Board not found: " + boardName);
			return "home";
		}
		
		model.addAttribute("content", board.getContent());

		System.out.println("Editing boaard: " + board);
		
		return "editboard";
	}
	
	@RequestMapping(value ="/{boardName}/edit", method = RequestMethod.POST)
	public String doEditBoard(@PathVariable("boardName") String boardName,
			WebRequest request, RedirectAttributes redirectAttributes, Model model) {

		Board board = boardRepository.findByName(boardName);
		
		if( board == null) {
			LOGGER.debug("Board not found: " + boardName);
			
			return "home";
		}
		
		if( board.getPassword().equals(request.getParameter("password"))) {
			board.setContent(request.getParameter("content"));
			boardRepository.save(board);
			return "redirect:/" + request.getParameter("boardName");
		}
		
		redirectAttributes.addAttribute("error", "Password mismatch");
		return "redirect:/" + request.getParameter("boardName") + "/edit";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerBoard(@Valid RegistrationForm registrationForm,
            BindingResult result, WebRequest request, RedirectAttributes redirectAttributes) {
		
		System.out.println("Boardname: " + request.getParameter("boardName"));
		System.out.println("Password: " + request.getParameter("password"));
		
		if( result.hasErrors() ) {
			System.out.println(result.getAllErrors());
			redirectAttributes.addAttribute("error", "Password should be more than 3 characters and less than 100");
			return "redirect:/" + request.getParameter("boardName");
		}

		Board board = new Board();
		
		board.setName(request.getParameter("boardName"));
		board.setPassword(request.getParameter("password"));
		
		boardRepository.save(board);
		
		return "redirect:/" + request.getParameter("boardName");
	}

}
