package com.fnb.mancala.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public final class Game {

    private BoardService board;

    public void play(final int index) {
        board.makeMove(index);
    }

    public String getWinnerMessage() {
        int exceedingStonesPlayer2 = getStonesKalahaPlayer2() - getStonesKalahaPlayer1();

        if (exceedingStonesPlayer2 == 0)
            return "It's a tie!";

        return exceedingStonesPlayer2 > 0 ? "Player Player2 has won!" : "Player Player1 has won!";
    }

    public int getOffsetPlayerPlayer1() {
        return board.getPitList().size() / 2;
    }

    public List<Integer> getPitListPlayer2() {
        List<Integer> subList = board.getPitList().subList(0, board.getIndexKalahaPlayer2());
        return new ArrayList<>(subList);
    }

    public List<Integer> getPitListPlayer1() {
        List<Integer> subList = board.getPitList()
                .subList(board.getIndexKalahaPlayer2() + 1, board.getIndexKalahaPlayer1());
        return new ArrayList<>(subList);
    }

    public int getStonesKalahaPlayer2() {
        int index = board.getIndexKalahaPlayer2();
        return board.getPitList().get(index);
    }

    public int getStonesKalahaPlayer1() {
        int index = board.getIndexKalahaPlayer1();
        return board.getPitList().get(index);
    }

    public boolean isPlayer2Turn() {
        return board.isPlayer2Turn();
    }

    public void setPlayer2Turn(final boolean Player2Turn) {
        board.setPlayer2Turn(Player2Turn);
    }

    public boolean isPitEmpty(final int index) {
        return board.isEmpty(index);
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }
}
