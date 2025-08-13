package io.github.monthalcantara.acme.infra.persistence.converter;

import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoFormaPagamentoConverter implements AttributeConverter<TipoMetodoPagamento, String> {

    @Override
    public String convertToDatabaseColumn(TipoMetodoPagamento attribute) {
        return attribute != null ? attribute.getDescricao().toUpperCase() : null;
    }

    @Override
    public TipoMetodoPagamento convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoMetodoPagamento.fromDescricao(dbData) : null;
    }
}
