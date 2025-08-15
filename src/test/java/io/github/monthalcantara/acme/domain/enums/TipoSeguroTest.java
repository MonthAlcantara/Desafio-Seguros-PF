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

@DisplayName("Testes de unidade para o Enum TipoSeguro")
class TipoSeguroTest {

    @Nested
    @DisplayName("Testes para o método isDescricaoValida")
    class IsDescricaoValidaTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (EnumSource)")
        @EnumSource(TipoSeguro.class)
        void deveRetornarTrueParaValoresEnumValidos(final TipoSeguro tipoSeguro) {
            // Quando
            final var isValida = TipoSeguro.isDescricaoValida(tipoSeguro.getDescricao());

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", tipoSeguro.getDescricao()));
        }

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida (ValueSource)")
        @ValueSource(strings = {"seguro de vida", "seguro de automóvel", "outros seguros"})
        void deveRetornarTrueParaDescricoesComCaseInsensitive(final String descricao) {
            // Quando
            final var isValida = TipoSeguro.isDescricaoValida(descricao);

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", descricao));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve ser inválida")
        @ValueSource(strings = {"SEGURO_INVALIDO", "Seguro Automovel", "seguro inválido"})
        @NullAndEmptySource
        void deveRetornarFalseParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var isValida = TipoSeguro.isDescricaoValida(descricao);

            // Então
            assertFalse(isValida, () -> String.format("A descrição '%s' deveria ser inválida", descricao));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromDescricao")
    class FromDescricaoTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar o tipo de seguro {1}")
        @ValueSource(strings = {"Seguro Residencial", "seguro residencial", "Seguro Residencial"})
        void deveRetornarSeguroCorretoParaDescricoesValidas(final String descricao) {
            // Quando
            final var resultado = TipoSeguro.fromDescricao(descricao);

            // Então
            assertEquals(TipoSeguro.RESIDENCIAL, resultado, () -> String.format("Esperava-se 'RESIDENCIAL' mas retornou '%s'", resultado));
        }

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar o tipo de seguro OUTROS")
        @ValueSource(strings = {"outros seguros", "Outros Seguros"})
        void deveRetornarTipoOutrosParaDescricoesValidas(final String descricao) {
            // Quando
            final var resultado = TipoSeguro.fromDescricao(descricao);

            // Então
            assertEquals(TipoSeguro.OUTROS, resultado, () -> String.format("Esperava-se 'OUTROS' mas retornou '%s'", resultado));
        }


        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve retornar nulo")
        @ValueSource(strings = {"SEGURO INEXISTENTE", "seguro_invalido"})
        @NullAndEmptySource
        void deveRetornarNuloParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = TipoSeguro.fromDescricao(descricao);

            // Então
            assertNull(resultado, () -> String.format("Esperava-se nulo para a descrição '%s'", resultado));
        }
    }
}