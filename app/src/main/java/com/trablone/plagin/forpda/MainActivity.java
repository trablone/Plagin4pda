package com.trablone.plagin.forpda;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity
{
	/* Called when the activity is first created. */
	Uri mUri;
	private String patch;
	private String fileout = "/mnt/sdcard/data/4pdaclient/styles/";
	private String plusFileOut = "mnt/sdcard/Android/data/org.softeg.slartus.forpdaplus/files/styles/"; 
	private String[] filesOut= {fileout, plusFileOut };
	private String nameFile;
	private String style;
	private String errorOpen = "Ошибка открытия стиля!";
	private String error = "Ошибка сохранения! Стиль удален или перемещен";
	private String saveOk = "Стиль успешно сохранен!";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plagin);

		final TextView white = (TextView)findViewById(R.id.textWhite);
		final TextView black = (TextView)findViewById(R.id.textBlack);
	
		Intent i = getIntent();
		String action = i.getAction();
		String type = i.getType();

		if (Intent.ACTION_VIEW.equals(action))
		{
			mUri = getIntent().getData();
			patch = mUri.getPath();
			if (patch != null)
				openNamedFile(patch);
			


		}
		

		white.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					if (nameFile != null)
					{
						copyWhite();
					}
					else
					{
						toastShow(error);
						finish();
					}
				}
			});
		black.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{

					if (nameFile != null)
					{
						copyBlack();
					}
					else
					{
						toastShow(error);
						finish();
					}
				}
			});
			
	}
	
	private void copyWhite(){
		style = "white";
		copyFile(style);
	}
	private void copyBlack(){
		style = "black";
		copyFile(style);
	}
	
	private void deleteOriginal()
	{
		File file = new File(patch);
		if (file.exists())
		{
			boolean deleted = file.delete();
		}
	}
	
	private void copyFile(final String style){
		findViewById(R.id.plaginLinearLayout).setVisibility(View.GONE);
		
		findViewById(R.id.plaginProgressBar).setVisibility(View.VISIBLE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){

				@Override
				public void run()
				{
					File in = new File(patch);
					for(String file : filesOut){
						File out = new File(file + style);
						com.trablone.plagin.forpda.EIfile.exportStyle(in, out, nameFile);
					}
					deleteOriginal();
					finish();
					toastShow(saveOk);
				}
			}, 500);
		
		
	}


	protected void openNamedFile(String patch)
	{
		try
		{
			File f = new File(patch);
			FileInputStream fis = new FileInputStream(f);

			long size = f.length();
			nameFile = f.getName();

			DataInputStream dis = new DataInputStream(fis);
			byte[] b = new byte[(int) size];
			int length = dis.read(b, 0, (int) size);

			dis.close();
			fis.close();

			String ttt = new String(b, 0, length, "UTF-8");
			
			if(nameFile.indexOf("white") >= 0){
				copyWhite();
			}
			if(nameFile.indexOf("black") >= 0){
				copyBlack();
			}

		}
		catch (FileNotFoundException e)
		{
			toastShow(errorOpen);
		}
		catch (IOException e)
		{
			toastShow(errorOpen);
		}
	}

	private void toastShow(String toastText)
	{
		Toast toast = Toast.makeText
		(this, toastText, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
