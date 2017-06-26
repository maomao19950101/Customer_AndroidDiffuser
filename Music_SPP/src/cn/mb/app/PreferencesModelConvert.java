package cn.mb.app;

import cn.mb.model.ClockSetModel;
import cn.mb.util.PreferencesUtils;

public class PreferencesModelConvert {

	public ClockSetModel getClockSetModel(){
		ClockSetModel setModel=new ClockSetModel();
		
		setModel.setFogOnOff(PreferencesUtils.getBoolean(AppAplication.getApplication(), AppConstant.KEY.FOG_ONOFF, false));
		setModel.setFogOnTime(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.FOG_ONOFF_ON, "00:00"));
		setModel.setFogOffTime(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.FOG_ONOFF_OFF, "24:00"));
		
		setModel.setLightOnOff(PreferencesUtils.getBoolean(AppAplication.getApplication(), AppConstant.KEY.LIGHT_ONOFF, false));
		setModel.setLightOnTime(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.LIGHT_ONOFF_ON, "00:00"));
		setModel.setLightOffTime(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.LIGHT_ONOFF_OFF, "24:00"));
		
		setModel.setMusicOnOff(PreferencesUtils.getBoolean(AppAplication.getApplication(), AppConstant.KEY.MUSIC_ONOFF, false));
		setModel.setMusicOnTime(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.MUSIC_ONOFF_ON, "00:00"));
		setModel.setMusicOffTime(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.MUSIC_ONOFF_OFF, "24:00"));
		return setModel;
	}
}
