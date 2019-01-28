/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Adapter for transaction lists.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class TransactionsAdapter extends EntityListAdapter<Transaction, TransactionViewHolder> {
    private LiveData<Map<Long, Account>> accounts = FinanceDatabase.getInstance().accountDao().getCacheMap();
    private LiveData<Map<Long, Category>> categories = FinanceDatabase.getInstance().categoryDao().getCacheMap();

    public TransactionsAdapter(BaseActivity context, LiveData<List<Transaction>> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_transaction, parent, false);
        return new TransactionViewHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        final Transaction transaction = getItem(index);
        holder.setTransactionName(transaction.getName());
        holder.setDate(transaction.getDate());
        holder.setAmount(transaction.getAmount());

        if (transaction.getRepeatingId() == null) {
            holder.setRepeatingName(null);
        } else {
            FinanceDatabase.getInstance().repeatingTransactionDao().get(transaction.getRepeatingId()).observe(context, new Observer<RepeatingTransaction>() {
                @Override
                public void onChanged(@Nullable RepeatingTransaction repeatingTransaction) {
                    holder.setRepeatingName(repeatingTransaction.getName());
                }
            });
        }

        accounts.observe(context, new Observer<Map<Long, Account>>() {
            @Override
            public void onChanged(@Nullable Map<Long, Account> map) {
                holder.setAccountName(map.get(transaction.getAccountId()).getName());
            }
        });

        categories.observe(context, new Observer<Map<Long, Category>>() {
            @Override
            public void onChanged(@Nullable Map<Long, Category> map) {
                if (transaction.getCategoryId() != null) {
                    Category category = map.get(transaction.getCategoryId());
                    holder.setCategoryName(category.getName());
                    holder.setCategoryColor(category.getColor());
                } else {
                    holder.setCategoryName(null);
                    holder.setCategoryColor(null);
                }
            }
        });
    }
}
