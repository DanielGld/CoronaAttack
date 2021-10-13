package com.example.coronaattack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    ListView listView;
    List<String> list;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        listView = findViewById(R.id.result_listview);
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.text_center, R.id.textItem ,list);
        listView.setAdapter(arrayAdapter);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://corona-attack-d8bdd-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("scores");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String txt;
                for (DataSnapshot sanp : dataSnapshot.getChildren()){
                    txt = sanp.getValue(String.class);
                    txt = txt.replaceAll(" ", "\t\t\t\t");
                    list.add(txt);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }
}