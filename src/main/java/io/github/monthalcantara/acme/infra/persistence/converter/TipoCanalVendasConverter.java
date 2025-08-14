package io.github.monthalcantara.acme.infra.persistence.converter;

import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoCanalVendasConverter implements AttributeConverter<TipoCanalVendas, String> {

    @Override
    public String convertToDatabaseColumn(final TipoCanalVendas attribute) {
        return attribute != null ? attribute.getDescricao().toUpperCase() : null;
    }

    @Override
    public TipoCanalVendas convertToEntityAttribute(final String dbData) {
        return dbData != null ? TipoCanalVendas.fromDescricao(dbData) : null;
    }
}
