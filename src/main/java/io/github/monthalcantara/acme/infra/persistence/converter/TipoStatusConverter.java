package io.github.monthalcantara.acme.infra.persistence.converter;

import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoStatusConverter implements AttributeConverter<TipoStatus, String> {

    @Override
    public String convertToDatabaseColumn(final TipoStatus attribute) {
        return attribute != null ? attribute.getDescricao().toUpperCase() : null;
    }

    @Override
    public TipoStatus convertToEntityAttribute(final String dbData) {
        return dbData != null ? TipoStatus.fromDescricao(dbData) : null;
    }
}
