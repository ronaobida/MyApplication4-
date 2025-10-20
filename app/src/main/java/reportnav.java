package com.example.myapplication;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.tasks.Task;
import com.example.myapplication.tasks.TaskRepository;
import com.example.myapplication.models.User;
import com.example.myapplication.services.FirestoreService;
import com.example.myapplication.reports.TechnicianPayrollAdapter;
import com.example.myapplication.reports.TechnicianPayrollItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reportnav#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reportnav extends Fragment {
    private TextView txtTotalTasks, txtCompletedTasks, txtPendingTasks, txtRevenue;
    private RecyclerView recyclerTechnicianPayroll;
    private TechnicianPayrollAdapter payrollAdapter;
    private BarChart barChartRevenue;
    private FirestoreService firestore;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PH"));

    @NonNull
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reportnav_fragment, container, false);

        txtTotalTasks = view.findViewById(R.id.txtTotalTasks);
        txtCompletedTasks = view.findViewById(R.id.txtCompletedTasks);
        txtPendingTasks = view.findViewById(R.id.txtPendingTasks);
        txtRevenue = view.findViewById(R.id.txtRevenue);
        recyclerTechnicianPayroll = view.findViewById(R.id.recyclerTechnicianPayroll);
        barChartRevenue = view.findViewById(R.id.barChartRevenue);

        recyclerTechnicianPayroll.setLayoutManager(new LinearLayoutManager(getContext()));
        payrollAdapter = new TechnicianPayrollAdapter();
        recyclerTechnicianPayroll.setAdapter(payrollAdapter);

        firestore = new FirestoreService();

        loadReports();
        loadTechnicianPayroll();
        loadRevenueChart();

        return view;
    }

    private void loadReports() {
        List<Task> tasks = TaskRepository.getAll();
        int total = tasks.size();
        int completed = 0;
        int pending = 0;
        double revenue = 0;

        for (Task t : tasks) {
            if (t.status == Task.Status.DONE) {
                completed++;
                // Assume revenue based on plan, e.g., 700, 1000, 1500
                // For simplicity, add a fixed amount or parse from description
                revenue += 500; // Placeholder
            } else if (t.status == Task.Status.NEW || t.status == Task.Status.ACCEPTED) {
                pending++;
            }
        }

        txtTotalTasks.setText("Total Tasks: " + total);
        txtCompletedTasks.setText("Completed: " + completed);
        txtPendingTasks.setText("Pending: " + pending);
        txtRevenue.setText("Estimated Revenue: " + currency.format(revenue));
    }

    private void loadTechnicianPayroll() {
        firestore.getUsersByRolesAnyCollection(Arrays.asList("technician", "Technician"), users -> {
            List<TechnicianPayrollItem> list = new ArrayList<>();
            for (User u : users) {
                double amount = 0; // TODO: compute based on completed tasks per technician when available
                String name = u.getDisplayName() != null ? u.getDisplayName() : (u.getName() != null ? u.getName() : "Technician");
                list.add(new TechnicianPayrollItem(name, amount));
            }
            payrollAdapter.setItems(list);
        }, e -> {
            // ignore errors for now
        });
    }

    private void loadRevenueChart() {
        firestore.getUsersByRolesAnyCollection(Arrays.asList("subscriber", "Subscriber"), users -> {
            double total = 0;
            for (User u : users) {
                Integer p = u.getPlanPrice();
                if (p != null) total += p;
            }

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0f, (float) total));
            BarDataSet dataSet = new BarDataSet(entries, "Monthly Revenue");
            dataSet.setColor(0xFF094BAE);

            BarData data = new BarData(dataSet);
            data.setBarWidth(0.6f);
            barChartRevenue.setData(data);

            XAxis xAxis = barChartRevenue.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Total"}));

            barChartRevenue.getAxisRight().setEnabled(false);
            barChartRevenue.getDescription().setEnabled(false);
            barChartRevenue.getLegend().setEnabled(true);
            barChartRevenue.invalidate();

            txtRevenue.setText("Estimated Revenue: " + currency.format(total));
        }, e -> {
            // ignore errors for now
        });
    }
}
