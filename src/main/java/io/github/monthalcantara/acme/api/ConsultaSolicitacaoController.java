package io.github.monthalcantara.acme.api;

import io.github.monthalcantara.acme.api.dto.response.SolicitacaoResponse;
import io.github.monthalcantara.acme.application.service.ConsultaSolicitacaoService;
import io.github.monthalcantara.acme.mapper.SolicitacaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/propostas")
@RequiredArgsConstructor
public class ConsultaSolicitacaoController {

    private final ConsultaSolicitacaoService consultaSolicitacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponse> porId(@PathVariable final UUID id) {
        final var solicitacao = consultaSolicitacaoService.porId(id);
        return ResponseEntity.ok(SolicitacaoMapper.toResponse(solicitacao));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<SolicitacaoResponse>> porClienteId(@PathVariable final UUID clienteId) {
        final var solicitacaos = consultaSolicitacaoService.porClienteId(clienteId);
        return ResponseEntity.ok(SolicitacaoMapper.toResponseList(solicitacaos));
    }
}
