package io.github.monthalcantara.acme.api.validator;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import io.github.monthalcantara.acme.exception.ValidacaoNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testes de unidade para SolicitacaoRequestValidator")
class SolicitacaoRequestValidatorTest {

    private SolicitacaoRequestValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new SolicitacaoRequestValidator();
    }

    private SolicitacaoRequest criarPayloadValido() {
        return new SolicitacaoRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                TipoCategoria.AUTO.getDescricao(),
                TipoCanalVendas.MOBILE.getDescricao(),
                TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                BigDecimal.valueOf(150.75),
                BigDecimal.valueOf(50000.00),
                null,
                null
        );
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class HappyPath {

        @Test
        @DisplayName("Deve validar a requisição quando todos os campos são válidos")
        void deveValidarComCamposValidos() {
            // Dado
            final var request = criarPayloadValido();

            // Quando e Então
            assertDoesNotThrow(() -> validator.validar(request),
                    "Não deveria lançar exceção para uma requisição válida");
        }
    }

    @Nested
    @DisplayName("Cenários de Falha")
    class FailurePaths {

        @ParameterizedTest(name = "Deve falhar com valor ''{0}'' para o campo ''{1}''")
        @MethodSource("invalidRequestProvider")
        @DisplayName("Deve lançar ValidacaoNegocioException para campos inválidos")
        void deveLancarExcecaoParaCamposInvalidos(
                final String categoria, final String canalVenda, final String metodoPagamento, final List<String> errosEsperados) {
            // Dado
            final var requestInvalido = new SolicitacaoRequest(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    categoria,
                    canalVenda,
                    metodoPagamento,
                    BigDecimal.valueOf(100.00),
                    BigDecimal.valueOf(20000.00),
                    null,
                    null
            );

            // Quando e Então
            final var exception = assertThrows(ValidacaoNegocioException.class, () -> validator.validar(requestInvalido));

            assertEquals(errosEsperados.size(), exception.getErros().size(),
                    "O número de erros esperados não corresponde ao número de erros obtidos");

            for (final String erroEsperado : errosEsperados) {
                assertEquals(1, exception.getErros().stream().filter(e -> e.startsWith(erroEsperado.split(":")[0])).count(),
                        "A lista de erros não contém a mensagem esperada: " + erroEsperado);
            }
        }

        private static Stream<Arguments> invalidRequestProvider() {
            final String categoriaInvalida = "CATEGORIA_INCORRETA";
            final String canalInvalido = "CANAL_INCORRETO";
            final String metodoInvalido = "METODO_INCORRETO";

            return Stream.of(
                    Arguments.of(categoriaInvalida, TipoCanalVendas.MOBILE.getDescricao(), TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                            List.of("categoria inválido")),
                    Arguments.of(TipoCategoria.AUTO.getDescricao(), canalInvalido, TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                            List.of("canalVenda inválido")),
                    Arguments.of(TipoCategoria.AUTO.getDescricao(), TipoCanalVendas.MOBILE.getDescricao(), metodoInvalido,
                            List.of("metodoPagamento inválido")),

                    Arguments.of(null, TipoCanalVendas.MOBILE.getDescricao(), TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                            List.of("categoria inválido")),
                    Arguments.of(TipoCategoria.AUTO.getDescricao(), null, TipoMetodoPagamento.CARTAO_CREDITO.getDescricao(),
                            List.of("canalVenda inválido")),
                    Arguments.of(TipoCategoria.AUTO.getDescricao(), TipoCanalVendas.MOBILE.getDescricao(), null,
                            List.of("metodoPagamento inválido")),

                    Arguments.of(categoriaInvalida, canalInvalido, metodoInvalido,
                            List.of("categoria inválido", "canalVenda inválido", "metodoPagamento inválido"))
            );
        }
    }
}