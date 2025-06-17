package com.example.appcartochka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appcartochka.databinding.FragmentHomeBinding;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements  RecyclerViewInterface {

    FragmentHomeBinding binding;
    List<item> items = new ArrayList<>();
    List<item> listItemsHelp = new ArrayList<>();

    NotesDataBaseHelper db;

    MyAdapter adapter;

    RecyclerViewInterface recyclerViewInterface;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,
                container,false);
        SharedPreferences sharedPreferences = getContext().
                getSharedPreferences("my_id",
                        Context.MODE_PRIVATE);
        int id = sharedPreferences.
                getInt("id", 0);
        db = new NotesDataBaseHelper(requireContext());
        List<item> listItems = db.getAllNotes(id);
         listItemsHelp = new ArrayList<>();
        item[] itemArray = listItems.
                toArray(new item[listItems.size()]);
        for(int i = 0;i < itemArray.length;i++){
            if(itemArray[i].user_id == id){
                listItemsHelp.
                        add(new item(itemArray[i].id,itemArray[i].name,
                        itemArray[i].user_id,itemArray[i].
                                address,itemArray[i].date,
                        itemArray[i].time));
                items.add(itemArray[i]);
            }
        }
        for(int i = 0;i < items.size();i++){
            LocalDateTime date = LocalDateTime.
                    parse(items.get(i).getDate());
            items.get(i).date = DateTimeFormatter.
                    ofPattern("yyyy-MM-dd",
                            Locale.ENGLISH).format(date);
        }
        Collections.reverse(items);
        Collections.reverse(listItemsHelp);



        binding.createReport.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                List<item> listItems = db.getAllNotes(id);
                List<item> filterItems = new ArrayList<item>();
                LocalDateTime endTime = LocalDateTime.now();
                LocalDateTime startTime = LocalDateTime.now().minusDays(7);

                for(int i = 0;i < listItems.size();i++){
                    LocalDateTime date = LocalDateTime.parse(listItems.get(i).getDate());
                    if(date.isBefore(endTime) && date.isAfter(startTime)){
                        filterItems.add(listItems.get(i));
                    }
                }
                if(filterItems.isEmpty()){
                    Toast.makeText(getContext(),"Нет маршрутов за этот период",Toast.LENGTH_SHORT).show();
                    return;
                }
                createPdf(filterItems,"неделю" + " ("  + startTime.toString().substring(0,10) + " - " +  endTime.toString().substring(0,10)  + ")");
            }
        });

















        recyclerViewInterface = new RecyclerViewInterface(){
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(requireContext(),
                        InfoNoteActivity.class);
                intent.putExtra("Name",
                        items.get(position).getName());
                intent.putExtra("Address",
                        items.get(position).getAddress());
                intent.putExtra("Time",
                        items.get(position).getTime());
                intent.putExtra("Date",
                        listItemsHelp.get(position).getDate());
                intent.putExtra("Id",
                        items.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                db.deleteNoteById(position);
                items.clear();
                List<item> listItems = db.getAllNotes(id);
                item[] itemArray = listItems.toArray(
                        new item[listItems.size()]);
                for(int i = 0;i < itemArray.length;i++){
                    if(itemArray[i].user_id == id){
                        listItemsHelp.add(new item(
                                itemArray[i].id,
                                itemArray[i].name,
                                itemArray[i].user_id,
                                itemArray[i].address,
                                itemArray[i].date,
                                itemArray[i].time));
                        items.add(itemArray[i]);
                    }
                }
                for(int i = 0;i < items.size();i++){
                    LocalDateTime date = LocalDateTime.
                            parse(items.get(i).getDate());
                    items.get(i).date = DateTimeFormatter.
                            ofPattern("yyyy-MM-dd",
                                    Locale.ENGLISH).format(date);
                }
                Collections.reverse(items);
                Collections.reverse(listItemsHelp);
                adapter.refreshData(items);
            }
        };

        adapter = new MyAdapter(requireContext(),
                items,recyclerViewInterface);
        binding.recyclerview.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
        binding.startShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),
                        "Вы начали смену",
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.addnewnote.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(requireContext(),
                    AddNewNoteActivity.class));
            }
        });
        return binding.getRoot();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getContext().
                getSharedPreferences("my_id",
                        Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        List<item> listItems = db.getAllNotes(id);
         listItemsHelp = new ArrayList<>();
        item[] itemArray = listItems.toArray(new item[listItems.size()]);
        items.clear();
        listItemsHelp.clear();
        for(int i = 0;i < itemArray.length;i++){
            if(itemArray[i].user_id == id){
                items.add(itemArray[i]);
                listItemsHelp.add(new item(itemArray[i].id,
                        itemArray[i].name,itemArray[i].user_id,
                        itemArray[i].address,itemArray[i].date,
                        itemArray[i].time));
            }
        }
        for(int i = 0;i < items.size();i++){
            LocalDateTime date = LocalDateTime.parse(
                    items.get(i).getDate());
            items.get(i).date = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd", Locale.ENGLISH).format(date);
        }
        Collections.reverse(items);
        Collections.reverse(listItemsHelp);
        adapter = new MyAdapter(requireContext(),
                items,recyclerViewInterface);
        binding.recyclerview.setLayoutManager(new
                LinearLayoutManager(requireContext()));
        binding.recyclerview.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireContext(),InfoNoteActivity.class);

        intent.putExtra("Name",items.get(position).getName());
        intent.putExtra("Address",items.get(position).getAddress());
        intent.putExtra("Time",items.get(position).getTime());
        intent.putExtra("Date",listItemsHelp.get(position).getDate());


        startActivity(intent);
    }



    @Override
    public void onDeleteClick(int position) {

    }

    private  void createPdf(List<item> items,String date)  {
        try{

            int minutes = 0;
            item[] itemArray = items.
                    toArray(new item[items.size()]);
            for(int i =0;i < itemArray.length;i++){
                minutes = minutes + Integer.parseInt(itemArray[i].time);
            }

            File[] dir = ContextCompat.getExternalFilesDirs(
                    getContext(), null);
            String path = dir[0] + "/PDF_Practice";
            File file = new File(path);
            String fontPath = "res/font/arial.ttf";
            PdfFont customFont = PdfFontFactory.createFont(fontPath);


            if(!file.exists()){
                file.mkdirs();
            }
            File pdf_file = new File(file.getAbsolutePath()
                    + "/MYPDF" +
                    getCurrentTime() + "_" + getTodayDate() +
                    ".pdf");
            if(!pdf_file.exists()){
                pdf_file.createNewFile();
            }
            PdfWriter writer = new PdfWriter(pdf_file.
                    getAbsoluteFile());
            PdfDocument pdfDocument = new
                    PdfDocument(writer);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            document.setHorizontalAlignment(HorizontalAlignment.CENTER);
            Paragraph paragraph = new Paragraph();
            paragraph.setMarginBottom(20);
            paragraph.add("Отчет за " + date).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setFont(customFont).setFontSize(36);
            paragraph.add("Часов отработано " + minutes/60).setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setFont(customFont).setFontSize(24);
            Table table = new Table(new float[]{1, 1, 1}).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER);

            table.addHeaderCell("Остановка").setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
            table.addHeaderCell("Адрес").setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
            table.addHeaderCell("Минут потребуется").setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);

            for(int i = 0; i < items.size();i++) {

                table.addCell(items.get(i).name).setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
                table.addCell(items.get(i).address).setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
                table.addCell(items.get(i).time).setTextAlignment(TextAlignment.CENTER).setFontSize(28).setFont(customFont);
            }
            document.add(paragraph);
            document.add(table);
            document.close();
            Toast.makeText(getContext(),"Отчет сформирован",
                    Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager =
                   requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FrameHome,
                    new ShareFragment(pdf_file.getAbsolutePath()));
            fragmentTransaction.commit();
        }
        catch (Exception e) {
            Log.i("client", e.getMessage().toString());
        }
    }


    private  String getCurrentTime(){
        return  new SimpleDateFormat("hh:mm a" ,Locale.getDefault()).format(new Date());
    }

    private  String getTodayDate(){
        return  new SimpleDateFormat("dd-MM-yyyy" ,Locale.getDefault()).format(new Date());
    }


}