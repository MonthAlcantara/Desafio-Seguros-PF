package io.github.monthalcantara.acme.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de unidade para o Enum ClassificacaoFraude")
class ClassificacaoFraudeTest {

    @Nested
    @DisplayName("Testes para o método isDescricaoValida")
    class IsDescricaoValidaTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve ser válida")
        @EnumSource(ClassificacaoFraude.class)
        void deveRetornarTrueParaDescricoesValidas(final ClassificacaoFraude classificacao) {
            // Quando
            final var isValida = ClassificacaoFraude.isDescricaoValida(classificacao.getDescricao());

            // Então
            assertTrue(isValida, () -> String.format("A descrição '%s' deveria ser válida", classificacao.getDescricao()));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve ser inválida")
        @ValueSource(strings = {"FRAUDE_INVALIDA", "RISCO_ALTO", "HIGH RISK"})
        @NullAndEmptySource
        void deveRetornarFalseParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var isValida = ClassificacaoFraude.isDescricaoValida(descricao);

            // Então
            assertFalse(isValida, () -> String.format("A descrição '%s' deveria ser inválida", descricao));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromDescricao")
    class FromDescricaoTest {

        @ParameterizedTest(name = "A descrição ''{0}'' deve retornar a classificação {1}")
        @EnumSource(ClassificacaoFraude.class)
        void deveRetornarClassificacaoCorretaParaDescricoesValidas(final ClassificacaoFraude classificacao) {
            // Dado
            final var descricao = classificacao.getDescricao();

            // Quando
            final var resultado = ClassificacaoFraude.fromDescricao(descricao);

            // Então
            assertEquals(classificacao, resultado, () -> String.format("Esperava-se '%s' mas retornou '%s'", classificacao, resultado));
        }

        @ParameterizedTest(name = "A descrição inválida ''{0}'' deve retornar UNKNOWN")
        @ValueSource(strings = {"FRAUDE_INEXISTENTE", "Risco Regular", "NO-INFO"})
        @NullAndEmptySource
        void deveRetornarUnknownParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = ClassificacaoFraude.fromDescricao(descricao);

            // Então
            assertEquals(ClassificacaoFraude.UNKNOWN, resultado,
                    () -> String.format("Esperava-se UNKNOWN para a descrição '%s'", descricao));
        }
    }
}