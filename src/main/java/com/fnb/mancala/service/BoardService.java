package com.fnb.mancala.service;

import java.util.List;

public interface BoardService {

    void makeMove(int index);

    List<Integer> getPitList();

    int getIndexKalahaPlayer2();

    int getIndexKalahaPlayer1();

    boolean isPlayer2Turn();

    void setPlayer2Turn(boolean Player2Turn);

    boolean isEmpty(int index);

    boolean isGameOver();
}
