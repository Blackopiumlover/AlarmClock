package com.yongjia.simplealarmclock.alarmslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yongjia.simplealarmclock.data.Alarm;
import com.yongjia.simplealarmclock.R;

public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener {
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmsListViewModel alarmsListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerViewAdapter.setAlarms(alarms);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);

        RecyclerView alarmsRecyclerView = view.findViewById(R.id.fragment_list_alarms_recyclerView);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);

        Button addAlarm = view.findViewById(R.id.fragment_list_alarms_addAlarm);
        addAlarm.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment));

        return view;
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (getContext() != null) {
            if (alarm.isStarted()) {
                alarm.cancelAlarm(getContext());
                alarmsListViewModel.update(alarm);
            } else {
                alarm.schedule(getContext());
                alarmsListViewModel.update(alarm);
            }
        }
    }
}