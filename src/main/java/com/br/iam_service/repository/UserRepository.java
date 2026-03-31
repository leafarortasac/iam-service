package com.br.iam_service.repository;

import com.br.iam_service.document.UserDocument;
import com.br.iam_service.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> , UserRepositoryCustom {

    Optional<UserDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
