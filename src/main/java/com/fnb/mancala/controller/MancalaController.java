package com.fnb.mancala.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fnb.mancala.domain.Game;
import com.fnb.mancala.service.BoardService;

@Controller
public class MancalaController {
	
	 @Autowired
	 private BoardService BoardService;
	 
	 private final Game game;
	
	@RequestMapping(value = "/play", method = RequestMethod.POST, produces={"application/json","application/xml"})
    public String performMove(@ModelAttribute final Payload payload, final Model model) {
        int chosenIndex = payload.getIndex();
        boolean isPlayer2Turn = payload.getIsPlayer2Turn();
        int pitListIndex = isPlayer2Turn ? chosenIndex : chosenIndex + game.getOffsetPlayerPlayer1();

        if (game.isPitEmpty(pitListIndex))
            addErrorMessageToModel(model, chosenIndex);
        else
            play(pitListIndex, isPlayer2Turn);

        addGameOverMessageIfGameIsOver(model);
        addAttributesToModel(model);
        return "board";
    }

    private void play(final int pitListIndex, final boolean Player2Turn) {
        game.setPlayer2Turn(Player2Turn);
        game.play(pitListIndex);
    }

    private void addErrorMessageToModel(final Model model, final int index) {
        model.addAttribute("errorMessage", String.format("The chosen pit %s contains no stones "
                + "please select another pit", index + 1));
    }

    private void addGameOverMessageIfGameIsOver(final Model model) {
        if (game.isGameOver())
            model.addAttribute("gameoverMessage", game.getWinnerMessage());
    }

   

}
