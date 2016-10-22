package bitterbug.hack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    String voterID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText voterid=(EditText)findViewById(R.id.voterid);
        Button login=(Button)findViewById(R.id.signin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voterID=voterid.getText().toString();
                Intent intent=new Intent(login.this,main.class);
                startActivity(intent);

            }
        });
    }
}
