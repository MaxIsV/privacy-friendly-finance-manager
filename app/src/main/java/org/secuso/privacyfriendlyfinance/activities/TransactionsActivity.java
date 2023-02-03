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
package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionsViewModel;
import org.secuso.privacyfriendlyfinance.csv.CsvExporter;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;
import org.secuso.privacyfriendlyfinance.domain.model.common.NameWithIdDto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Transactions activity that show ALL transactions in the system. Is the main activity of this app.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class TransactionsActivity extends TransactionListActivity {
    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return TransactionsViewModel.class;
    }

    private static final String TAG = "mytag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.transactions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exportButton:
                CsvExporter exporter = null;
                Id2Name<NameWithIdDto> id2Category = new Id2Name<>(Arrays.asList(new NameWithIdDto("my test category", 12345L)));
                Id2Name<NameWithIdDto> id2Account = new Id2Name<>(Arrays.asList(new NameWithIdDto("my test account", 54321L)));
                try {
                    exporter = new CsvExporter(new FileWriter(getExternalFilesDir("MyFileDir") + "/the-test.csv"), id2Category, id2Account);
                    Log.d(TAG, getExternalFilesDir("MyFileDir") + "/the-test.csv");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                exporter.writeCsvHeader();

                Transaction transaction = new Transaction("Example", 42, new LocalDate(), 54321L, 12345L);
                exporter.writeCsvLine(transaction);
                try {
                    exporter.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // Code for About goes here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
