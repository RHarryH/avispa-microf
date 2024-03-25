/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.model.invoice.service.counter.impl;

import com.avispa.microf.model.invoice.BaseInvoice;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

/**
 * This strategy gets last invoice number and appends one to it
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public class ContinuousCounterStrategy implements CounterStrategy {
    private final EntityManager entityManager;

    @Override
    public int getNextSerialNumber(BaseInvoice invoice) {
        return (int) (getMax(invoice.getClass()) + 1);
    }

    private Long getMax(Class<? extends BaseInvoice> clazz) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<? extends BaseInvoice> queryRoot = criteriaQuery.from(clazz);

        criteriaQuery = criteriaQuery.select(criteriaBuilder.count(queryRoot));

        var query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }
}
