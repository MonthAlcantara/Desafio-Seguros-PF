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

@DisplayName("Testes de unidade para o Enum TipoCanalVendas")
class TipoCanalVendasTest {

    @Nested
    @DisplayName("Testes para o método isDescricaoValida")
    class IsDescricaoValidaTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (EnumSource)")
        @EnumSource(TipoCanalVendas.class)
        void deveRetornarTrueParaValoresEnumValidos(final TipoCanalVendas canal) {
            // Quando
            final var isValida = TipoCanalVendas.isDescricaoValida(canal.getDescricao());

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", canal.getDescricao()));
        }

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (ValueSource)")
        @ValueSource(strings = {"whatsapp", "WEB_site", "Mobile"})
        void deveRetornarTrueParaDescricoesComCaseInsensitive(final String descricao) {
            // Quando
            final var isValida = TipoCanalVendas.isDescricaoValida(descricao);

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", descricao));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve ser inválida")
        @ValueSource(strings = {"CANAL_INVALIDO", "rede social"})
        @NullAndEmptySource
        void deveRetornarFalseParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var isValida = TipoCanalVendas.isDescricaoValida(descricao);

            // Então
            assertFalse(isValida, () -> String.format("A descrição '%s' deveria ser inválida", descricao));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromDescricao")
    class FromDescricaoTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar o canal {1}")
        @ValueSource(strings = {"WEB_SITE", "web_site", "wEb_SiTe"})
        void deveRetornarCanalCorretoParaDescricoesValidas(final String descricao) {
            // Quando
            final var resultado = TipoCanalVendas.fromDescricao(descricao);

            // Então
            assertEquals(TipoCanalVendas.WEB_SITE, resultado, () -> String.format("Esperava-se 'WEB_SITE' mas retornou '%s'", resultado));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve retornar nulo")
        @ValueSource(strings = {"CANAL_INEXISTENTE", "televendas"})
        @NullAndEmptySource
        void deveRetornarNuloParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = TipoCanalVendas.fromDescricao(descricao);

            // Então
            assertNull(resultado, () -> String.format("Esperava-se nulo para a descrição '%s'", descricao));
        }
    }
}