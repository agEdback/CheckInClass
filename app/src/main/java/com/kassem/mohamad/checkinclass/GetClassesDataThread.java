package com.kassem.mohamad.checkinclass;

/**
 * Created by ALAA on 12/3/2017.

 */

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


class GetClassesDataThread extends AsyncTask<String, Void, String> {
    MainActivity m;
    ArrayList<Class> lc2;
    GetClassesDataThread(MainActivity m, ArrayList<Class> lc)
    {
        this.m=m;
        lc2=lc;
    }
    protected String doInBackground(String...params) {
        PrintWriter out;
        Scanner in;
        Socket s=null;
        String email=params[0];
        String who=params[1];
        try {
            //s = new Socket("192.168.43.157",8082);
            s=new Socket();
            //s.connect(new InetSocketAddress("192.168.43.157",8082),4000); // alaa server by phn
            s.connect(new InetSocketAddress(m.getResources().getString(R.string.server_ip),8082),4000);
            in =new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream(),true);
            out.println(params[1]+"--#--"+email);
            String more=in.nextLine();
            int nb=0;
            while(true) {
                if(more.equals(new String("end")))
                    break;
                nb++;
                String name = in.nextLine();
                String id = in.nextLine();
                String location = in.nextLine();
                Class C=new Class(name,id,email,location);
                if(params[1].equals("profClasses"))m.db.addClass(C);
                else lc2.add(C);
                more=in.nextLine();

            }
            s.close();
            return who+"//done:"+nb;
        }
        catch (Exception e)
        {
            //error=e.getMessage();
            try {if(s!=null)s.close();}
            catch (IOException e1) {}
            finally {return who+"//failure";}
        }

    }
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
        m.result=r.split("//")[1];
        if(r.split("//")[0].equals("profClasses"))
            m.finishGetClasses();
        else m.finishStudentClasses();
    }
}
