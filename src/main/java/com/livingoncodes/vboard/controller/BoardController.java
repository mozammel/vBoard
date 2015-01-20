package com.livingoncodes.vboard.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BoardController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoardController.class);

	
	@RequestMapping(value = "/{boardName}", method = RequestMethod.GET)
	public String showBoard(@PathVariable("boardName") String boardName, Model model) {
		
		LOGGER.debug("Rendering view for board: " + boardName);
		
		model.addAttribute("boardName", boardName);
		
		return "board";
	}

}
