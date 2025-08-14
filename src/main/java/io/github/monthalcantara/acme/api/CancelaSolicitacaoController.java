package io.github.monthalcantara.acme.api;


import io.github.monthalcantara.acme.application.service.AtualizaSolicitacaoStatusService;
import io.github.monthalcantara.acme.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/propostas")
@RequiredArgsConstructor
@Tag(name = "Solicitações", description = "Endpoints para a gestão de solicitações de apólice")
public class CancelaSolicitacaoController {

    private final AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;

    @PatchMapping(value = "/{id}/cancelar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cancela uma solicitação de apólice",
            description = "Permite o cancelamento de uma solicitação, desde que não esteja no status APROVADO ou REJEITADO.")
    @ApiResponse(responseCode = "204", description = "Solicitação cancelada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou status não permite cancelamento", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Solicitação não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> cancelarSolicitacao(
            @Parameter(description = "ID da solicitação a ser cancelada.", example = "920c57c4-54c3-4d43-85f0-f20387431102")
            @PathVariable UUID id) {
        atualizaSolicitacaoStatusService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}