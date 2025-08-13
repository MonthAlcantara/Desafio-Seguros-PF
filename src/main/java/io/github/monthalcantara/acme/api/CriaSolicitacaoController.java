package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.api.dto.response.SolicitacaoCriadaResponse;
import io.github.monthalcantara.acme.application.validator.SolicitacaoRequestValidator;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import io.github.monthalcantara.acme.application.service.CriaSolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/propostas")
public class CriaSolicitacaoController {

    private final CriaSolicitacaoService criaSolicitacaoService;
    private final SolicitacaoRequestValidator requestValidator;

    public CriaSolicitacaoController(CriaSolicitacaoService criaSolicitacaoService, SolicitacaoRequestValidator requestValidator) {
        this.criaSolicitacaoService = criaSolicitacaoService;
        this.requestValidator = requestValidator;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoCriadaResponse> criarApolice(
            @Valid @RequestBody final SolicitacaoRequest request,
            @RequestHeader(value = "x-idempotency-key") final String idempotencyKey) {

        requestValidator.validar(request);

        final var solicitacao = SolicitacaoMapper.toModel(request);

        final var criada = criaSolicitacaoService.criar(solicitacao, idempotencyKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitacaoMapper.toResponseDto(criada));
    }
}
