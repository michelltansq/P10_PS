package sg.edu.rp.webservices.p10_ps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Record extends AppCompatActivity {

    TextView tv;
    ArrayAdapter aa;
    ArrayList<String> records, favorites;
    Button btnRefresh, btnFavorites;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        tv = findViewById(R.id.tv);
        records = new ArrayList<>();
        favorites = new ArrayList<>();

        aa = new ArrayAdapter(Record.this, android.R.layout.simple_list_item_1, records);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
                File targetFile = new File(folderLocation, "record.txt");
                if (targetFile.exists()) {
                    records.clear();
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            records.add(line);
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                aa = new ArrayAdapter(Record.this, android.R.layout.simple_list_item_1, records);
                lv.setAdapter(aa);
                tv.setText("Number of records: " + records.size());
            }

        });

        btnFavorites = findViewById(R.id.btnFavorites);
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
                File targetFile = new File(folderLocation, "favorites.txt");
                if (targetFile.exists()) {
                    favorites.clear();
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            favorites.add(line);
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                aa = new ArrayAdapter(Record.this, android.R.layout.simple_list_item_1, favorites);
                lv.setAdapter(aa);
                tv.setText("Number of records: " + favorites.size());
            }
        });

        lv = findViewById(R.id.lv);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(Record.this);
                myBuilder.setMessage("Add this location in your favourite list?");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
                            File targetFile = new File(folderLocation, "favorites.txt");
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write(records.get(position) + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                myBuilder.setNegativeButton("No", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        File targetFile = new File(folderLocation, "record.txt");
        if (targetFile.exists()) {
            records.clear();
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    records.add(line);
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        aa.notifyDataSetChanged();
        tv.setText("Number of records: " + records.size());
    }
}