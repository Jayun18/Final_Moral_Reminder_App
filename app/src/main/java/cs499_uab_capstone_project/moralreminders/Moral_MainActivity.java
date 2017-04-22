package cs499_uab_capstone_project.moralreminders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/*
 * This is the main class from where everything gets connected and actually displayed when the app is opened.
 * All of the buttons are connected to the app here and even the action handler after a button is clicked is handled here.
 * Call function is also set here. When a call is made, it will ask for permission and when it receives permission,
 * it will call that number.
 */
public class Moral_MainActivity extends AppCompatActivity {

    private String version = "";
    final int CALL_REQUEST_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moral__main);

        final Moral_Database moralDatabase = new Moral_Database(this);

        final Button happyButton = (Button) findViewById(R.id.happyButton);
        final Button sadButton = (Button) findViewById(R.id.sadButton);
        final Button angryButton = (Button) findViewById(R.id.angryButton);
        final Button loveButton = (Button) findViewById(R.id.loveButton);
        final Button lonelyButton = (Button) findViewById(R.id.lonelyButton);
        final Button faithButton = (Button) findViewById(R.id.faithButton);
        final Button updateButton = (Button) findViewById(R.id.update_button);
        final Button savedButton = (Button) findViewById(R.id.saved_button);
        final Button callButton = (Button) findViewById(R.id.button);

        final TextView versionNumber = (TextView) findViewById(R.id.version_number);
        versionNumber.setText(getVersionText(moralDatabase));

        final Intent newIntent = new Intent(this, MessageActivity.class);
        final Intent savedIntent = new Intent(this, SavedListActivity.class);

        /*
         * Handles the Happy button. When this button is clicked, quotes that are related to Happiness are
         * retrieved from the database. After it retrieves a quote, it takes user to a new screen and displays
         * the retrieved quote with a colorful background and soothing music.
         */
        happyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] message = moralDatabase.getMessage("Happy", false);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                newIntent.putExtras(b);
                startActivity(newIntent);
            }
        });

        /*
         * Handles the Sad button. When this button is clicked, quotes that are related to Sadness (basically happiness because
          * we want to get make user's mood better) are retrieved from the database. After it retrieves a quote, it takes user
          * to a new screen and displays the retrieved quote with a colorful background and soothing music.
         */
        sadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message[] = moralDatabase.getMessage("Sad", false);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                newIntent.putExtras(b);
                startActivity(newIntent);
            }
        });

        /*
         * Handles the Angry button. When this button is clicked, quotes that are related to Calmness are
         * retrieved from the database. After it retrieves a quote, it takes user to a new screen and displays
         * the retrieved quote with a colorful background and soothing music.
         */
        angryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message[] = moralDatabase.getMessage("Angry", false);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                newIntent.putExtras(b);
                startActivity(newIntent);            }
        });

        /*
         * Handles the Love button. When this button is clicked, quotes that are related to Love are
         * retrieved from the database. After it retrieves a quote, it takes user to a new screen and displays
         * the retrieved quote with a colorful background and soothing music.
         */
        loveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message[] = moralDatabase.getMessage("Love", false);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                newIntent.putExtras(b);
                startActivity(newIntent);            }
        });

        /*
         * Handles the Lonely button. When this button is clicked, quotes that are related to Happiness / Love are
         * retrieved from the database. After it retrieves a quote, it takes user to a new screen and displays
         * the retrieved quote with a colorful background and soothing music.
         */
        lonelyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message[] = moralDatabase.getMessage("Lonely", false);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                newIntent.putExtras(b);
                startActivity(newIntent);            }
        });

        /*
         * Handles the Faith button. When this button is clicked, quotes that are related to Faith are
         * retrieved from the database. After it retrieves a quote, it takes user to a new screen and displays
         * the retrieved quote with a colorful background and soothing music.
         */
        faithButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message[] = moralDatabase.getMessage("Faith", false);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                newIntent.putExtras(b);
                startActivity(newIntent);            }
        });

        /*
         * When saved button is clicked, it pulls out all the messages that are saved by the user.
         */
        savedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message[] = moralDatabase.getMessage("SAVED", true);
                Bundle b = new Bundle();
                b.putSerializable("message", message);
                savedIntent.putExtras(b);
                startActivity(savedIntent);            }
        });

        /*
         * Retrieves time for notification. Every 8 hours, it notifies the user.
         */
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int time =  calendar.get(Calendar.HOUR) + 8;
                calendar.set(Calendar.HOUR, time);
                getWebMessages(moralDatabase);
            }
        });

        /*
         * Handles calling button.
         * When this button is clicked, it asks the user if he / she really wants to make this call.
         * If so then it will make a call.
         */
        callButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    invokeCall();
                }
                else {
                    String[] permissionRequested = {Manifest.permission.CALL_PHONE};
                    requestPermissions(permissionRequested, CALL_REQUEST_CODE);
                }
            }
        });
    }

    /*
     * It retrieves permission from the user, because it is something outside of the app. In order to do something
     * outside of the app, the app needs to take permission from user.
     */
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                invokeCall();
            }
            else {
                Toast.makeText(this, "Unable to call without permission. ", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
     * It actually call the number.
     */
    private void invokeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:8605140407"));
        startActivity(callIntent);
    }


    public String getVersionText(Moral_Database db){
        try {
            String filePath = "versionNumber.txt";
            InputStream readVersion = openFileInput(filePath);
            if (readVersion != null) {
                InputStreamReader versionReader = new InputStreamReader(readVersion);
                BufferedReader br = new BufferedReader(versionReader);
                version = br.readLine();
            }
        } catch (FileNotFoundException e) {
            //Toast.makeText(getApplicationContext(), "Unable to determine version number - updating list!", Toast.LENGTH_LONG);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return version;
    }

    /*
     * Retrieves messages that I stored outside of the app. These messages were stored in dropbox.
     */
    public void getWebMessages(Moral_Database db){
        Message_Parser webParser = new Message_Parser(db);
        webParser.execute("https://www.dropbox.com/s/g7e2tm73zhmb9lh/data.txt?raw=1");
    }

    /*
     * It retrieves a quote and parses according to the needs.
     */
    private class Message_Parser extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        Moral_Database db;

        public Message_Parser(Moral_Database database){
            db = database;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Moral_MainActivity.this);
            pd.setMessage("Retrieving new messages, please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        protected  void onPostExecute(String result){
            TextView versionNumber = (TextView) findViewById(R.id.version_number);
            getVersionText(db);
            versionNumber.setText(version);
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader;

            try
            {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();
                FileOutputStream outputStream;
                if (!(line.equals(getVersionText(db))))
                {
                    try  {
                        outputStream = openFileOutput("versionNumber.txt", Context.MODE_PRIVATE);
                        outputStream.write(line.getBytes());
                        outputStream.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        String[] values = line.split("::");
                        for (int i = 0; i < values.length; i++){
                            if (values[i].contains("'")){
                                System.out.println("ADSFASDFASDFA");
                                values[i] = values[i].replace("'", "''");
                            }
                        }
                        System.out.println(values[0] + " " + values[1] + " " + values[2]);
                        db.updateDatabase(values[2], values[0], values[1]);
                    }

                }
            } catch (MalformedURLException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
