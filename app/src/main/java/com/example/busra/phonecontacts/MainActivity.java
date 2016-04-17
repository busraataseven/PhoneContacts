package com.example.busra.phonecontacts;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener {


    private ListView listView;
    private List<ContactBean> list = new ArrayList<ContactBean>();
    private List<ContactBean> list_turkcell = new ArrayList<ContactBean>();
    private List<ContactBean> list_avea = new ArrayList<ContactBean>();
    private List<ContactBean> list_vodafone = new ArrayList<ContactBean>();

    private List<ContactBean> list_recovery = new ArrayList<ContactBean>();
    private List<ContactBean> list_recovery_turkcell = new ArrayList<ContactBean>();
    private List<ContactBean> list_recover_avea = new ArrayList<ContactBean>();
    private List<ContactBean> list_recover_vodafone = new ArrayList<ContactBean>();

    boolean recovery_existent = false;

    RadioButton allOprs;
    RadioButton avea;
    RadioButton turkcell;
    RadioButton vodafone;

    Button backUp;

    ContactAdapter objAdapter; //all operators
    ContactAdapter objAdapter2; //turkcell
    ContactAdapter objAdapter3; //vodafone
    ContactAdapter objAdapter4; //avea

    String temp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backUp = (Button)findViewById(R.id.backUp);

        listView = (ListView) findViewById(R.id.myList);
        listView.setOnItemClickListener(this);

        allOprs = (RadioButton)findViewById(R.id.all);
        avea = (RadioButton)findViewById(R.id.avea);
        turkcell = (RadioButton)findViewById(R.id.turkcell);
        vodafone = (RadioButton)findViewById(R.id.vodafone);

        // getting the contacts
        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            if(!phoneNumber.equals(temp))
            {
                ContactBean objContact = new ContactBean();
                objContact.setName(name);
                objContact.setPhoneNo(phoneNumber);

                list.add(objContact);

                //seperating the numbers in each operators
                if(phoneNumber.substring(0,3).equals("053")||phoneNumber.substring(0,6).equals("+90 53"))
                {
                    String sub_phone = phoneNumber.substring(0,3);
                    list_turkcell.add(objContact);
                }

                if(phoneNumber.substring(0,3).equals("054")||phoneNumber.substring(0,6).equals("+90 54"))
                {
                    String sub_phone = phoneNumber.substring(0,3);
                    list_vodafone.add(objContact);
                }

                if(phoneNumber.substring(0,3).equals("050")||phoneNumber.substring(0,6).equals("+90 50")||phoneNumber.substring(0,3).equals("055")||phoneNumber.substring(0,6).equals("+90 55"))
                {
                    String sub_phone = phoneNumber.substring(0,3);
                    list_avea.add(objContact);
                }
            }

            temp = phoneNumber;


        } //End while
        phones.close();

        objAdapter = new ContactAdapter(
                MainActivity.this, R.layout.contact_list_row, list);

        objAdapter2 = new ContactAdapter(
                MainActivity.this, R.layout.contact_list_row, list_turkcell);

        objAdapter3 = new ContactAdapter(
                MainActivity.this, R.layout.contact_list_row, list_vodafone);

        objAdapter4 = new ContactAdapter(
                MainActivity.this, R.layout.contact_list_row,list_avea);


        //sorting the contact names in alphabetic order
        Collections.sort(list, new Comparator<ContactBean>() {

            @Override
            public int compare(ContactBean lhs, ContactBean rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        Collections.sort(list_turkcell, new Comparator<ContactBean>() {

            @Override
            public int compare(ContactBean lhs, ContactBean rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        Collections.sort(list_vodafone, new Comparator<ContactBean>() {

            @Override
            public int compare(ContactBean lhs, ContactBean rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        Collections.sort(list_avea, new Comparator<ContactBean>() {

            @Override
            public int compare(ContactBean lhs, ContactBean rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });


    } //onCreate end



    public void allOprs(View view) {

        listView.setAdapter(objAdapter);
    }


    public void TurkcellClk(View view) {

        listView.setAdapter(objAdapter2);
    }


    public void VodafoneClk(View view) {

        listView.setAdapter(objAdapter3);
    }


    public void AveaClk(View view) {

        listView.setAdapter(objAdapter4);
    }


    public void recover(View v)
    {
        getFileandBackUp();
    }


    //Back-Up method
    public void backUp(View v)
    {
        String data="";
        FileOutputStream writer=null;
        File file = getFileStreamPath("backup.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(),"File is created",Toast.LENGTH_LONG).show();

        }

        try {
            writer  = openFileOutput(file.getName(),MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        if(writer!=null)
        {
            try {
                data= gson.toJson(list).toString();
                writer.write(gson.toJson(list).getBytes());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //When backup clicked it writes to the file
        Toast.makeText(getApplicationContext(),"Backup has been taken",Toast.LENGTH_SHORT).show();

    }


    private void getFileandBackUp()
    {
        String temp_recovery="";
        StringBuffer stringBuffer = new StringBuffer();
        ContactBean[] backup_data=null;


        try {

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("backup.txt")));
            String inputString="";

            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString);

            }

            Gson gson = new Gson();
            backup_data = gson.fromJson(stringBuffer.toString(),ContactBean[].class);




        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<backup_data.length;i++){
            boolean isExistent = true;

            String existing_name="",existing_phone="",back_name="",back_phone="";


            back_name=backup_data[i].getName();
            back_phone=backup_data[i].getPhoneNo();
            for(int j=0;j<list.size();j++){

                existing_name=list.get(j).getName();
                existing_phone=list.get(j).getPhoneNo();
                if(existing_name.equals(back_name) && existing_phone.equals(back_phone))
                    isExistent=false;

            }
            if(isExistent){
                addContact(back_name,back_phone);
            }
        }



        Toast.makeText(getApplicationContext(),"Recovery Completed!",Toast.LENGTH_SHORT).show();

        if(!recovery_existent)
        {
            Cursor phones_recovery = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                    null, null);
            while (phones_recovery.moveToNext()) {

                String name = phones_recovery
                        .getString(phones_recovery
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phoneNumber = phones_recovery
                        .getString(phones_recovery
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                if(!phoneNumber.equals(temp_recovery))
                {
                    ContactBean objContact = new ContactBean();
                    objContact.setName(name);
                    objContact.setPhoneNo(phoneNumber);

                    list_recovery.add(objContact);

                    //seperating the numbers in each operators
                    if(phoneNumber.substring(0,3).equals("053")||phoneNumber.substring(0,5).equals("+90 53"))
                    {
                        String sub_phone = phoneNumber.substring(0,3);
                        list_recovery_turkcell.add(objContact);
                    }

                    if(phoneNumber.substring(0,3).equals("054")||phoneNumber.substring(0,5).equals("+90 54"))
                    {
                        String sub_phone = phoneNumber.substring(0,3);
                        list_recover_vodafone.add(objContact);
                    }

                    if(phoneNumber.substring(0,3).equals("050")||phoneNumber.substring(0,5).equals("+90 50")||phoneNumber.substring(0,3).equals("055")||phoneNumber.substring(0,5).equals("+90 55"))
                    {
                        String sub_phone = phoneNumber.substring(0,3);
                        list_recover_avea.add(objContact);
                    }
                }

                temp_recovery = phoneNumber;


            } //End While
            phones_recovery.close();

            objAdapter = new ContactAdapter(
                    MainActivity.this, R.layout.contact_list_row, list_recovery);

            objAdapter2 = new ContactAdapter(
                    MainActivity.this, R.layout.contact_list_row, list_recovery_turkcell);

            objAdapter3 = new ContactAdapter(
                    MainActivity.this, R.layout.contact_list_row, list_recover_vodafone);

            objAdapter4 = new ContactAdapter(
                    MainActivity.this, R.layout.contact_list_row,list_recover_avea);


            //sorting the numbers in alphabetic order
            Collections.sort(list, new Comparator<ContactBean>() {

                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            Collections.sort(list_turkcell, new Comparator<ContactBean>() {

                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            Collections.sort(list_vodafone, new Comparator<ContactBean>() {

                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            Collections.sort(list_avea, new Comparator<ContactBean>() {

                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            listView.setAdapter(objAdapter);
        }

        recovery_existent=true;

    }


    private void addContact(String name, String phone) {

        ContentValues contvals = new ContentValues();
        contvals.put(Contacts.People.NAME, name);
        Uri u = getContentResolver().insert(Contacts.People.CONTENT_URI, contvals);
        Uri pathu = Uri.withAppendedPath(u, Contacts.People.Phones.CONTENT_DIRECTORY);
        contvals.clear();
        contvals.put(Contacts.People.NUMBER, phone);
        getContentResolver().insert(pathu, contvals);
        Toast.makeText(getApplicationContext(), "Backup-Completed", Toast.LENGTH_LONG).show();

    }//end


    //DIALING A NUMBER
    private void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> listview, View v, int position,
                            long id) {
        ContactBean bean = (ContactBean) listview.getItemAtPosition(position);
        showCallDialog(bean.getName(), bean.getPhoneNo());
    }


    private void showCallDialog(String name, final String phoneNo) {
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .create();
        alert.setTitle("Call?");

        alert.setMessage("Are you sure want to call " + name + " ?");

        alert.setButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setButton2("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = "tel:" + phoneNo;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                        .parse(phoneNumber));
                startActivity(intent);
            }
        });
        alert.show();
    }


}
