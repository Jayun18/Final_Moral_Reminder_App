package cs499_uab_capstone_project.moralreminders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import android.media.MediaPlayer;
import android.graphics.Color;

/*
 * This class the main part of the app : it takes the user to a new page and displays a quote with its author.
 * It is give users different background colors and a soothing music.
 * There is save button on the screen, which helps the user in saving quotes if they like it.
 */
public class MessageActivity extends AppCompatActivity {

    /*
     * A list that will store different music tracks
     */
    MediaPlayer [] emotionSong = new MediaPlayer[10];

    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        /*
         * All the stored music clips in the raw folder are added in emotionSong list
         */
        emotionSong[0] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundcute);
        emotionSong[1] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundhappiness);
        emotionSong[2] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundacousticbreeze);
        emotionSong[3] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundanewbeginning);
        emotionSong[4] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundbetterdays);
        emotionSong[5] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundenergy);
        emotionSong[6] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundlittleidea);
        emotionSong[7] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundmemories);
        emotionSong[8] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundthelounge);
        emotionSong[9] = MediaPlayer.create(MessageActivity.this, R.raw.bensoundfunnysong);


        /*
         * It retrieves a random number and based on that it generates a background color
         */
        Random rnd = new Random();
        int color = Color.argb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
        findViewById(android.R.id.content).setBackgroundColor(color);

        /*
         * It retrieves a random number and based on that it starts a music in the background
         */
        for (int i = 0; i < 1; i++) {
            Random r = new Random();
            n = r.nextInt(emotionSong.length);
            emotionSong[n].start();
        }

        final TextView quoteText = (TextView) findViewById(R.id.quote_text);
        final TextView authorText = (TextView) findViewById(R.id.author_info);
        final Button saveButton = (Button) findViewById(R.id.saveButton);

        /*
         * Retrieves quote from te database and displays it
         */
        Bundle bundle = this.getIntent().getExtras();
        final Moral_Database helper = new Moral_Database(this);
        String[] message = new String[2];
        if (bundle != null)
            message = bundle.getStringArray("message");
        final String quote = message[0];
        final String author = message[1];
        quoteText.setText(quote);
        authorText.setText(author);

        /*
         * It takes user to browser and searches the clicked part : author's information or quote
         */
        quoteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?espv=2&q=" + quoteText.getText()));
                    startActivity(browser);
                }
            }
        });
        authorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?espv=2&q=" + authorText.getText()));
                    startActivity(browser);
                }
            }
        });
        /*
         * It saves the quote
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    helper.saveQuote(quote, author);
                    saveButton.setEnabled(false);
                }
            }
        });
    }
    /*
     * Pauses the music
     */
    protected void onPause() {
        super.onPause();
        emotionSong[n].release();
        finish();
    }
}
