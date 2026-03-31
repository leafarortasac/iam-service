package com.br.iam_service.repository.custom;

import com.br.iam_service.document.UserDocument;
import com.br.shared.contracts.model.UserRoleRepresentation;
import org.springframework.data.domain.Page;

public interface UserRepositoryCustom {

    Page<UserDocument> findUsersCustom(
            final String id,
            final String nome,
            final String email,
            final UserRoleRepresentation role,
            final Integer limit,
            final Boolean unPaged);
}
