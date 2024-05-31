package learn.gomoku.players;

import learn.gomoku.game.Stone;

import java.util.List;

public interface Player {

    void setName(String name);
    String getName();

    Stone generateMove(List<Stone> previousMoves);
}
