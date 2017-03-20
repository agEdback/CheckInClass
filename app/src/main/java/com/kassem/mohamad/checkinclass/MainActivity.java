package com.kassem.mohamad.checkinclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static int request_code = 1;
    public String result;
    private String email;
    public MainActivity m;
    String nametocreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m=this;
        System.out.println("create");
        Toast.makeText(getApplicationContext(),"create",Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if(tabLayout.getSelectedTabPosition() == 0){
                    Toast.makeText(getApplicationContext(),"student",Toast.LENGTH_LONG).show();
                    addClass();
                }
                else if(tabLayout.getSelectedTabPosition() == 1){
                    Toast.makeText(getApplicationContext(),"professor",Toast.LENGTH_LONG).show();
                    create_class();
                }

            }
        });

        // check if already login
        email = "";
        try{
            //File file = new File("login.txt");
          //  if(!file.exists()){
             //   System.out.println("main1");
             //   Intent intent = new Intent(this, LoginActivity.class);
             //   startActivityForResult(intent,request_code);
           // }
          //  else {
                FileInputStream inputStream = openFileInput("login");
                int i;
                while((i=inputStream.read()) != -1){
                    email += String.valueOf((char)i);
                }
                if(email.equals("")){
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent,request_code);
                }
         //   }
        }
        catch (Exception ex){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,request_code);
        }
    //refreshProfData();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"settings", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_sign_out:
                Toast.makeText(getApplicationContext(),"sign out", Toast.LENGTH_LONG).show();
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void signOut(){
        String loginfile = "login";
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput(loginfile, Context.MODE_PRIVATE);
            outputStream.write(new String("").getBytes());
            outputStream.close();
            //this.onCreate(null);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception ex){
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // Returning the current tabs
            switch (position){
                case 0:
                StudentTab studentTab = new StudentTab();
                    return studentTab;
                case 1:
                    ProfessorTab professorTab = new ProfessorTab();
                    return  professorTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Student";
                case 1:
                    return "Professor";

            }
            return null;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    // Registration to a new class
    private void addClass(){
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_class_linear_layout);
        linearLayout.removeAllViews();


        final EditText editText = new EditText(this);
        editText.setHint("Enter The ID");
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));

        ImageButton searchButton = new ImageButton(this);
        int idSearchbButton = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_search_black_24dp" , null, null);
        searchButton.setImageResource(idSearchbButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search for the new class

               try{
                   String id= editText.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Search a class ...");
                progressDialog.show();

                result="";
                AddClassThread a=new AddClassThread(m,id);
                a.execute();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run()
                            {
                                editText.setText(result);
                                progressDialog.dismiss();
                            }
                        }, 5000);
               }
               catch(Exception e){}
            }
        });

        ImageButton exitButton = new ImageButton(this);
        int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_close_black_24dp" , null, null);
        exitButton.setImageResource(idExitImage);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
            }
        });

        linearLayout.addView(editText);
        linearLayout.addView(searchButton);
        linearLayout.addView(exitButton);
    }
    private void create_class()
    {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.prof_linear_layout);
        linearLayout.removeAllViews();
        final EditText editText = new EditText(this);
        editText.setHint("Class name");
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));
        ImageButton createButton = new ImageButton(this);
        int idButton = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_add_box_black_24dp" , null, null);
        createButton.setImageResource(idButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search for the new class

                try{
                    nametocreate= editText.getText().toString();
                    if(!nametocreate.equals("")) {
                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("creating ...");
                        progressDialog.show();

                        result = "";
                        CreateClassThread a = new CreateClassThread(m, email, nametocreate);
                        a.execute();
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        if(result.equals("failure:0"))
                                            Toast.makeText(getApplicationContext(),"failure", Toast.LENGTH_LONG).show();
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"not failure!", Toast.LENGTH_LONG).show();

                                            try
                                            {
                                                FileInputStream inputStream = openFileInput("profClass");
                                                int i;
                                                Toast.makeText(getApplicationContext(),"open the file!", Toast.LENGTH_LONG).show();

                                                String data="";
                                                while ((i = inputStream.read()) != -1)
                                                {
                                                    data += String.valueOf((char) i);
                                                }
                                                data+="--#--"+nametocreate+"--#--"+result.split(":")[1];
                                                inputStream.close();
                                                Toast.makeText(getApplicationContext(),"getthedata!", Toast.LENGTH_LONG).show();

                                                FileOutputStream outputStream;
                                                String filename="profClass";
                                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                outputStream.write(data.getBytes());
                                                outputStream.close();
                                            }
                                            catch(Exception e)
                                            {
                                                FileOutputStream outputStream;
                                                String filename="profClass";
                                                Toast.makeText(getApplicationContext(),"profclass first time!", Toast.LENGTH_LONG).show();
                                                try
                                                {
                                                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                                    outputStream.write(new String(nametocreate+"--#--"+result.split(":")[1]).getBytes());
                                                    outputStream.close();
                                                }
                                                catch (IOException e1){}

                                            }
                                            refreshProfData();
                                        }
                                        progressDialog.dismiss();
                                    }
                                }, 5000);
                    }else Toast.makeText(getApplicationContext(),"enter a class name!", Toast.LENGTH_LONG).show();
                }
                catch(Exception e){}

            }
        });
        ImageButton exitButton = new ImageButton(this);
        int idExitImage = getResources().getIdentifier("com.kassem.mohamad.checkinclass:drawable/ic_close_black_24dp" , null, null);
        exitButton.setImageResource(idExitImage);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
            }
        });

        linearLayout.addView(editText);
        linearLayout.addView(createButton);
        linearLayout.addView(exitButton);
    }
    private void refreshProfData()
    {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.prof_data);
        linearLayout.removeAllViews();
        FileInputStream inputStream = null;
        try {
            String s2="";
            inputStream = openFileInput("profClass");
            int i;
            while((i=inputStream.read()) != -1){
                s2 += String.valueOf((char)i);
            }
            if(!s2.equals(""))
            {
                String[] data=s2.split("--#--");
                int len;
                for(len=0;len<data.length;len++)
                {
                    TextView t=new TextView(this);
                    t.setText(data[len]);
                    linearLayout.addView(t);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void onClassExist(){

    }
}
