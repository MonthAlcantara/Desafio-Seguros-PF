package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.api.dto.response.SolicitacaoCriadaResponse;
import io.github.monthalcantara.acme.application.service.CriaSolicitacaoService;
import io.github.monthalcantara.acme.api.validator.SolicitacaoRequestValidator;
import io.github.monthalcantara.acme.exception.ErrorResponse;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/v1/propostas")
@Tag(name = "Solicitações", description = "Endpoints para a gestão de solicitações de apólice")
public class CriaSolicitacaoController {

    private final CriaSolicitacaoService criaSolicitacaoService;
    private final SolicitacaoRequestValidator requestValidator;

    public CriaSolicitacaoController(final CriaSolicitacaoService criaSolicitacaoService, final SolicitacaoRequestValidator requestValidator) {
        this.criaSolicitacaoService = criaSolicitacaoService;
        this.requestValidator = requestValidator;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cria uma nova solicitação de apólice de seguro",
            description = "Este endpoint recebe um payload com os dados de uma apólice, valida, e cria a solicitação.")
    @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso", content = @Content(schema = @Schema(implementation = SolicitacaoCriadaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida (payload malformado ou valores incorretos)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitacaoCriadaResponse> criarApolice(
            @Valid @RequestBody final SolicitacaoRequest request,
            @Parameter(description = "Chave de idempotência para evitar duplicação de requisições.", required = true)
            @RequestHeader(value = "x-idempotency-key") final String idempotencyKey) {

        requestValidator.validar(request);

        final var solicitacao = SolicitacaoMapper.toModel(request);

        final var criada = criaSolicitacaoService.criar(solicitacao, idempotencyKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitacaoMapper.toResponseDto(criada));
    }
}