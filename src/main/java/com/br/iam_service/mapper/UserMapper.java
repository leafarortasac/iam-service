package com.br.iam_service.mapper;

import com.br.iam_service.document.UserDocument;
import com.br.shared.contracts.model.UsuarioDocumentRepresentation;
import com.br.shared.contracts.model.UsuarioRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "senha", ignore = true)
    UsuarioRepresentation toRepresentation(UserDocument document);

    UserDocument toDocument(UsuarioRepresentation representation);

    @Mapping(target = "usuario", source = "document")
    UsuarioDocumentRepresentation toDocumentRepresentation(UserDocument document);

    List<UsuarioDocumentRepresentation> toListUsuarioDocumentoRepresentatio(List<UserDocument> registros);
}
