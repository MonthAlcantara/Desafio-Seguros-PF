package io.github.monthalcantara.acme.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testes de unidade para o Enum TipoMetodoPagamento")
class TipoMetodoPagamentoTest {

    @Nested
    @DisplayName("Testes para o método isDescricaoValida")
    class IsDescricaoValidaTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (EnumSource)")
        @EnumSource(TipoMetodoPagamento.class)
        void deveRetornarTrueParaValoresEnumValidos(final TipoMetodoPagamento metodoPagamento) {
            // Quando
            final var isValida = TipoMetodoPagamento.isDescricaoValida(metodoPagamento.getDescricao());

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", metodoPagamento.getDescricao()));
        }

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (ValueSource)")
        @ValueSource(strings = {"cartao_credito", "boleto", "Pix"})
        void deveRetornarTrueParaDescricoesComCaseInsensitive(final String descricao) {
            // Quando
            final var isValida = TipoMetodoPagamento.isDescricaoValida(descricao);

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", descricao));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve ser inválida")
        @ValueSource(strings = {"METODO_INVALIDO", "cartão de débito"})
        @NullAndEmptySource
        void deveRetornarFalseParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var isValida = TipoMetodoPagamento.isDescricaoValida(descricao);

            // Então
            assertFalse(isValida, () -> String.format("A descrição '%s' deveria ser inválida", descricao));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromDescricao")
    class FromDescricaoTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar o método {1}")
        @ValueSource(strings = {"PIX", "pix", "PiX"})
        void deveRetornarMetodoCorretoParaDescricoesValidas(final String descricao) {
            // Quando
            final var resultado = TipoMetodoPagamento.fromDescricao(descricao);

            // Então
            assertEquals(TipoMetodoPagamento.PIX, resultado, () -> String.format("Esperava-se 'PIX' mas retornou '%s'", resultado));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve retornar nulo")
        @ValueSource(strings = {"METODO_INEXISTENTE", "transferencia bancaria"})
        @NullAndEmptySource
        void deveRetornarNuloParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = TipoMetodoPagamento.fromDescricao(descricao);

            // Então
            assertNull(resultado, () -> String.format("Esperava-se nulo para a descrição '%s'", descricao));
        }
    }
}