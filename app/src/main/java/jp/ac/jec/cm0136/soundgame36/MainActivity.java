package jp.ac.jec.cm0136.soundgame36;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
//    private static final String KEY_EASY = "EASY";
//    private static final String KEY_HARD = "HARD";
//    private int rankTimesEasy = 0;
//    private int rankTimesHard = 0;
    SharedPreferences sp;
    private Button btnEasy,btnHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEasy = findViewById(R.id.btnEasy);
        btnHard = findViewById(R.id.btnHard);
        sp = getSharedPreferences("SoundGame36", Context.MODE_PRIVATE);
        int rankEasy = sp.getInt("EASY",-1);
        TextView txtRankEasy = findViewById(R.id.txtRankEasy);
        if (rankEasy != -1) {
            txtRankEasy.setText("EasyMode Best: " + rankEasy + " clicks");
        }

        int rankHard = sp.getInt("HARD",-1);
        TextView txtRankHard = findViewById(R.id.txtRankHard);
        if (rankHard != -1) {
            txtRankHard.setText("HardMode Best: " + rankHard + " clicks");
        }

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EasyActivity.class);
                startActivityForResult(intent, EasyActivity.CODE_RANK_EASY);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HardActivity.class);
                startActivityForResult(intent, HardActivity.CODE_RANK_HARD);
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        if (requestCode == HardActivity.CODE_RANK_HARD) {
            int rankHard = sp.getInt("HARD",-1);
            TextView txtRankHard = findViewById(R.id.txtRankHard);
            if (rankHard != -1) {
                txtRankHard.setText("HardMode Best: " + rankHard + " clicks");
            }
        }
        if (requestCode == EasyActivity.CODE_RANK_EASY) {
            int rankEasy = sp.getInt("EASY",-1);
            TextView txtRankEasy = findViewById(R.id.txtRankEasy);
            if (rankEasy != -1) {
                txtRankEasy.setText("HardMode Best: " + rankEasy + " clicks");
            }
        }

    }
}