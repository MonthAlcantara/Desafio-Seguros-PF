package io.github.monthalcantara.acme.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes de unidade para o Enum ClassificacaoRisco")
class ClassificacaoRiscoTest {

    @Nested
    @DisplayName("Testes para o método validarCapitalSegurado")
    class ValidarCapitalSeguradoTest {

        @ParameterizedTest(name = "Categoria {0} com capital {1} deve ser VALIDADO para {2}")
        @CsvSource({
                "AUTO, 250000.00, HIGH_RISK",
                "RESIDENCIAL, 150000.00, HIGH_RISK",
                "VIDA, 500000.00, REGULAR",
                "AUTO, 350000.00, REGULAR",
                "VIDA, 800000.00, PREFERENTIAL",
                "AUTO, 450000.00, PREFERENTIAL",
                "OUTROS, 55000.00, NO_INFO"
        })
        void deveRetornarValidadoQuandoCapitalSeguradoEstaNoLimite(final TipoCategoria categoria, final BigDecimal capital, final ClassificacaoRisco risco) {
            // Quando
            final var status = risco.validarCapitalSegurado(categoria, capital);

            // Então
            assertEquals(TipoStatus.VALIDADO, status, () -> String.format("Esperava-se VALIDADO para categoria '%s' e capital '%s'", categoria, capital));
        }

        @ParameterizedTest(name = "Categoria {0} com capital {1} deve ser REJEITADO para {2}")
        @CsvSource({
                "AUTO, 250000.01, HIGH_RISK",
                "RESIDENCIAL, 150000.01, HIGH_RISK",
                "VIDA, 500000.01, REGULAR",
                "AUTO, 350000.01, REGULAR",
                "VIDA, 800000.01, PREFERENTIAL",
                "AUTO, 450000.01, PREFERENTIAL",
                "OUTROS, 55000.01, NO_INFO"
        })
        void deveRetornarRejeitadoQuandoCapitalSeguradoExcedeLimite(final TipoCategoria categoria, final BigDecimal capital, final ClassificacaoRisco risco) {
            // Quando
            final var status = risco.validarCapitalSegurado(categoria, capital);

            // Então
            assertEquals(TipoStatus.REJEITADO, status, () -> String.format("Esperava-se REJEITADO para categoria '%s' e capital '%s'", categoria, capital));
        }
    }

    @Nested
    @DisplayName("Testes para o método fromString")
    class FromStringTest {

        @ParameterizedTest(name = "A string ''{0}'' deve retornar a classificação {1}")
        @CsvSource({
                "HIGH_RISK, HIGH_RISK",
                "regular, REGULAR",
                " PREFERENTIAL , PREFERENTIAL",
                "No_Info, NO_INFO"
        })
        void deveRetornarClassificacaoCorretaParaDescricoesValidas(final String descricao, final ClassificacaoRisco riscoEsperado) {
            // Quando
            final var resultado = ClassificacaoRisco.fromString(descricao);

            // Então
            assertEquals(riscoEsperado, resultado);
        }

        @ParameterizedTest(name = "A string inválida ''{0}'' deve retornar NO_INFO")
        @ValueSource(strings = {"NAO_EXISTE", "Risco Preferencial"})
        @NullAndEmptySource
        void deveRetornarNoInfoParaDescricoesInvalidas(final String descricao) {
            // Quando
            final var resultado = ClassificacaoRisco.fromString(descricao);

            // Então
            assertEquals(ClassificacaoRisco.NO_INFO, resultado);
        }
    }
}