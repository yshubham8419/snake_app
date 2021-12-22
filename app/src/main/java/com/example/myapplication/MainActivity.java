package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int first=6,last=0 ,direction=1;
    boolean on = true;
    int[] snake=new int[7];
    ArrayAdapter<String> adapter;
    String[] a = new String[200];
    Random r=new Random();
    Context c=this;
    int food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView g = findViewById(R.id.grid);

        for (int i = 0; i <=6; i++) {
            a[i] = "@";
            snake[i]=6-i;
        }

        for(int i=7;i<200;i++)
            a[i]=" ";


        food=r.nextInt(200);
        a[food]="$";
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_gallery_item, a);
        g.setAdapter(adapter);
        ImageButton b0 = findViewById(R.id.up);
        ImageButton b1 = findViewById(R.id.right);
        ImageButton b2 = findViewById(R.id.down);
        ImageButton b3 = findViewById(R.id.left);
        b0.setOnClickListener(v -> direction = 0);
        b1.setOnClickListener(v -> direction = 1);
        b2.setOnClickListener(v -> direction = 2);
        b3.setOnClickListener(v -> direction = 3);
        Thread t = new Thread(() -> {
            while (on)
               {

                  a = updateArray(a);
                  adapter = new ArrayAdapter<>(c, android.R.layout.simple_gallery_item, a);
                   if(first==food) {
                       updatef();
                       food = r.nextInt(200);
                       a[food] = "$";
                       snake=increase();
                   }
                   else {
                       updatel();
                       updatef();
                       updatesnake();
                   }
                  runOnUiThread(() -> g.setAdapter(adapter));
                  try {
                    Thread.sleep(500);
                   }catch(Exception e) {
                      e.printStackTrace();
                  }

                  on=!checkselfbite();
               }
            for(int i=6;i<14;i++)
            {
                String s="GameOver";
                a[60+i]=s.charAt(i-6)+"";
                adapter = new ArrayAdapter<>(c, android.R.layout.simple_gallery_item, a);
                runOnUiThread(() -> g.setAdapter(adapter));
            }

        });

        t.start();

    }

    int[] increase()
    {
        int[] x = new int[snake.length+1];
        for(int i=0;i<snake.length;i++)
            x[i+1]=snake[i];
        x[0]=first;
        return x;
    }

    void updatel()
    {
        last=snake[snake.length-1];
    }

    boolean checkselfbite()
    {
        for(int i=1;i<snake.length;i++)
        if(first==snake[i])
            return true;
        return false;

    }
    protected void onDestroy(){
        on=false;
        super.onDestroy();
    }


    void updatesnake() {
        for(int i=snake.length-1; i>0;i--)
            snake[i]=snake[i-1];
        snake[0]=first;

    }
    void updatef() {
        if (direction == 0)
        {
            if(first<=19)
                first=first+180;
            else
                first=first-20;
        }
        else if (direction == 1)
        {
            if(first%20==19)
                first=first-19;
            else
                first=first+1;
        }
        else if (direction == 2)
        {
            if(first>=180)
                first=first-180;
            else
                first=first+20;
        }
        else
        {

            if(first%20==0)
                first=first+19;
            else
                first=first-1;
        }

    }
    String[] updateArray( String[] old) {

        if (direction == 0)
            return up(old);
        else if (direction == 1)
            return right(old);
        else if (direction == 2)
            return down(old);
        return left(old );
    }
    String[] up(String[] old) {
        old[last] = " ";
        int[] twodp = get2dpos(first);
        if (twodp[0] == 0)
            old[180 + first] = "@";
        else
            old[first - 20] = "@";
        return old;
    }
    String[] down(String[] old) {

        old[last] = " ";
        int[] twodp = get2dpos(first);
        if (first>=180)
            old[first - 180] = "@";
        else
            old[first + 20] = "@";
        return old;

    }
    String[] left(String[] old) {

        old[last] = " ";
        int[] twodp = get2dpos(first);
        if (twodp[1] == 0)
            old[19 + first] = "@";
        else
            old[first - 1] = "@";
        return old;

    }
    String[] right(String[] old) {

        old[last] = " ";
        int[] twodp = get2dpos(first);
        if (twodp[1] == 19)
            old[first - 19] = "@";
        else
            old[first + 1] = "@";
        return old;
    }
    int[] get2dpos(int oned) {

        int[] pos = new int[2];
        pos[0] =  oned / 20;
        pos[1] =  oned % 20;
        return pos;
    }


}

