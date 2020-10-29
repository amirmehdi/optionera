package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.Board;
import com.gitlab.amirmehdi.service.BoardQueryService;
import com.gitlab.amirmehdi.service.BoardService;
import com.gitlab.amirmehdi.service.dto.BoardCriteria;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.Board}.
 */
@RestController
@RequestMapping("/api")
public class BoardResource {

    private final Logger log = LoggerFactory.getLogger(BoardResource.class);

    private final BoardService boardService;

    private final BoardQueryService boardQueryService;

    public BoardResource(BoardService boardService, BoardQueryService boardQueryService) {
        this.boardService = boardService;
        this.boardQueryService = boardQueryService;
    }

    /**
     * {@code GET  /boards} : get all the boards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boards in body.
     */
    @GetMapping("/boards")
    public ResponseEntity<List<Board>> getAllBoards(BoardCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Boards by criteria: {}", criteria);
        Page<Board> page = boardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /boards/count} : count all the boards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/boards/count")
    public ResponseEntity<Long> countBoards(BoardCriteria criteria) {
        log.debug("REST request to count Boards by criteria: {}", criteria);
        return ResponseEntity.ok().body(boardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /boards/:id} : get the "id" board.
     *
     * @param isin the id of the board to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the board, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boards/{isin}")
    public ResponseEntity<Board> getBoard(@PathVariable String isin) {
        log.debug("REST request to get Board : {}", isin);
        Optional<Board> board = boardService.findOne(isin);
        return ResponseUtil.wrapOrNotFound(board);
    }
}
