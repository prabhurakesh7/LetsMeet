package ras.asu.com.letsmeet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class FBCheckPart1 extends AppCompatActivity {
    Fragment fragmentOne;
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbcheck_part1);
        Intent intent = getIntent();
        ProjCostants.PNAME =intent.getStringExtra("pName");
        ProjCostants.PLAT=intent.getStringExtra("pLat");
        ProjCostants.PLONG=intent.getStringExtra("pLong");
        Button fab = (Button) findViewById(R.id.shareIt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
try {

    setContentView(R.layout.frame_layout);

    fragmentOne = new FBCheckin();

    fragmentTransaction.add(R.id.frameLayoutFragmentContainer, fragmentOne);
    fragmentTransaction.addToBackStack(null);

    fragmentTransaction.commit();
}
catch(Exception e)
{
    e.printStackTrace();
}
            }
        });
    }

}
