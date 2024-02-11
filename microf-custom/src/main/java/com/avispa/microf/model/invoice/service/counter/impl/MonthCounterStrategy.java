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
 * This strategy resets counter every month based on the invoice service date
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public class MonthCounterStrategy implements CounterStrategy {
    private final EntityManager entityManager;

    @Override
    public int getNextSerialNumber(BaseInvoice invoice) {
        return getMax(invoice.getClass(), invoice.getIssueDate().getMonthValue()) + 1;
    }

    private Integer getMax(Class<? extends BaseInvoice> clazz, int month) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root<? extends BaseInvoice> queryRoot = criteriaQuery.from(clazz);

        criteriaQuery = criteriaQuery.select(criteriaBuilder.coalesce(criteriaBuilder.max(queryRoot.get("serialNumber")), 0));

        criteriaQuery = criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.function("month", Integer.class, queryRoot.get("issueDate")), month));

        var query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }
}
