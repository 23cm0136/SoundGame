package jp.ac.jec.cm0136.soundgame36;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardActivity extends AppCompatActivity {
    private static final String TAG = "HardActivity";
    public static final int CODE_RANK_HARD = 123;

    SharedPreferences sp;
    int rankHard;
    private int clickTimes = 0;
    private int rightAnswer = 0;
    private TextView txtClick;

    private final Map<Integer, Integer> buttonToSoundIdMap = new HashMap<>();

    private ImageButton preSelet = null;
    private TextView txt;

    private final int[] imgButtons = {
            R.id.ibtn01, R.id.ibtn02, R.id.ibtn03, R.id.ibtn04,
            R.id.ibtn05, R.id.ibtn06, R.id.ibtn07, R.id.ibtn08,
            R.id.ibtn09, R.id.ibtn10, R.id.ibtn11, R.id.ibtn12,
            R.id.ibtn13, R.id.ibtn14, R.id.ibtn15, R.id.ibtn16
    };
    private final MediaPlayer[] plays = new MediaPlayer[imgButtons.length];
    private int nowState = -1;

    private SoundPool soundPool;
    int sound1, sound2;

    class ButtonClickAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i;
            for (i = 0; i < imgButtons.length; i++) {
                if (v.getId() == imgButtons[i]) {
                    break;
                }
            }
            MediaPlayer p = plays[i];

            p.setLooping(false);
            p.seekTo(0);
            p.start();

            v.setEnabled(false);

            if (nowState == -1) {
                txt.setText("二つ目を選択してください");
                nowState = i;
                preSelet = ((ImageButton) v);
                ((ImageButton) v).setImageResource(R.drawable.neko02);
            } else {
                if (buttonToSoundIdMap.get(i).equals(buttonToSoundIdMap.get(nowState))) {
                    txt.setText("正解!");
                    ((ImageButton)v).setImageResource(R.drawable.neko02);
                    preSelet.setImageResource(R.drawable.neko02);
                    soundPool.play(sound1,1.0f,1.0f,0,0,1);
                    rightAnswer++;
                } else {
                    txt.setText("不正解！一つ目を選択してください");
                    soundPool.play(sound2,1.0f,1.0f,0,0,1);
                    ((ImageButton)v).setImageResource(R.drawable.neko01);
                    preSelet.setImageResource(R.drawable.neko01);
                    preSelet.setEnabled(true);
                    v.setEnabled(true);
                }
                nowState = -1;
            }
            clickTimes++;
            txtClick.setText("クリック数：　" + clickTimes);

            if (rightAnswer == 8) {
                if (clickTimes == 16) {
                    Toast.makeText(HardActivity.this, clickTimes + "回クリックで完成! き、君は神か!", Toast.LENGTH_SHORT).show();
                } else if (clickTimes <= 32) {
                    Toast.makeText(HardActivity.this, clickTimes + "回クリックで完成。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HardActivity.this, clickTimes + "回クリックで完成…より一層の精進をすべし", Toast.LENGTH_SHORT).show();
                }

                if (rankHard == -1 || clickTimes < rankHard) {
                    SharedPreferences.Editor edtr = sp.edit();
                    edtr.putInt("HARD",clickTimes);
                    edtr.apply();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard);

        Button btnReset = findViewById(R.id.btnReset);
        Button btnBack = findViewById(R.id.btnBack);
        txt = findViewById(R.id.textView);
        txtClick = findViewById(R.id.txtClick);

        sp = getSharedPreferences("SoundGame36", Context.MODE_PRIVATE);
        rankHard = sp.getInt("HARD",-1);

        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(attr).setMaxStreams(8).build();
        sound1 = soundPool.load(this, R.raw.se_ok,1);
        sound2 = soundPool.load(this, R.raw.se_ng,1);


//        findViewById(R.id.ibtn01).setOnClickListener(new ButtonClickAction());
        for (int id : imgButtons) {
            ImageButton iBtn = findViewById(id);
            iBtn.setImageResource(R.drawable.neko01);
            iBtn.setOnClickListener(new HardActivity.ButtonClickAction());
        }
        initGame();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTimes = 0;
                rightAnswer = 0;
                preSelet = null;
                nowState = -1;
                txt.setText("");
                txtClick.setText("");
                initGame();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightAnswer == 8) {
                    Intent intent = getIntent();
                    intent.putExtra("HARD",clickTimes);
                    setResult(CODE_RANK_HARD,intent);
                }
                finish();
            }
        });
    }


    private void initGame() {
        int[] setItems = {
                R.raw.se_door01, R.raw.se_phone02, R.raw.se_whistle01, R.raw.se_drink02,
                R.raw.se_dog02, R.raw.se_human01, R.raw.se_magical06, R.raw.se_tiger01
        };

        buttonToSoundIdMap.clear();

        List<Integer> soundList = new ArrayList<>();
        for (int soundResource : setItems) {
            soundList.add(soundResource);
            soundList.add(soundResource);
        }

        List<Integer> buttonIdList = new ArrayList<>();
        for (int i = 0; i < imgButtons.length; i++) {
            buttonIdList.add(i);
        }
        Collections.shuffle(buttonIdList);

        for (int i = 0; i < imgButtons.length; i++) {
            int buttonId = buttonIdList.get(i);
            int soundResource = soundList.get(i);
            buttonToSoundIdMap.put(buttonId,soundResource);
        }
        Log.d(TAG, "initGame: map" + buttonToSoundIdMap);

        for (int i = 0; i < plays.length; i++) {
            int soundResource = buttonToSoundIdMap.get(i);
            plays[i] = MediaPlayer.create(this, soundResource);
        }
        Log.d(TAG, "initGame: plays" + Arrays.toString(plays));

        for (int buttonId : imgButtons) {
            ImageButton iBtn = findViewById(buttonId);
            iBtn.setImageResource(R.drawable.neko01);
            iBtn.setEnabled(true);
        }
    }
}