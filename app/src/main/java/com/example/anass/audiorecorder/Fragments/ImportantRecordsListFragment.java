package com.example.anass.audiorecorder.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anass.audiorecorder.Activities.MainActivity;
import com.example.anass.audiorecorder.Adapters.ImpRecordsAdapter;
import com.example.anass.audiorecorder.Database.DataBase;
import com.example.anass.audiorecorder.Database.Repositories.ImportantRecordRepository;
import com.example.anass.audiorecorder.Helper.OnLoadCompleted;
import com.example.anass.audiorecorder.Managers.RecyclerViewManager;
import com.example.anass.audiorecorder.Models.ImportantRecord;
import com.example.anass.audiorecorder.Models.RecordingItem;
import com.example.anass.audiorecorder.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportantRecordsListFragment extends Fragment implements OnLoadCompleted {

    @Bind(R.id.imp_records_recycler)
    public RecyclerView mainRecycler;

    MainActivity activity;
    ImpRecordsAdapter adapter;
    ImportantRecordRepository.getRecordsAsyncTask recordsAsyncTask;
    DataBase db;
    private int idRecord;
    private RecordingItem recordingItem;
    public static final String ARGS_ID = "id_record";
    public static final String ARGS_ITEM = "item_record";
    private static final String TAG = "ImportantRecordsListFra";

    public static ImportantRecordsListFragment newInstance(int idRecord, RecordingItem recordingItem) {
        ImportantRecordsListFragment fragment = new ImportantRecordsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_ID, idRecord);
        args.putSerializable(ARGS_ITEM, recordingItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.important_records_list_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("Recycler fragment", "lanched");
        activity = (MainActivity) getActivity();
        init();
    }

    public void init() {
        idRecord = getArguments().getInt(ARGS_ID);
        recordingItem = (RecordingItem) getArguments().getSerializable(ARGS_ITEM);
        adapter = new ImpRecordsAdapter(activity, recordingItem);
        RecyclerViewManager.configureRecycleView(activity, mainRecycler);
        mainRecycler.setAdapter(adapter);
        getData();
    }

    @Override
    public void OnLoadCompleted() {
        refreshRecyclerView();
    }

    public void getData() {
        db = DataBase.getInstance(activity.getApplicationContext());
        recordsAsyncTask = new ImportantRecordRepository.getRecordsAsyncTask(db.importantRecordDao(), this);
        recordsAsyncTask.execute(idRecord);
    }

    private void refreshRecyclerView() {
        List<ImportantRecord> recordingItems = recordsAsyncTask.getImportantRecords();
        Log.i(TAG, "refreshRecyclerView: Number of Imp:" + recordingItems.size());
        if (recordingItems != null && recordingItems.size() > 0) {
            adapter.addAllItems(recordingItems);
        }
    }

    @OnClick(R.id.btn_back_imp_rec)
    public void btnBackClicked(){
        activity.onBackPressed();
    }

}
