package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.api.dto.response.SolicitacaoResponse;
import io.github.monthalcantara.acme.application.service.ConsultaSolicitacaoService;
import io.github.monthalcantara.acme.exception.ErrorResponse;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/propostas")
@Tag(name = "Consultas", description = "Endpoints para a consulta de solicitações de apólice")
public class ConsultaSolicitacaoController {

    private final ConsultaSolicitacaoService consultaSolicitacaoService;

    public ConsultaSolicitacaoController(final ConsultaSolicitacaoService consultaSolicitacaoService) {
        this.consultaSolicitacaoService = consultaSolicitacaoService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca uma solicitação por ID",
            description = "Retorna os detalhes completos de uma solicitação de apólice específica.")
    @ApiResponse(responseCode = "200", description = "Solicitação encontrada com sucesso", content = @Content(schema = @Schema(implementation = SolicitacaoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitacaoResponse> porId(
            @Parameter(description = "ID da solicitação a ser buscada.", example = "920c57c4-54c3-4d43-85f0-f20387431102")
            @PathVariable final UUID id) {
        final var solicitacao = consultaSolicitacaoService.porId(id);
        return ResponseEntity.ok(SolicitacaoMapper.toResponse(solicitacao));
    }

    @GetMapping(value = "/cliente/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca solicitações por ID do cliente",
            description = "Retorna uma lista de solicitações de apólice de seguro associadas a um cliente.")
    @ApiResponse(responseCode = "200", description = "Lista de solicitações retornada", content = @Content(schema = @Schema(implementation = SolicitacaoResponse.class)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação encontrada para o cliente", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<SolicitacaoResponse>> porClienteId(
            @Parameter(description = "ID do cliente.", example = "920c57c4-54c3-4d43-85f0-f20387431102")
            @PathVariable final UUID clienteId) {
        final var solicitacaos = consultaSolicitacaoService.porClienteId(clienteId);
        return ResponseEntity.ok(SolicitacaoMapper.toResponseList(solicitacaos));
    }
}