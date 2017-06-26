package com.simple.player.music.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.simple.player.music.entity.MusicMessage;

public class MusicData {
	public static List<MusicMessage> allMusicList = new ArrayList<MusicMessage>();
	
	String DATA =  MediaStore.Audio.Media.DATA;
	String TITLE = MediaStore.Audio.Media.TITLE;
	String DURATION = MediaStore.Audio.Media.DURATION;
	String ARTIST_ID = MediaStore.Audio.Media.ARTIST_ID;
	String ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID;
	String YEAR = MediaStore.Audio.Media.YEAR;

	String ARTIST = MediaStore.Audio.Media.ARTIST;
	String ALBUM = MediaStore.Audio.Media.ALBUM;
	
	
	
	Uri uri_meta = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	Uri uri_albums = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
	Uri uri_artist = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

	
	
	
	public  void initAllMusicList(Context context) { 
		Cursor cs_meta = null;
		try{
		cs_meta = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			
		if(cs_meta != null && cs_meta.getCount() > 0){
			while (cs_meta.moveToNext()) {
				MusicMessage allMusicMessage = new MusicMessage();
				long id = cs_meta.getLong(cs_meta.getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
				String title = cs_meta.getString((cs_meta.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
				String artist = cs_meta.getString(cs_meta.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
				long duration = cs_meta.getLong(cs_meta.getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
				long size = cs_meta.getLong(cs_meta.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
				String url = cs_meta.getString(cs_meta.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
				int isMusic = cs_meta.getInt(cs_meta.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
				String albums = cs_meta.getString(cs_meta.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				
				if (isMusic != 0) { // 只把音乐添加到集合当中
					
					allMusicMessage.set_data(url);
					allMusicMessage.setTitle(title);
					allMusicMessage.setDuration((int)duration);  
					allMusicMessage.setArtist(artist);
					allMusicMessage.setAlbum(albums);
					allMusicMessage.setSize(size);
					allMusicMessage.set_id(id);
					     
				
				String year = String.valueOf(cs_meta.getInt(cs_meta.getColumnIndex(MediaStore.Audio.Media.YEAR)));
				if(year.equals("0")){
					year = "未知年份";
				}
				allMusicMessage.setYear(year);

//				int artist_id = cs_meta.getInt(3);
//				int album_id = cs_meta.getInt(4);
//				Cursor cs_albums=null;
//				try{
//				 cs_albums = context.getContentResolver().query(uri_albums, null, 
//						MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);
//
//				if(cs_albums != null && cs_albums.getCount() > 0){
//					cs_albums.moveToFirst();
//					String album = cs_albums.getString(1);
//					if(album.equalsIgnoreCase("sdcard")){
//						album = "未知专辑";
//					}
//					allMusicMessage.setAlbum(album);
//				}
//				}finally{
//					if(cs_albums!=null&&!cs_albums.isClosed()){cs_albums.close();cs_albums=null;}
//				}
//				
//				Cursor cs_artist =null;
//				try{
//				 cs_artist = context.getContentResolver().query(uri_artist, 
//						null, MediaStore.Audio.Artists._ID + "=?", new String[]{String.valueOf(artist_id)}, null);
//
//				if(cs_artist != null && cs_artist.getCount() > 0){
//					cs_artist.moveToFirst();
//					String artist = cs_artist.getString(1);
//					if(artist.equalsIgnoreCase("<unknown>")){
//						artist = "未知歌手";
//					}
//					allMusicMessage.setArtist(artist);
//				}
				MusicData.allMusicList.add(allMusicMessage);
//				}finally{
//					if(cs_artist!=null&&!cs_artist.isClosed()){cs_artist.close();cs_artist=null;}
//				}
				}  
			}
		}
		if(cs_meta!=null&&!cs_meta.isClosed()){cs_meta.close();cs_meta=null;}
		}finally{
			if(cs_meta!=null&&!cs_meta.isClosed()){cs_meta.close();cs_meta=null;}
		}
	}
	
	public  void initAllMusicList2(Context context) { 
		Cursor cs_meta = null;
		try{
		 cs_meta = context.getContentResolver().query(uri_meta, new String[]{DATA, TITLE, DURATION, ARTIST, ALBUM, YEAR}, DURATION+">? ", new String[]{String.valueOf(50*1000)}, TITLE +" DESC");  
		if(cs_meta != null && cs_meta.getCount() > 0){
			while (cs_meta.moveToNext()) {
				MusicMessage allMusicMessage = new MusicMessage();
				allMusicMessage.set_data(cs_meta.getString(0));
				allMusicMessage.setTitle(cs_meta.getString(1));
				allMusicMessage.setDuration(cs_meta.getInt(2));  
				allMusicMessage.setArtist(cs_meta.getString(3));
				allMusicMessage.setAlbum(cs_meta.getString(4));
				String year = String.valueOf(cs_meta.getInt(5));
				if(year.equals("0")){
					year = "未知年份";
				}
				allMusicMessage.setYear(year);

				int artist_id = cs_meta.getInt(3);
				int album_id = cs_meta.getInt(4);
				Cursor cs_albums=null;
				try{
				 cs_albums = context.getContentResolver().query(uri_albums, null, 
						MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(album_id)}, null);

				if(cs_albums != null && cs_albums.getCount() > 0){
					cs_albums.moveToFirst();
					String album = cs_albums.getString(1);
					if(album.equalsIgnoreCase("sdcard")){
						album = "未知专辑";
					}
					allMusicMessage.setAlbum(album);
				}
				}finally{
					if(cs_albums!=null&&!cs_albums.isClosed()){cs_albums.close();cs_albums=null;}
				}
				
				Cursor cs_artist =null;
				try{
				 cs_artist = context.getContentResolver().query(uri_artist, 
						null, MediaStore.Audio.Artists._ID + "=?", new String[]{String.valueOf(artist_id)}, null);

				if(cs_artist != null && cs_artist.getCount() > 0){
					cs_artist.moveToFirst();
					String artist = cs_artist.getString(1);
					if(artist.equalsIgnoreCase("<unknown>")){
						artist = "未知歌手";
					}
					allMusicMessage.setArtist(artist);
				}
				MusicData.allMusicList.add(allMusicMessage);
				}finally{
					if(cs_artist!=null&&!cs_artist.isClosed()){cs_artist.close();cs_artist=null;}
				}
			}
		}
		if(cs_meta!=null&&!cs_meta.isClosed()){cs_meta.close();cs_meta=null;}
		}finally{
			if(cs_meta!=null&&!cs_meta.isClosed()){cs_meta.close();cs_meta=null;}
		}
	}
	
}
