package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.sahra.SahraRequestService;
import com.gitlab.amirmehdi.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gitlab.amirmehdi.domain.Token}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TokenResource {

    private final Logger log = LoggerFactory.getLogger(TokenResource.class);

    private static final String ENTITY_NAME = "token";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TokenRepository tokenRepository;
    private final SahraRequestService sahraRequestService;

    public TokenResource(TokenRepository tokenRepository, SahraRequestService sahraRequestService) {
        this.tokenRepository = tokenRepository;
        this.sahraRequestService = sahraRequestService;
    }

    /**
     * {@code POST  /tokens} : Create a new token.
     *
     * @param token the token to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new token, or with status {@code 400 (Bad Request)} if the token has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tokens")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Token> createToken(@Valid @RequestBody Token token) throws URISyntaxException {
        log.debug("REST request to save Token : {}", token);
        if (token.getId() != null) {
            throw new BadRequestAlertException("A new token cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Token result = save(token);
        return ResponseEntity.created(new URI("/api/tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Token save(Token token) {
        token= tokenRepository.save(token);
        if (Broker.FIROOZE_ASIA.equals(token.getBroker())){
            sahraRequestService.connectAndStart();
        }
        return token;
    }

    /**
     * {@code PUT  /tokens} : Updates an existing token.
     *
     * @param token the token to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated token,
     * or with status {@code 400 (Bad Request)} if the token is not valid,
     * or with status {@code 500 (Internal Server Error)} if the token couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tokens")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Token> updateToken(@Valid @RequestBody Token token) throws URISyntaxException {
        log.debug("REST request to update Token : {}", token);
        if (token.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Token result = save(token);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, token.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tokens} : get all the tokens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tokens in body.
     */
    @GetMapping("/tokens")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<Token>> getAllTokens(Pageable pageable) {
        log.debug("REST request to get a page of Tokens");
        Page<Token> page = tokenRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tokens/:id} : get the "id" token.
     *
     * @param id the id of the token to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the token, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tokens/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Token> getToken(@PathVariable Long id) {
        log.debug("REST request to get Token : {}", id);
        Optional<Token> token = tokenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(token);
    }

    /**
     * {@code DELETE  /tokens/:id} : delete the "id" token.
     *
     * @param id the id of the token to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tokens/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteToken(@PathVariable Long id) {
        log.debug("REST request to delete Token : {}", id);
        tokenRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
