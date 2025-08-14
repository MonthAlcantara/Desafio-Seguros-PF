package io.github.monthalcantara.acme.api;


import io.github.monthalcantara.acme.application.service.AtualizaSolicitacaoStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/propostas")
@RequiredArgsConstructor
public class CancelaSolicitacaoController {

    private final AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSolicitacao(@PathVariable UUID id) {
        atualizaSolicitacaoStatusService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}