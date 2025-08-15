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

@DisplayName("Testes de unidade para o Enum TipoCategoria")
class TipoCategoriaTest {

    @Nested
    @DisplayName("Testes para o método isDescricaoValida")
    class IsDescricaoValidaTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (EnumSource)")
        @EnumSource(TipoCategoria.class)
        void deveRetornarTrueParaValoresEnumValidos(final TipoCategoria categoria) {
            // Quando
            final var isValida = TipoCategoria.isDescricaoValida(categoria.getDescricao());

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", categoria.getDescricao()));
        }

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (ValueSource)")
        @ValueSource(strings = {"vida", "aUtO", "reSIDEncIAl"})
        void deveRetornarTrueParaDescricoesComCaseInsensitive(final String descricao) {
            // Quando
            final var isValida = TipoCategoria.isDescricaoValida(descricao);

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", descricao));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve ser inválida")
        @ValueSource(strings = {"CATEGORIA_INVALIDA", "automovel"})
        @NullAndEmptySource
        void deveRetornarFalseParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var isValida = TipoCategoria.isDescricaoValida(descricao);

            // Então
            assertFalse(isValida, () -> String.format("A descrição '%s' deveria ser inválida", descricao));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromDescricao")
    class FromDescricaoTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar a categoria {1}")
        @ValueSource(strings = {"VIDA", "vida", "ViDa"})
        void deveRetornarCategoriaCorretaParaDescricoesValidas(final String descricao) {
            // Quando
            final var resultado = TipoCategoria.fromDescricao(descricao);

            // Então
            assertEquals(TipoCategoria.VIDA, resultado, () -> String.format("Esperava-se 'VIDA' mas retornou '%s'", resultado));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve retornar nulo")
        @ValueSource(strings = {"CATEGORIA_INEXISTENTE", "seguro auto"})
        @NullAndEmptySource
        void deveRetornarNuloParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = TipoCategoria.fromDescricao(descricao);

            // Então
            assertNull(resultado, () -> String.format("Esperava-se nulo para a descrição '%s'", resultado));
        }
    }
}