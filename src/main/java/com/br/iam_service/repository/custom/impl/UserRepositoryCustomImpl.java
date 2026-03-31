package com.br.iam_service.repository.custom.impl;

import com.br.iam_service.document.UserDocument;
import com.br.iam_service.repository.custom.UserRepositoryCustom;
import com.br.shared.contracts.model.UserRoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<UserDocument> findUsersCustom(
            final String id,
            final String nome,
            final String email,
            final UserRoleRepresentation role,
            final Integer limit,
            final Boolean unPaged) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (id != null) criteriaList.add(Criteria.where("id").is(id));

        if (StringUtils.hasText(nome)) {
            criteriaList.add(Criteria.where("nome").regex(".*" + nome + ".*", "i"));
        }

        if (email != null) criteriaList.add(Criteria.where("email").is(email));

        if (role != null) criteriaList.add(Criteria.where("role").is(role));

        if (!criteriaList.isEmpty()){
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        Pageable pageable = unPaged ? Pageable.unpaged() : PageRequest.of(0, limit);

        if (pageable.isPaged()) {
            query.with(pageable);
        }

        List<UserDocument> list = mongoTemplate.find(query, UserDocument.class);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), UserDocument.class));
    }
}