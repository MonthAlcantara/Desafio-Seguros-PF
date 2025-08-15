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

@DisplayName("Testes de unidade para o Enum TipoCliente")
class TipoClienteTest {

    @Nested
    @DisplayName("Testes para o método isDescricaoValida")
    class IsDescricaoValidaTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (EnumSource)")
        @EnumSource(TipoCliente.class)
        void deveRetornarTrueParaValoresEnumValidos(final TipoCliente tipoCliente) {
            // Quando
            final var isValida = TipoCliente.isDescricaoValida(tipoCliente.getDescricao());

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", tipoCliente.getDescricao()));
        }

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (ValueSource)")
        @ValueSource(strings = {"cliente_regular", "CLIENTE_PREFERENCIAL"})
        void deveRetornarTrueParaDescricoesComCaseInsensitive(final String descricao) {
            // Quando
            final var isValida = TipoCliente.isDescricaoValida(descricao);

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", descricao));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve ser inválida")
        @ValueSource(strings = {"cliente_invalido", "cliente-regular", "cliente regular"})
        @NullAndEmptySource
        void deveRetornarFalseParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var isValida = TipoCliente.isDescricaoValida(descricao);

            // Então
            assertFalse(isValida, () -> String.format("A descrição '%s' deveria ser inválida", descricao));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromDescricao")
    class FromDescricaoTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar o tipo {1}")
        @ValueSource(strings = {"cliente_auto_risco", "CLIENTE_AUTO_RISCO"})
        void deveRetornarTipoCorretoParaDescricoesValidas(final String descricao) {
            // Quando
            final var resultado = TipoCliente.fromDescricao(descricao);

            // Então
            assertEquals(TipoCliente.AUTO_RISCO, resultado, () -> String.format("Esperava-se 'AUTO_RISCO' mas retornou '%s'", resultado));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve retornar nulo")
        @ValueSource(strings = {"cliente inexistente", "auto risco"})
        @NullAndEmptySource
        void deveRetornarNuloParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = TipoCliente.fromDescricao(descricao);

            // Então
            assertNull(resultado, () -> String.format("Esperava-se nulo para a descrição '%s'", resultado));
        }
    }
}