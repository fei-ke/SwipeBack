package us.shandian.mod.swipeback.ui.preference;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

public class BlackWhiteSwitchPreference extends SwitchPreference
{
	public BlackWhiteSwitchPreference(Context context) {
		super(context);
	}
	
	public BlackWhiteSwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BlackWhiteSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onClick() {
	}
}
