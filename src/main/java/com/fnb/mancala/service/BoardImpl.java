package com.fnb.mancala.service;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor
@Service
public final class BoardImpl implements BoardService {

    private boolean Player2Turn;
    private final List<Integer> pitList;

    @Override
    public void makeMove(final int index) {
        int lastPit = allocateStonesAndGetLastPit(index);
        captureIfLastPitIsOwnEmptyPit(lastPit);
        collectLastStonesIfGameIsOver();
        switchTurnsIfLastPitIsNotOwnKalaha(lastPit);
    }

    @Override
    public List<Integer> getPitList() {
        return pitList;
    }

    @Override
    public int getIndexKalahaPlayer2() {
        return getPitList().size() / 2 - 1;
    }

    @Override
    public int getIndexKalahaPlayer1() {
        return getPitList().size() - 1;
    }

    @Override
    public boolean isPlayer2Turn() {
        return Player2Turn;
    }

    @Override
    public void setPlayer2Turn(final boolean Player2Turn) {
        this.Player2Turn = Player2Turn;
    }

    @Override
    public boolean isEmpty(final int index) {
        return getStonesInPit(index) == 0;
    }

    @Override
    public boolean isGameOver() {
        return getTotalStonesInPitsPlayer2() == 0 || getTotalStonesInPitsPlayer1() == 0;
    }

    private int allocateStonesAndGetLastPit(final int index) {
        int stones = getStonesInPit(index);
        int lastPit = index;
        while (stones > 0) {
            lastPit = nextPit(lastPit);
            incrementStonesInPit(lastPit);
            --stones;
        }
        emptyPit(index);
        return lastPit;
    }

    private int nextPit(final int index) {
        int nextIndex = isLastIndex(index) ? 0 : index + 1;
        return skipKalahaOpponent(nextIndex);
    }

    private boolean isLastIndex(final int index) {
        return index == getIndexKalahaPlayer1();
    }

    private void incrementStonesInPit(final int index) {
        setStonesInPit(index, getStonesInPit(index) + 1);
    }

    private void captureIfLastPitIsOwnEmptyPit(final int index) {
        if (pitContainsOneStone(index) && isARegularPit(index) && landsInPlayersOwnPit(index)) {
            int kalaha = isPlayer2Turn() ? getIndexKalahaPlayer2() : getIndexKalahaPlayer1();
            int capturedStones = getStonesInPit(index) + getStonesInPit(oppositePit(index));

            setStonesInPit(kalaha, getStonesInPit(kalaha) + capturedStones);
            emptyPit(index);
            emptyPit(oppositePit(index));
        }
    }

    private boolean pitContainsOneStone(final int index) {
        return getStonesInPit(index) == 1;
    }

    private boolean isARegularPit(final int index) {
        return index != getIndexKalahaPlayer2() && index != getIndexKalahaPlayer1();
    }

    private boolean landsInPlayersOwnPit(final int index) {
        return isPlayer2Turn() && index < getIndexKalahaPlayer2()
                || isNotPlayer2Turn() && index > getIndexKalahaPlayer2();
    }

    private int oppositePit(final int index) {
        return 2 * getIndexKalahaPlayer2() - index;
    }

    private void collectLastStonesIfGameIsOver() {
        if (isGameOver()) {
            int lastStonesPlayer2 = getTotalStonesInPitsPlayer2();
            int lastStonesPlayer1 = getTotalStonesInPitsPlayer1();

            collectStones(lastStonesPlayer2, lastStonesPlayer1);
            emptyRegularPits();
        }
    }

    private void collectStones(final int lastStonesPlayer2, final int lastStonesPlayer1) {
        int newAmountPlayer2 = getStonesInPit(getIndexKalahaPlayer2()) + lastStonesPlayer2;
        int newAmountPlayer1 = getStonesInPit(getIndexKalahaPlayer1()) + lastStonesPlayer1;

        setStonesInPit(getIndexKalahaPlayer2(), newAmountPlayer2);
        setStonesInPit(getIndexKalahaPlayer1(), newAmountPlayer1);
    }

    private void emptyRegularPits() {
        IntStream.range(0, getIndexKalahaPlayer2())
                .forEach(this::emptyPit);

        IntStream.range(getIndexKalahaPlayer2() + 1, getIndexKalahaPlayer1())
                .forEach(this::emptyPit);
    }

    private void switchTurnsIfLastPitIsNotOwnKalaha(final int lastPit) {
        if (isNotOwnKalaha(lastPit)) {
            setPlayer2Turn(isNotPlayer2Turn());
        }
    }

    private boolean isNotOwnKalaha(final int index) {
        return isPlayer2Turn() && index != getIndexKalahaPlayer2()
                || isNotPlayer2Turn() && index != getIndexKalahaPlayer1();
    }

    private int getStonesInPit(final int index) {
        return getPitList().get(index);
    }

    private void setStonesInPit(final int index, final int value) {
        getPitList().set(index, value);
    }

    private void emptyPit(final int index) {
        getPitList().set(index, 0);
    }

    private int getTotalStonesInPitsPlayer2() {
        return IntStream.range(0, getIndexKalahaPlayer2())
                .map(this::getStonesInPit)
                .sum();
    }

    private int getTotalStonesInPitsPlayer1() {
        return IntStream.range(getIndexKalahaPlayer2() + 1, getIndexKalahaPlayer1())
                .map(this::getStonesInPit)
                .sum();
    }

    private int skipKalahaOpponent(final int index) {
        if (isPlayer2Turn() && index == getIndexKalahaPlayer1())
            return 0;
        if (isNotPlayer2Turn() && index == getIndexKalahaPlayer2())
            return index + 1;
        return index;
    }

    private boolean isNotPlayer2Turn() {
        return !isPlayer2Turn();
    }

    public static final class BoardImplBuilder {
        private List<Integer> pitList;

        public BoardImplBuilder pitList(final int pitsPerPlayer, final int stonesPerPit) {
            int totalAmountOfPits = 2 * pitsPerPlayer + 2;
            List<Integer> list = IntStream.
                    generate(() -> stonesPerPit)
                    .limit(totalAmountOfPits)
                    .boxed()
                    .collect(Collectors.toList());
            list.set(list.size() / 2 - 1, 0);
            list.set(list.size() - 1, 0);

            this.pitList = list;
            return this;
        }
    }
}
