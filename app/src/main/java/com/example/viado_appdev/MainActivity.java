package com.example.viado_appdev;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.viado_appdev.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_STATION_CODE = "extra_station_code";
    public static final String EXTRA_STATUS = "extra_status";
    public static final String EXTRA_ISSUE_NOTE = "extra_issue_note";


    private ActivityMainBinding binding;
    private String currentCode = "PC-07";
    private String currentStatus = "Needs Check";
    private String currentNote = "Emulator slow to boot";


    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new
 ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    currentStatus = result.getData().getStringExtra(EXTRA_STATUS);
                    currentNote = result.getData().getStringExtra(EXTRA_ISSUE_NOTE);
                    binding.tvStationStatus.setText(currentStatus);
                    binding.tvStationSpec.setText("Note: " + currentNote);
                    Toast.makeText(this, "Updated " + currentCode + " -> " +
 currentStatus, Toast.LENGTH_SHORT).show();
                }
            });

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            binding.btnInspectStation.setOnClickListener(v -> {
                Intent intent = new Intent(this, StationDetailActivity.class);
                intent.putExtra(EXTRA_STATION_CODE, currentCode);
                intent.putExtra(EXTRA_STATUS, currentStatus);
                intent.putExtra(EXTRA_ISSUE_NOTE, currentNote);
                detailLauncher.launch(intent);
            });

            binding.btnShareReport.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LabCheck Report: " + currentCode);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Station: " + currentCode + "\nStatus: " + currentStatus + "\nNote: " + currentNote);

                startActivity(Intent.createChooser(shareIntent, "Share Station Status"));
            });
        }
}