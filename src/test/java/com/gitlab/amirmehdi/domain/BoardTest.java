package com.gitlab.amirmehdi.domain;

import com.gitlab.amirmehdi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Board.class);
        Board board1 = new Board();
        board1.setIsin("IRO1FOLD0001");
        Board board2 = new Board();
        board2.setIsin(board1.getIsin());
        assertThat(board1).isEqualTo(board2);
        board2.setIsin("IRO1SIPA0001");
        assertThat(board1).isNotEqualTo(board2);
        board1.setIsin(null);
        assertThat(board1).isNotEqualTo(board2);
    }
}
