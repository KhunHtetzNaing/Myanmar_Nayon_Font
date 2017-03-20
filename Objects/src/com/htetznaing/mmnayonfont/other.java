package com.htetznaing.mmnayonfont;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class other extends Activity implements B4AActivity{
	public static other mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mmnayonfont", "com.htetznaing.mmnayonfont.other");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (other).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mmnayonfont", "com.htetznaing.mmnayonfont.other");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mmnayonfont.other", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (other) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (other) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return other.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (other) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (other) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _ad1 = null;
public static anywheresoftware.b4a.objects.Timer _ad2 = null;
public static anywheresoftware.b4a.objects.Timer _ti = null;
public static anywheresoftware.b4a.objects.Timer _tr = null;
public static anywheresoftware.b4a.objects.Timer _ist = null;
public static anywheresoftware.b4a.objects.Timer _rst = null;
public anywheresoftware.b4a.phone.Phone _ph = null;
public static String _os = "";
public anywheresoftware.b4a.objects.LabelWrapper _b1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _b2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _b3 = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _interstitial = null;
public MLfiles.Fileslib.MLfiles _ml = null;
public com.htetznaing.mmnayonfont.slidemenu _sm = null;
public anywheresoftware.b4a.objects.LabelWrapper _tlb = null;
public anywheresoftware.b4a.objects.ButtonWrapper _menu = null;
public anywheresoftware.b4a.objects.ButtonWrapper _share = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _sbg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _mbg = null;
public b4a.util.BClipboard _copy = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.objects.LabelWrapper _lw = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _mm = null;
public static String _rooot = "";
public com.htetznaing.mmnayonfont.main _main = null;
public com.htetznaing.mmnayonfont.main2 _main2 = null;
public com.htetznaing.mmnayonfont.samsung _samsung = null;
public com.htetznaing.mmnayonfont.oppo _oppo = null;
public com.htetznaing.mmnayonfont.huawei _huawei = null;
public com.htetznaing.mmnayonfont.vivo _vivo = null;
public com.htetznaing.mmnayonfont.tutorial _tutorial = null;
public com.htetznaing.mmnayonfont.about _about = null;
public com.htetznaing.mmnayonfont.xiaomi _xiaomi = null;
public com.htetznaing.mmnayonfont.msg _msg = null;
public com.htetznaing.mmnayonfont.msg1 _msg1 = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _b1bg = null;
int _height = 0;
 //BA.debugLineNum = 31;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="Select ph.SdkVersion";
switch (BA.switchObjectToInt(mostCurrent._ph.getSdkVersion(),(int) (2),(int) (3),(int) (4),(int) (5),(int) (6),(int) (7),(int) (8),(int) (9),(int) (10),(int) (11),(int) (12),(int) (13),(int) (14),(int) (15),(int) (16),(int) (17),(int) (18),(int) (19),(int) (21),(int) (22),(int) (23),(int) (24),(int) (25))) {
case 0: {
 //BA.debugLineNum = 34;BA.debugLine="Case 2 : OS = \"1.1\"";
mostCurrent._os = "1.1";
 break; }
case 1: {
 //BA.debugLineNum = 35;BA.debugLine="Case 3 : OS = \"1.5\"";
mostCurrent._os = "1.5";
 break; }
case 2: {
 //BA.debugLineNum = 36;BA.debugLine="Case 4 : OS = \"1.6\"";
mostCurrent._os = "1.6";
 break; }
case 3: {
 //BA.debugLineNum = 37;BA.debugLine="Case 5 : OS = \"2.0\"";
mostCurrent._os = "2.0";
 break; }
case 4: {
 //BA.debugLineNum = 38;BA.debugLine="Case 6 : OS = \"2.0.1\"";
mostCurrent._os = "2.0.1";
 break; }
case 5: {
 //BA.debugLineNum = 39;BA.debugLine="Case 7 : OS = \"2.1\"";
mostCurrent._os = "2.1";
 break; }
case 6: {
 //BA.debugLineNum = 40;BA.debugLine="Case 8 : OS = \"2.2\"";
mostCurrent._os = "2.2";
 break; }
case 7: {
 //BA.debugLineNum = 41;BA.debugLine="Case 9 : OS = \"2.3 - 2.3.2\"";
mostCurrent._os = "2.3 - 2.3.2";
 break; }
case 8: {
 //BA.debugLineNum = 42;BA.debugLine="Case 10 : OS = \"	2.3.3 - 2.3.7\" ' 2.3.3 or 2.3.4";
mostCurrent._os = "	2.3.3 - 2.3.7";
 break; }
case 9: {
 //BA.debugLineNum = 43;BA.debugLine="Case 11 : OS = \"3.0\"";
mostCurrent._os = "3.0";
 break; }
case 10: {
 //BA.debugLineNum = 44;BA.debugLine="Case 12 : OS = \"3.1\"";
mostCurrent._os = "3.1";
 break; }
case 11: {
 //BA.debugLineNum = 45;BA.debugLine="Case 13 : OS = \"3.2\"";
mostCurrent._os = "3.2";
 break; }
case 12: {
 //BA.debugLineNum = 46;BA.debugLine="Case 14 : OS = \"	4.0.1 - 4.0.2\"";
mostCurrent._os = "	4.0.1 - 4.0.2";
 break; }
case 13: {
 //BA.debugLineNum = 47;BA.debugLine="Case 15 : OS = \"4.0.3 - 4.0.4\"";
mostCurrent._os = "4.0.3 - 4.0.4";
 break; }
case 14: {
 //BA.debugLineNum = 48;BA.debugLine="Case 16 : OS = \"	4.1.x\"";
mostCurrent._os = "	4.1.x";
 break; }
case 15: {
 //BA.debugLineNum = 49;BA.debugLine="Case 17 : OS = \"	4.2.x\"";
mostCurrent._os = "	4.2.x";
 break; }
case 16: {
 //BA.debugLineNum = 50;BA.debugLine="Case 18 : OS = 	\"4.3.x\"";
mostCurrent._os = "4.3.x";
 break; }
case 17: {
 //BA.debugLineNum = 51;BA.debugLine="Case 19 : OS = \"	4.4 - 4.4.4\"";
mostCurrent._os = "	4.4 - 4.4.4";
 break; }
case 18: {
 //BA.debugLineNum = 52;BA.debugLine="Case 21: OS = \"5.0\"";
mostCurrent._os = "5.0";
 break; }
case 19: {
 //BA.debugLineNum = 53;BA.debugLine="Case 22: OS = \"5.1\"";
mostCurrent._os = "5.1";
 break; }
case 20: {
 //BA.debugLineNum = 54;BA.debugLine="Case 23: OS = \"6.0\"";
mostCurrent._os = "6.0";
 break; }
case 21: {
 //BA.debugLineNum = 55;BA.debugLine="Case 24 : OS = \"	7.0\"";
mostCurrent._os = "	7.0";
 break; }
case 22: {
 //BA.debugLineNum = 56;BA.debugLine="Case 25 : OS = \"	7.1\"";
mostCurrent._os = "	7.1";
 break; }
default: {
 //BA.debugLineNum = 57;BA.debugLine="Case Else : OS = \"?\"";
mostCurrent._os = "?";
 break; }
}
;
 //BA.debugLineNum = 60;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 61;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 62;BA.debugLine="rooot = \"Rooted\"";
mostCurrent._rooot = "Rooted";
 }else {
 //BA.debugLineNum = 64;BA.debugLine="rooot = \"Unroot\"";
mostCurrent._rooot = "Unroot";
 };
 //BA.debugLineNum = 67;BA.debugLine="lw.Initialize(\"\")";
mostCurrent._lw.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 68;BA.debugLine="lw.Text = \"Warning :  This is beta version!\" & CR";
mostCurrent._lw.setText(BA.ObjectToCharSequence("Warning :  This is beta version!"+anywheresoftware.b4a.keywords.Common.CRLF+" Use at your own risk :)"));
 //BA.debugLineNum = 69;BA.debugLine="lw.TextColor = Colors.Red";
mostCurrent._lw.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 70;BA.debugLine="lw.Gravity = Gravity.CENTER";
mostCurrent._lw.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 71;BA.debugLine="Activity.AddView(lw,1%x,55dip,100%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lw.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 73;BA.debugLine="lb.Initialize(\"lb\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"lb");
 //BA.debugLineNum = 74;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 75;BA.debugLine="lb.Text = \"Brand Name : \" & ph.Manufacturer & CRL";
mostCurrent._lb.setText(BA.ObjectToCharSequence("Brand Name : "+mostCurrent._ph.getManufacturer()+anywheresoftware.b4a.keywords.Common.CRLF+"Device Name : "+mostCurrent._ph.getModel()+anywheresoftware.b4a.keywords.Common.CRLF+"Android Version : "+mostCurrent._os+anywheresoftware.b4a.keywords.Common.CRLF+"Root Status : "+mostCurrent._rooot));
 //BA.debugLineNum = 76;BA.debugLine="Activity.AddView(lb,0%x,(lw.Height+lw.Top)+1%y,10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lw.getHeight()+mostCurrent._lw.getTop())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 77;BA.debugLine="lb.TextColor = Colors.Black";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 78;BA.debugLine="lb.Typeface = mm";
mostCurrent._lb.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 80;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 81;BA.debugLine="ph.SetScreenOrientation(1)";
mostCurrent._ph.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 83;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 84;BA.debugLine="Dim b1bg As ColorDrawable";
_b1bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 85;BA.debugLine="b1bg.Initialize(Colors.Black,10)";
_b1bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Black,(int) (10));
 //BA.debugLineNum = 86;BA.debugLine="b1.Text = \"Install\"";
mostCurrent._b1.setText(BA.ObjectToCharSequence("Install"));
 //BA.debugLineNum = 87;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 88;BA.debugLine="b1.Gravity = Gravity.CENTER";
mostCurrent._b1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 89;BA.debugLine="b1.Textcolor = Colors.White";
mostCurrent._b1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 90;BA.debugLine="b1.TextSize = 17";
mostCurrent._b1.setTextSize((float) (17));
 //BA.debugLineNum = 91;BA.debugLine="Activity.AddView(b1,20%x,(lb.Height+lb.Top)+1%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._lb.getHeight()+mostCurrent._lb.getTop())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 93;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 94;BA.debugLine="b2.Background = b1bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 95;BA.debugLine="b2.Text = \"Restore\"";
mostCurrent._b2.setText(BA.ObjectToCharSequence("Restore"));
 //BA.debugLineNum = 96;BA.debugLine="b2.Gravity = Gravity.CENTER";
mostCurrent._b2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 97;BA.debugLine="b2.Textcolor = Colors.White";
mostCurrent._b2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 98;BA.debugLine="b2.TextSize = 17";
mostCurrent._b2.setTextSize((float) (17));
 //BA.debugLineNum = 99;BA.debugLine="Activity.AddView(b2,20%x,(b1.Top+b1.Height)+2%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 101;BA.debugLine="b3.Initialize(\"b3\")";
mostCurrent._b3.Initialize(mostCurrent.activityBA,"b3");
 //BA.debugLineNum = 102;BA.debugLine="b3.Text = \"Tutorial\"";
mostCurrent._b3.setText(BA.ObjectToCharSequence("Tutorial"));
 //BA.debugLineNum = 103;BA.debugLine="b3.Background = b1bg";
mostCurrent._b3.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 104;BA.debugLine="b3.Gravity = Gravity.CENTER";
mostCurrent._b3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 105;BA.debugLine="b3.Textcolor = Colors.White";
mostCurrent._b3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 106;BA.debugLine="b3.TextSize = 17";
mostCurrent._b3.setTextSize((float) (17));
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddView(b3,20%x,(b2.Top+b2.Height)+2%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b2.getTop()+mostCurrent._b2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 110;BA.debugLine="tlb.Initialize(\"tlb\")";
mostCurrent._tlb.Initialize(mostCurrent.activityBA,"tlb");
 //BA.debugLineNum = 111;BA.debugLine="tlb.Text = \"Other [#Root]\"";
mostCurrent._tlb.setText(BA.ObjectToCharSequence("Other [#Root]"));
 //BA.debugLineNum = 112;BA.debugLine="tlb.Color = Colors.rgb(76, 175, 80)";
mostCurrent._tlb.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (76),(int) (175),(int) (80)));
 //BA.debugLineNum = 113;BA.debugLine="tlb.TextColor = Colors.White";
mostCurrent._tlb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 114;BA.debugLine="tlb.TextSize = 25";
mostCurrent._tlb.setTextSize((float) (25));
 //BA.debugLineNum = 115;BA.debugLine="tlb.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._tlb.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 117;BA.debugLine="tlb.Gravity = Gravity.CENTER";
mostCurrent._tlb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 118;BA.debugLine="Activity.AddView(tlb,0%x,0%y,100%x,55dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tlb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55)));
 //BA.debugLineNum = 120;BA.debugLine="sm.Initialize(Activity, Me, \"SlideMenu\",0,70%x)";
mostCurrent._sm._initialize(mostCurrent.activityBA,mostCurrent._activity,other.getObject(),"SlideMenu",(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 121;BA.debugLine="sm.AddItem(\"Samsung\",LoadBitmap(File.DirAssets,\"s";
mostCurrent._sm._additem("Samsung",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"samsung.png"),(Object)(1));
 //BA.debugLineNum = 122;BA.debugLine="sm.AddItem(\"Oppo\",LoadBitmap(File.DirAssets,\"oppo";
mostCurrent._sm._additem("Oppo",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"oppo.png"),(Object)(2));
 //BA.debugLineNum = 123;BA.debugLine="sm.AddItem(\"Vivo\",LoadBitmap(File.DirAssets,\"vivo";
mostCurrent._sm._additem("Vivo",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"vivo.png"),(Object)(3));
 //BA.debugLineNum = 124;BA.debugLine="sm.AddItem(\"Huawei\",LoadBitmap(File.DirAssets,\"hu";
mostCurrent._sm._additem("Huawei",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"huawei.jpg"),(Object)(4));
 //BA.debugLineNum = 125;BA.debugLine="sm.AddItem(\"Xiaomi\",LoadBitmap(File.DirAssets,\"xi";
mostCurrent._sm._additem("Xiaomi",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"xiaomi.png"),(Object)(5));
 //BA.debugLineNum = 126;BA.debugLine="sm.AddItem(\"Other [#Root]\",LoadBitmap(File.DirAss";
mostCurrent._sm._additem("Other [#Root]",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"other.png"),(Object)(6));
 //BA.debugLineNum = 127;BA.debugLine="sm.AddItem(\"Share App\",LoadBitmap(File.DirAssets,";
mostCurrent._sm._additem("Share App",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png"),(Object)(7));
 //BA.debugLineNum = 128;BA.debugLine="sm.AddItem(\"More App\",LoadBitmap(File.DirAssets,\"";
mostCurrent._sm._additem("More App",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"moreapp.png"),(Object)(8));
 //BA.debugLineNum = 129;BA.debugLine="sm.AddItem(\"About\",LoadBitmap(File.DirAssets,\"abo";
mostCurrent._sm._additem("About",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"about.png"),(Object)(9));
 //BA.debugLineNum = 131;BA.debugLine="mbg.Initialize(LoadBitmap(File.DirAssets,\"menu.pn";
mostCurrent._mbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu.png").getObject()));
 //BA.debugLineNum = 132;BA.debugLine="menu.Initialize(\"menu\")";
mostCurrent._menu.Initialize(mostCurrent.activityBA,"menu");
 //BA.debugLineNum = 133;BA.debugLine="menu.Background = mbg";
mostCurrent._menu.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mbg.getObject()));
 //BA.debugLineNum = 134;BA.debugLine="menu.Gravity = Gravity.CENTER";
mostCurrent._menu.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 135;BA.debugLine="Activity.AddView(menu,10dip,12.5dip,30dip,30dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._menu.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 137;BA.debugLine="sbg.Initialize(LoadBitmap(File.DirAssets,\"share.p";
mostCurrent._sbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()));
 //BA.debugLineNum = 138;BA.debugLine="share.Initialize(\"share\")";
mostCurrent._share.Initialize(mostCurrent.activityBA,"share");
 //BA.debugLineNum = 139;BA.debugLine="share.Background = sbg";
mostCurrent._share.setBackground((android.graphics.drawable.Drawable)(mostCurrent._sbg.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="share.Gravity = Gravity.CENTER";
mostCurrent._share.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 141;BA.debugLine="Activity.AddView(share,100%x - 40dip,12.5dip,30di";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._share.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 143;BA.debugLine="Banner.Initialize2(\"Banner\",\"ca-app-pub-417334857";
mostCurrent._banner.Initialize2(mostCurrent.activityBA,"Banner","ca-app-pub-4173348573252986/3885084957",mostCurrent._banner.SIZE_SMART_BANNER);
 //BA.debugLineNum = 144;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 145;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 147;BA.debugLine="If 100%x > 100%y Then height = 32dip Else height";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 150;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 152;BA.debugLine="Activity.AddView(Banner, 0dip, 100%y - height, 10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 153;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 154;BA.debugLine="Log(Banner)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._banner));
 //BA.debugLineNum = 156;BA.debugLine="Interstitial.Initialize(\"Interstitial\",\"ca-app-pu";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/5361818159");
 //BA.debugLineNum = 157;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 159;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 160;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="ad2.Initialize(\"ad2\",60000)";
_ad2.Initialize(processBA,"ad2",(long) (60000));
 //BA.debugLineNum = 162;BA.debugLine="ad2.Enabled = True";
_ad2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 164;BA.debugLine="ti.Initialize(\"ti\",100)";
_ti.Initialize(processBA,"ti",(long) (100));
 //BA.debugLineNum = 165;BA.debugLine="ti.Enabled = False";
_ti.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 166;BA.debugLine="tr.Initialize(\"tr\",100)";
_tr.Initialize(processBA,"tr",(long) (100));
 //BA.debugLineNum = 167;BA.debugLine="tr.Enabled = False";
_tr.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 169;BA.debugLine="ist.Initialize(\"ist\",5000)";
_ist.Initialize(processBA,"ist",(long) (5000));
 //BA.debugLineNum = 170;BA.debugLine="ist.Enabled = False";
_ist.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="rst.Initialize(\"rst\",5000)";
_rst.Initialize(processBA,"rst",(long) (5000));
 //BA.debugLineNum = 172;BA.debugLine="rst.Enabled = False";
_rst.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
 //BA.debugLineNum = 601;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 602;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 603;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 604;BA.debugLine="in = pm.GetApplicationIntent(\"com.xinmei365.fonu\"";
_in = _pm.GetApplicationIntent("com.xinmei365.fonu");
 //BA.debugLineNum = 605;BA.debugLine="If in.IsInitialized Then";
if (_in.IsInitialized()) { 
 //BA.debugLineNum = 606;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 607;BA.debugLine="ml.rmrf(File.DirRootExternal & \"/.MyanmarHeartFo";
mostCurrent._ml.rmrf(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.MyanmarHeartFont");
 };
 //BA.debugLineNum = 609;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _in = null;
anywheresoftware.b4a.phone.PackageManagerWrapper _pm = null;
 //BA.debugLineNum = 564;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 565;BA.debugLine="Dim in As Intent";
_in = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 566;BA.debugLine="Dim pm As PackageManager";
_pm = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 567;BA.debugLine="in = pm.GetApplicationIntent(\"com.xinmei365.fonu\"";
_in = _pm.GetApplicationIntent("com.xinmei365.fonu");
 //BA.debugLineNum = 568;BA.debugLine="If in.IsInitialized Then";
if (_in.IsInitialized()) { 
 //BA.debugLineNum = 569;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 570;BA.debugLine="ml.rmrf(File.DirRootExternal & \"/.MyanmarHeartFo";
mostCurrent._ml.rmrf(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.MyanmarHeartFont");
 };
 //BA.debugLineNum = 572;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 574;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 575;BA.debugLine="If Interstitial.Ready Then Interstitial.Show";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();};
 //BA.debugLineNum = 576;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 577;BA.debugLine="End Sub";
return "";
}
public static String  _ad2_tick() throws Exception{
 //BA.debugLineNum = 597;BA.debugLine="Sub ad2_Tick";
 //BA.debugLineNum = 598;BA.debugLine="If Interstitial.Ready Then Interstitial.Show";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();};
 //BA.debugLineNum = 599;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 176;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 177;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 178;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 179;BA.debugLine="File.Copy(File.DirAssets,\"Nayon.ttf\",File.DirRoo";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Nayon.ttf",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"MyanmarHeart.ttf");
 //BA.debugLineNum = 180;BA.debugLine="ProgressDialogShow(\"Installing...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Installing..."));
 //BA.debugLineNum = 181;BA.debugLine="ti.Enabled = True";
_ti.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 183;BA.debugLine="Msgbox(\"Your device not have Root Access!\",\"Atte";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Your device not have Root Access!"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 185;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
 //BA.debugLineNum = 417;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 418;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 419;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 420;BA.debugLine="ProgressDialogShow(\"Please Wait...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Please Wait..."));
 //BA.debugLineNum = 421;BA.debugLine="tr.Enabled = True";
_tr.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 423;BA.debugLine="Msgbox(\"Your device not have Root Access!\",\"Atte";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Your device not have Root Access!"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 425;BA.debugLine="End Sub";
return "";
}
public static String  _b3_click() throws Exception{
 //BA.debugLineNum = 560;BA.debugLine="Sub b3_Click";
 //BA.debugLineNum = 561;BA.debugLine="StartActivity(Tutorial)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._tutorial.getObject()));
 //BA.debugLineNum = 562;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 14;BA.debugLine="Dim OS As String";
mostCurrent._os = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim b1,b2,b3 As Label";
mostCurrent._b1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._b3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim Interstitial As InterstitialAd";
mostCurrent._interstitial = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 20;BA.debugLine="Dim sm As SlideMenu";
mostCurrent._sm = new com.htetznaing.mmnayonfont.slidemenu();
 //BA.debugLineNum = 21;BA.debugLine="Dim tlb As Label";
mostCurrent._tlb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim menu,share As Button";
mostCurrent._menu = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._share = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim sbg,mbg As BitmapDrawable";
mostCurrent._sbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._mbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 24;BA.debugLine="Dim copy As BClipboard";
mostCurrent._copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 25;BA.debugLine="Dim lb,lw As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lw = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim mm As Typeface : mm = mm.LoadFromAssets(\"Nayo";
mostCurrent._mm = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim mm As Typeface : mm = mm.LoadFromAssets(\"Nayo";
mostCurrent._mm.setObject((android.graphics.Typeface)(mostCurrent._mm.LoadFromAssets("Nayon.ttf")));
 //BA.debugLineNum = 27;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 28;BA.debugLine="Dim rooot As String";
mostCurrent._rooot = "";
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_adclosed() throws Exception{
 //BA.debugLineNum = 580;BA.debugLine="Sub Interstitial_AdClosed";
 //BA.debugLineNum = 581;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 582;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_adopened() throws Exception{
 //BA.debugLineNum = 593;BA.debugLine="Sub Interstitial_adopened";
 //BA.debugLineNum = 594;BA.debugLine="Log(\"Opened\")";
anywheresoftware.b4a.keywords.Common.Log("Opened");
 //BA.debugLineNum = 595;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 588;BA.debugLine="Sub Interstitial_FailedToReceiveAd (ErrorCode As S";
 //BA.debugLineNum = 589;BA.debugLine="Log(\"not Received - \" &\"Error Code: \"&ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("not Received - "+"Error Code: "+_errorcode);
 //BA.debugLineNum = 590;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 591;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_receivead() throws Exception{
 //BA.debugLineNum = 584;BA.debugLine="Sub Interstitial_ReceiveAd";
 //BA.debugLineNum = 585;BA.debugLine="Log(\"Received\")";
anywheresoftware.b4a.keywords.Common.Log("Received");
 //BA.debugLineNum = 586;BA.debugLine="End Sub";
return "";
}
public static String  _ist_tick() throws Exception{
 //BA.debugLineNum = 407;BA.debugLine="Sub ist_Tick";
 //BA.debugLineNum = 408;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 409;BA.debugLine="Msgbox(\"Congratulations! Myanmar Installed in you";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Congratulations! Myanmar Installed in your device"+anywheresoftware.b4a.keywords.Common.CRLF+"Now, your device will be reboot!"),BA.ObjectToCharSequence("Completed"),mostCurrent.activityBA);
 //BA.debugLineNum = 410;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 411;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 412;BA.debugLine="ml.RootCmd(\"reboot\",\"\",Null,Null,False)";
mostCurrent._ml.RootCmd("reboot","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 414;BA.debugLine="ist.Enabled = False";
_ist.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 415;BA.debugLine="End Sub";
return "";
}
public static String  _menu_click() throws Exception{
 //BA.debugLineNum = 645;BA.debugLine="Sub menu_Click";
 //BA.debugLineNum = 646;BA.debugLine="If sm.isVisible Then sm.Hide Else sm.Show";
if (mostCurrent._sm._isvisible()) { 
mostCurrent._sm._hide();}
else {
mostCurrent._sm._show();};
 //BA.debugLineNum = 647;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim ad1,ad2 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
_ad2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 8;BA.debugLine="Dim ti,tr As Timer";
_ti = new anywheresoftware.b4a.objects.Timer();
_tr = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 9;BA.debugLine="Dim ist,rst As Timer";
_ist = new anywheresoftware.b4a.objects.Timer();
_rst = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _rst_tick() throws Exception{
 //BA.debugLineNum = 548;BA.debugLine="Sub rst_Tick";
 //BA.debugLineNum = 549;BA.debugLine="ml.GetRoot";
mostCurrent._ml.GetRoot();
 //BA.debugLineNum = 550;BA.debugLine="If ml.HaveRoot Then";
if (mostCurrent._ml.HaveRoot) { 
 //BA.debugLineNum = 551;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 552;BA.debugLine="Msgbox(\"Congratulations! Original Restored\" & CR";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Congratulations! Original Restored"+anywheresoftware.b4a.keywords.Common.CRLF+"Now, your device will be reboot!"),BA.ObjectToCharSequence("Completed"),mostCurrent.activityBA);
 //BA.debugLineNum = 553;BA.debugLine="ml.RootCmd(\"reboot\",\"\",Null,Null,False)";
mostCurrent._ml.RootCmd("reboot","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 555;BA.debugLine="Msgbox(\"Your device not have Root Access!\",\"Atte";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Your device not have Root Access!"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 557;BA.debugLine="rst.Enabled = False";
_rst.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 558;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
 //BA.debugLineNum = 649;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 650;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 651;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 652;BA.debugLine="copy.setText(\"#Myanmar_Nayon_Font App! Beautiful";
mostCurrent._copy.setText(mostCurrent.activityBA,"#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://www.htetznaing.com/search?q=Myanmar+Nayon+Font");
 //BA.debugLineNum = 653;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 654;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 655;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 656;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 657;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 658;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return "";
}
public static String  _slidemenu_click(Object _item) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 612;BA.debugLine="Sub SlideMenu_Click(Item As Object)";
 //BA.debugLineNum = 613;BA.debugLine="sm.Hide";
mostCurrent._sm._hide();
 //BA.debugLineNum = 614;BA.debugLine="Select Item";
switch (BA.switchObjectToInt(_item,(Object)(1),(Object)(2),(Object)(3),(Object)(4),(Object)(5),(Object)(6),(Object)(7),(Object)(8),(Object)(9))) {
case 0: {
 //BA.debugLineNum = 616;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,other.getObject());
 break; }
case 1: {
 //BA.debugLineNum = 618;BA.debugLine="StartActivity(Oppo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._oppo.getObject()));
 break; }
case 2: {
 //BA.debugLineNum = 620;BA.debugLine="StartActivity(Vivo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._vivo.getObject()));
 break; }
case 3: {
 //BA.debugLineNum = 622;BA.debugLine="StartActivity(Huawei)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._huawei.getObject()));
 break; }
case 4: {
 //BA.debugLineNum = 624;BA.debugLine="StartActivity(Xiaomi)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._xiaomi.getObject()));
 break; }
case 5: {
 //BA.debugLineNum = 626;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,other.getObject());
 break; }
case 6: {
 //BA.debugLineNum = 628;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 629;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 630;BA.debugLine="copy.setText(\"#Myanmar_Nayon_Font App! Beautifu";
mostCurrent._copy.setText(mostCurrent.activityBA,"#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://bit.ly/2mqSEWy");
 //BA.debugLineNum = 631;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 632;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 633;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",c";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 634;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 635;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 636;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 break; }
case 7: {
 //BA.debugLineNum = 638;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 639;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetzna";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("http://www.htetznaing.com")));
 break; }
case 8: {
 //BA.debugLineNum = 641;BA.debugLine="StartActivity(About)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._about.getObject()));
 break; }
}
;
 //BA.debugLineNum = 643;BA.debugLine="End Sub";
return "";
}
public static String  _ti_tick() throws Exception{
 //BA.debugLineNum = 187;BA.debugLine="Sub ti_Tick";
 //BA.debugLineNum = 188;BA.debugLine="ml.RootCmd(\"mount -o rw,remount /system\",\"\",Null,";
mostCurrent._ml.RootCmd("mount -o rw,remount /system","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 189;BA.debugLine="If ml.Exists(\"/system/Ht3tzN4ing.ttf\") = False Th";
if (mostCurrent._ml.Exists("/system/Ht3tzN4ing.ttf")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 190;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /syst";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/Ht3tzN4ing.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 192;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk.ttf\") = True";
if (mostCurrent._ml.Exists("/system/fonts/Padauk.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 193;BA.debugLine="ml.mv(\"/system/fonts/Padauk.ttf\",\"/system/fonts";
mostCurrent._ml.mv("/system/fonts/Padauk.ttf","/system/fonts/Padauk.ttf.bak");
 //BA.debugLineNum = 194;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 195;BA.debugLine="ml.chmod(\"/system/fonts/Padauk.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Padauk.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 199;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk-book.ttf\") =";
if (mostCurrent._ml.Exists("/system/fonts/Padauk-book.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 200;BA.debugLine="ml.mv(\"/system/fonts/Padauk-book.ttf\",\"/system/";
mostCurrent._ml.mv("/system/fonts/Padauk-book.ttf","/system/fonts/Padauk-book.ttf.bak");
 //BA.debugLineNum = 201;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-book.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 202;BA.debugLine="ml.chmod(\"/system/fonts/Padauk-book.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Padauk-book.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 206;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk-bookbold.ttf\"";
if (mostCurrent._ml.Exists("/system/fonts/Padauk-bookbold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 207;BA.debugLine="ml.mv(\"/system/fonts/Padauk-bookbold.ttf\",\"/sys";
mostCurrent._ml.mv("/system/fonts/Padauk-bookbold.ttf","/system/fonts/Padauk-bookbold.ttf.bak");
 //BA.debugLineNum = 208;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-bookbold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 209;BA.debugLine="ml.chmod(\"/system/fonts/Padauk-bookbold.ttf\",64";
mostCurrent._ml.chmod("/system/fonts/Padauk-bookbold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 213;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmar-Bold";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmar-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 214;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmar-Bold.ttf\",";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmar-Bold.ttf","/system/fonts/NotoSansMyanmar-Bold.ttf.bak");
 //BA.debugLineNum = 215;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 216;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmar-Bold.tt";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmar-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 220;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmar-Regu";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmar-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 221;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmar-Regular.tt";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmar-Regular.ttf","/system/fonts/NotoSansMyanmar-Regular.ttf.bak");
 //BA.debugLineNum = 222;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 223;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmar-Regular";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmar-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 227;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarUI-Bo";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarUI-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 228;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarUI-Bold.ttf";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarUI-Bold.ttf","/system/fonts/NotoSansMyanmarUI-Bold.ttf.bak");
 //BA.debugLineNum = 229;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 230;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarUI-Bold.";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarUI-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 234;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarUI-Re";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarUI-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 235;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarUI-Regular.";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarUI-Regular.ttf","/system/fonts/NotoSansMyanmarUI-Regular.ttf.bak");
 //BA.debugLineNum = 236;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 237;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarUI-Regul";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarUI-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 242;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarZawgy";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 243;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarZawgyi-Bold";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf","/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf.bak");
 //BA.debugLineNum = 244;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 245;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarZawgyi-B";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 249;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarZawgy";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 250;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarZawgyi-Regu";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf","/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf.bak");
 //BA.debugLineNum = 251;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 252;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarZawgyi-R";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 256;BA.debugLine="If ml.Exists(\"/system/fonts/SamsungMyanmar.ttf\")";
if (mostCurrent._ml.Exists("/system/fonts/SamsungMyanmar.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 257;BA.debugLine="ml.mv(\"/system/fonts/SamsungMyanmar.ttf\",\"/syst";
mostCurrent._ml.mv("/system/fonts/SamsungMyanmar.ttf","/system/fonts/SamsungMyanmar.ttf.bak");
 //BA.debugLineNum = 258;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/SamsungMyanmar.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 259;BA.debugLine="ml.chmod(\"/system/fonts/SamsungMyanmar.ttf\",644";
mostCurrent._ml.chmod("/system/fonts/SamsungMyanmar.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 263;BA.debugLine="If ml.Exists(\"/system/fonts/ZawgyiOne.ttf\") = Tr";
if (mostCurrent._ml.Exists("/system/fonts/ZawgyiOne.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 264;BA.debugLine="ml.mv(\"/system/fonts/ZawgyiOne.ttf\",\"/system/fo";
mostCurrent._ml.mv("/system/fonts/ZawgyiOne.ttf","/system/fonts/ZawgyiOne.ttf.bak");
 //BA.debugLineNum = 265;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 266;BA.debugLine="ml.chmod(\"/system/fonts/ZawgyiOne.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/ZawgyiOne.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 270;BA.debugLine="If ml.Exists(\"/system/fonts/ZawgyiOne2008.ttf\")";
if (mostCurrent._ml.Exists("/system/fonts/ZawgyiOne2008.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 271;BA.debugLine="ml.mv(\"/system/fonts/ZawgyiOne2008.ttf\",\"/syste";
mostCurrent._ml.mv("/system/fonts/ZawgyiOne2008.ttf","/system/fonts/ZawgyiOne2008.ttf.bak");
 //BA.debugLineNum = 272;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne2008.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 273;BA.debugLine="ml.chmod(\"/system/fonts/ZawgyiOne2008.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/ZawgyiOne2008.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 277;BA.debugLine="If ml.Exists(\"/system/fonts/mmsd.ttf\") = True Th";
if (mostCurrent._ml.Exists("/system/fonts/mmsd.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 278;BA.debugLine="ml.mv(\"/system/fonts/mmsd.ttf\",\"/system/fonts/m";
mostCurrent._ml.mv("/system/fonts/mmsd.ttf","/system/fonts/mmsd.ttf.bak");
 //BA.debugLineNum = 279;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/mmsd.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 280;BA.debugLine="ml.chmod(\"/system/fonts/mmsd.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/mmsd.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 284;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Regular.ttf\")";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 285;BA.debugLine="ml.mv(\"/system/fonts/Roboto-Regular.ttf\",\"/syst";
mostCurrent._ml.mv("/system/fonts/Roboto-Regular.ttf","/system/fonts/Roboto-Regular.ttf.bak");
 //BA.debugLineNum = 286;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 287;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Regular.ttf\",644";
mostCurrent._ml.chmod("/system/fonts/Roboto-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 290;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Light.ttf\") =";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Light.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 291;BA.debugLine="ml.mv(\"/system/fonts/Roboto-Light.ttf\",\"/system";
mostCurrent._ml.mv("/system/fonts/Roboto-Light.ttf","/system/fonts/Roboto-Light.ttf.bak");
 //BA.debugLineNum = 292;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Light.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 293;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Light.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Light.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 296;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Bold.ttf\") =";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 297;BA.debugLine="ml.mv(\"/system/fonts/Roboto-Bold.ttf\",\"/system/";
mostCurrent._ml.mv("/system/fonts/Roboto-Bold.ttf","/system/fonts/Roboto-Bold.ttf.bak");
 //BA.debugLineNum = 298;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 299;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Bold.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 302;BA.debugLine="ist.Enabled = True";
_ist.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 303;BA.debugLine="ti.Enabled = False";
_ti.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 308;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk.ttf\") = True";
if (mostCurrent._ml.Exists("/system/fonts/Padauk.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 309;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 310;BA.debugLine="ml.chmod(\"/system/fonts/Padauk.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Padauk.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 314;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk-book.ttf\") =";
if (mostCurrent._ml.Exists("/system/fonts/Padauk-book.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 315;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-book.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 316;BA.debugLine="ml.chmod(\"/system/fonts/Padauk-book.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Padauk-book.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 320;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk-bookbold.ttf\"";
if (mostCurrent._ml.Exists("/system/fonts/Padauk-bookbold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 321;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-bookbold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 322;BA.debugLine="ml.chmod(\"/system/fonts/Padauk-bookbold.ttf\",64";
mostCurrent._ml.chmod("/system/fonts/Padauk-bookbold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 326;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmar-Bold";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmar-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 327;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 328;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmar-Bold.tt";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmar-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 332;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmar-Regu";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmar-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 333;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 334;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmar-Regular";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmar-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 338;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarUI-Bo";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarUI-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 339;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 340;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarUI-Bold.";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarUI-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 344;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarUI-Re";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarUI-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 345;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 346;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarUI-Regul";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarUI-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 351;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarZawgy";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 352;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 353;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarZawgyi-B";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 357;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarZawgy";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 358;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 359;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarZawgyi-R";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 363;BA.debugLine="If ml.Exists(\"/system/fonts/SamsungMyanmar.ttf\")";
if (mostCurrent._ml.Exists("/system/fonts/SamsungMyanmar.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 364;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/SamsungMyanmar.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 365;BA.debugLine="ml.chmod(\"/system/fonts/SamsungMyanmar.ttf\",644";
mostCurrent._ml.chmod("/system/fonts/SamsungMyanmar.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 369;BA.debugLine="If ml.Exists(\"/system/fonts/ZawgyiOne.ttf\") = Tr";
if (mostCurrent._ml.Exists("/system/fonts/ZawgyiOne.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 370;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 371;BA.debugLine="ml.chmod(\"/system/fonts/ZawgyiOne.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/ZawgyiOne.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 375;BA.debugLine="If ml.Exists(\"/system/fonts/ZawgyiOne2008.ttf\")";
if (mostCurrent._ml.Exists("/system/fonts/ZawgyiOne2008.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 376;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne2008.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 377;BA.debugLine="ml.chmod(\"/system/fonts/ZawgyiOne2008.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/ZawgyiOne2008.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 381;BA.debugLine="If ml.Exists(\"/system/fonts/mmsd.ttf\") = True Th";
if (mostCurrent._ml.Exists("/system/fonts/mmsd.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 382;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/mmsd.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 383;BA.debugLine="ml.chmod(\"/system/fonts/mmsd.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/mmsd.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 387;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Regular.ttf\")";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Regular.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 388;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Regular.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 389;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Regular.ttf\",644";
mostCurrent._ml.chmod("/system/fonts/Roboto-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 392;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Light.ttf\") =";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Light.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 393;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Light.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 394;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Light.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Light.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 397;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Bold.ttf\") =";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Bold.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 398;BA.debugLine="ml.RootCmd(\"cp -r /sdcard/MyanmarHeart.ttf /sys";
mostCurrent._ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Bold.ttf","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 399;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Bold.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 402;BA.debugLine="ist.Enabled = True";
_ist.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 403;BA.debugLine="ti.Enabled = False";
_ti.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 405;BA.debugLine="End Sub";
return "";
}
public static String  _tr_tick() throws Exception{
 //BA.debugLineNum = 427;BA.debugLine="Sub tr_Tick";
 //BA.debugLineNum = 428;BA.debugLine="ml.RootCmd(\"mount -o rw,remount /system\",\"\",Null,";
mostCurrent._ml.RootCmd("mount -o rw,remount /system","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 431;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk.ttf.bak\") = Tr";
if (mostCurrent._ml.Exists("/system/fonts/Padauk.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 432;BA.debugLine="ml.rm(\"/system/fonts/Padauk.ttf\")";
mostCurrent._ml.rm("/system/fonts/Padauk.ttf");
 //BA.debugLineNum = 433;BA.debugLine="ml.mv(\"/system/fonts/Padauk.ttf.bak\",\"/system/fo";
mostCurrent._ml.mv("/system/fonts/Padauk.ttf.bak","/system/fonts/Padauk.ttf");
 //BA.debugLineNum = 434;BA.debugLine="ml.chmod(\"/system/fonts/Padauk.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Padauk.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 438;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk-book.ttf.bak\")";
if (mostCurrent._ml.Exists("/system/fonts/Padauk-book.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 439;BA.debugLine="ml.rm(\"/system/fonts/Padauk-book.ttf\")";
mostCurrent._ml.rm("/system/fonts/Padauk-book.ttf");
 //BA.debugLineNum = 440;BA.debugLine="ml.mv(\"/system/fonts/Padauk-book.ttf.bak\",\"/syst";
mostCurrent._ml.mv("/system/fonts/Padauk-book.ttf.bak","/system/fonts/Padauk-book.ttf");
 //BA.debugLineNum = 441;BA.debugLine="ml.chmod(\"/system/fonts/Padauk-book.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Padauk-book.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 445;BA.debugLine="If ml.Exists(\"/system/fonts/Padauk-bookbold.ttf.b";
if (mostCurrent._ml.Exists("/system/fonts/Padauk-bookbold.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 446;BA.debugLine="ml.rm(\"/system/fonts/Padauk-bookbold.ttf\")";
mostCurrent._ml.rm("/system/fonts/Padauk-bookbold.ttf");
 //BA.debugLineNum = 447;BA.debugLine="ml.mv(\"/system/fonts/Padauk-bookbold.ttf.bak\",\"/";
mostCurrent._ml.mv("/system/fonts/Padauk-bookbold.ttf.bak","/system/fonts/Padauk-bookbold.ttf");
 //BA.debugLineNum = 448;BA.debugLine="ml.chmod(\"/system/fonts/Padauk-bookbold.ttf\",644";
mostCurrent._ml.chmod("/system/fonts/Padauk-bookbold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 452;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmar-Bold.";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmar-Bold.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 453;BA.debugLine="ml.rm(\"/system/fonts/NotoSansMyanmar-Bold.ttf\")";
mostCurrent._ml.rm("/system/fonts/NotoSansMyanmar-Bold.ttf");
 //BA.debugLineNum = 454;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmar-Bold.ttf.ba";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmar-Bold.ttf.bak","/system/fonts/NotoSansMyanmar-Bold.ttf");
 //BA.debugLineNum = 455;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmar-Bold.ttf";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmar-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 459;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmar-Regul";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmar-Regular.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 460;BA.debugLine="ml.rm(\"/system/fonts/NotoSansMyanmar-Regular.ttf";
mostCurrent._ml.rm("/system/fonts/NotoSansMyanmar-Regular.ttf");
 //BA.debugLineNum = 461;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmar-Regular.ttf";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmar-Regular.ttf.bak","/system/fonts/NotoSansMyanmar-Regular.ttf");
 //BA.debugLineNum = 462;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmar-Regular.";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmar-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 466;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarUI-Bol";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarUI-Bold.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 467;BA.debugLine="ml.rm(\"/system/fonts/NotoSansMyanmarUI-Bold.ttf\"";
mostCurrent._ml.rm("/system/fonts/NotoSansMyanmarUI-Bold.ttf");
 //BA.debugLineNum = 468;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarUI-Bold.ttf.";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarUI-Bold.ttf.bak","/system/fonts/NotoSansMyanmarUI-Bold.ttf");
 //BA.debugLineNum = 469;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarUI-Bold.t";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarUI-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 473;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarUI-Reg";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarUI-Regular.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 474;BA.debugLine="ml.rm(\"/system/fonts/NotoSansMyanmarUI-Regular.t";
mostCurrent._ml.rm("/system/fonts/NotoSansMyanmarUI-Regular.ttf");
 //BA.debugLineNum = 475;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarUI-Regular.t";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarUI-Regular.ttf.bak","/system/fonts/NotoSansMyanmarUI-Regular.ttf");
 //BA.debugLineNum = 476;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarUI-Regula";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarUI-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 480;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarZawgyi";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 481;BA.debugLine="ml.rm(\"/system/fonts/NotoSansMyanmarZawgyi-Bold.";
mostCurrent._ml.rm("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf");
 //BA.debugLineNum = 482;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarZawgyi-Bold.";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf.bak","/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf");
 //BA.debugLineNum = 483;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarZawgyi-Bo";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 487;BA.debugLine="If ml.Exists(\"/system/fonts/NotoSansMyanmarZawgyi";
if (mostCurrent._ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 488;BA.debugLine="ml.rm(\"/system/fonts/NotoSansMyanmarZawgyi-Regul";
mostCurrent._ml.rm("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf");
 //BA.debugLineNum = 489;BA.debugLine="ml.mv(\"/system/fonts/NotoSansMyanmarZawgyi-Regul";
mostCurrent._ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf.bak","/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf");
 //BA.debugLineNum = 490;BA.debugLine="ml.chmod(\"/system/fonts/NotoSansMyanmarZawgyi-Re";
mostCurrent._ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 494;BA.debugLine="If ml.Exists(\"/system/fonts/SamsungMyanmar.ttf.ba";
if (mostCurrent._ml.Exists("/system/fonts/SamsungMyanmar.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 495;BA.debugLine="ml.rm(\"/system/fonts/SamsungMyanmar.ttf\")";
mostCurrent._ml.rm("/system/fonts/SamsungMyanmar.ttf");
 //BA.debugLineNum = 496;BA.debugLine="ml.mv(\"/system/fonts/SamsungMyanmar.ttf.bak\",\"/s";
mostCurrent._ml.mv("/system/fonts/SamsungMyanmar.ttf.bak","/system/fonts/SamsungMyanmar.ttf");
 //BA.debugLineNum = 497;BA.debugLine="ml.chmod(\"/system/fonts/SamsungMyanmar.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/SamsungMyanmar.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 501;BA.debugLine="If ml.Exists(\"/system/fonts/ZawgyiOne.ttf.bak\") =";
if (mostCurrent._ml.Exists("/system/fonts/ZawgyiOne.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 502;BA.debugLine="ml.rm(\"/system/fonts/ZawgyiOne.ttf\")";
mostCurrent._ml.rm("/system/fonts/ZawgyiOne.ttf");
 //BA.debugLineNum = 503;BA.debugLine="ml.mv(\"/system/fonts/ZawgyiOne.ttf.bak\",\"/system";
mostCurrent._ml.mv("/system/fonts/ZawgyiOne.ttf.bak","/system/fonts/ZawgyiOne.ttf");
 //BA.debugLineNum = 504;BA.debugLine="ml.chmod(\"/system/fonts/ZawgyiOne.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/ZawgyiOne.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 508;BA.debugLine="If ml.Exists(\"/system/fonts/ZawgyiOne2008.ttf.bak";
if (mostCurrent._ml.Exists("/system/fonts/ZawgyiOne2008.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 509;BA.debugLine="ml.rm(\"/system/fonts/ZawgyiOne2008.ttf\")";
mostCurrent._ml.rm("/system/fonts/ZawgyiOne2008.ttf");
 //BA.debugLineNum = 510;BA.debugLine="ml.mv(\"/system/fonts/ZawgyiOne2008.ttf.bak\",\"/sy";
mostCurrent._ml.mv("/system/fonts/ZawgyiOne2008.ttf.bak","/system/fonts/ZawgyiOne2008.ttf");
 //BA.debugLineNum = 511;BA.debugLine="ml.chmod(\"/system/fonts/ZawgyiOne2008.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/ZawgyiOne2008.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 515;BA.debugLine="If ml.Exists(\"/system/fonts/mmsd.ttf.bak\") = True";
if (mostCurrent._ml.Exists("/system/fonts/mmsd.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 516;BA.debugLine="ml.rm(\"/system/fonts/mmsd.ttf\")";
mostCurrent._ml.rm("/system/fonts/mmsd.ttf");
 //BA.debugLineNum = 517;BA.debugLine="ml.mv(\"/system/fonts/mmsd.ttf.bak\",\"/system/font";
mostCurrent._ml.mv("/system/fonts/mmsd.ttf.bak","/system/fonts/mmsd.ttf");
 //BA.debugLineNum = 518;BA.debugLine="ml.chmod(\"/system/fonts/mmsd.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/mmsd.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 522;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Bold.ttf.bak\")";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Bold.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 523;BA.debugLine="ml.rm(\"/system/fonts/Roboto-Bold.ttf\")";
mostCurrent._ml.rm("/system/fonts/Roboto-Bold.ttf");
 //BA.debugLineNum = 524;BA.debugLine="ml.mv(\"/system/fonts/Roboto-Bold.ttf.bak\",\"/syst";
mostCurrent._ml.mv("/system/fonts/Roboto-Bold.ttf.bak","/system/fonts/Roboto-Bold.ttf");
 //BA.debugLineNum = 525;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Bold.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Bold.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 528;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Light.ttf.bak\"";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Light.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 529;BA.debugLine="ml.rm(\"/system/fonts/Roboto-Light.ttf\")";
mostCurrent._ml.rm("/system/fonts/Roboto-Light.ttf");
 //BA.debugLineNum = 530;BA.debugLine="ml.mv(\"/system/fonts/Roboto-Light.ttf.bak\",\"/sys";
mostCurrent._ml.mv("/system/fonts/Roboto-Light.ttf.bak","/system/fonts/Roboto-Light.ttf");
 //BA.debugLineNum = 531;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Light.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Light.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 534;BA.debugLine="If ml.Exists(\"/system/fonts/Roboto-Regular.ttf.ba";
if (mostCurrent._ml.Exists("/system/fonts/Roboto-Regular.ttf.bak")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 535;BA.debugLine="ml.rm(\"/system/fonts/Roboto-Regular.ttf\")";
mostCurrent._ml.rm("/system/fonts/Roboto-Regular.ttf");
 //BA.debugLineNum = 536;BA.debugLine="ml.mv(\"/system/fonts/Roboto-Regular.ttf.bak\",\"/s";
mostCurrent._ml.mv("/system/fonts/Roboto-Regular.ttf.bak","/system/fonts/Roboto-Regular.ttf");
 //BA.debugLineNum = 537;BA.debugLine="ml.chmod(\"/system/fonts/Roboto-Regular.ttf\",644)";
mostCurrent._ml.chmod("/system/fonts/Roboto-Regular.ttf",BA.NumberToString(644));
 };
 //BA.debugLineNum = 540;BA.debugLine="If ml.Exists(\"/system/Ht3tzN4ing.ttf\") = True The";
if (mostCurrent._ml.Exists("/system/Ht3tzN4ing.ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 541;BA.debugLine="ml.rm(\"/system/Ht3tzN4ing.ttf\")";
mostCurrent._ml.rm("/system/Ht3tzN4ing.ttf");
 };
 //BA.debugLineNum = 544;BA.debugLine="rst.Enabled = True";
_rst.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 545;BA.debugLine="tr.Enabled = False";
_tr.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 546;BA.debugLine="End Sub";
return "";
}
}
