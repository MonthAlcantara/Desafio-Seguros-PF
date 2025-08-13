package io.github.monthalcantara.acme.infra.persistence.converter;

import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoCategoriaConverter implements AttributeConverter<TipoCategoria, String> {

    @Override
    public String convertToDatabaseColumn(TipoCategoria attribute) {
        return attribute != null ? attribute.getDescricao().toUpperCase() : null;
    }

    @Override
    public TipoCategoria convertToEntityAttribute(String dbData) {
        return dbData != null ? TipoCategoria.fromDescricao(dbData) : null;
    }
}
