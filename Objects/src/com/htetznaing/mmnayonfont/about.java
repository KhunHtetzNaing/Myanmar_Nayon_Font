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

public class about extends Activity implements B4AActivity{
	public static about mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mmnayonfont", "com.htetznaing.mmnayonfont.about");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (about).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mmnayonfont", "com.htetznaing.mmnayonfont.about");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mmnayonfont.about", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (about) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (about) Resume **");
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
		return about.class;
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
        BA.LogInfo("** Activity (about) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (about) Resume **");
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
public anywheresoftware.b4a.objects.StringUtils _su = null;
public anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lstone = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _abg = null;
public anywheresoftware.b4a.phone.Phone _ph = null;
public com.htetznaing.mmnayonfont.slidemenu _sm = null;
public anywheresoftware.b4a.objects.LabelWrapper _tlb = null;
public anywheresoftware.b4a.objects.ButtonWrapper _menu = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _mbg = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public com.htetznaing.mmnayonfont.main _main = null;
public com.htetznaing.mmnayonfont.main2 _main2 = null;
public com.htetznaing.mmnayonfont.samsung _samsung = null;
public com.htetznaing.mmnayonfont.oppo _oppo = null;
public com.htetznaing.mmnayonfont.huawei _huawei = null;
public com.htetznaing.mmnayonfont.vivo _vivo = null;
public com.htetznaing.mmnayonfont.tutorial _tutorial = null;
public com.htetznaing.mmnayonfont.xiaomi _xiaomi = null;
public com.htetznaing.mmnayonfont.other _other = null;
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
anywheresoftware.b4a.objects.ImageViewWrapper _imvlogo = null;
anywheresoftware.b4a.objects.LabelWrapper _lblname = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _c = null;
anywheresoftware.b4a.objects.LabelWrapper _lblcredit = null;
int _height = 0;
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="ph.SetScreenOrientation(1)";
mostCurrent._ph.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 31;BA.debugLine="abg.Initialize(LoadBitmap(File.DirAssets,\"bg.jpg\"";
mostCurrent._abg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 32;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 34;BA.debugLine="Dim imvLogo As ImageView";
_imvlogo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="imvLogo.Initialize (\"imv\")";
_imvlogo.Initialize(mostCurrent.activityBA,"imv");
 //BA.debugLineNum = 36;BA.debugLine="imvLogo.Bitmap = LoadBitmap(File.DirAssets , \"ico";
_imvlogo.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon.png").getObject()));
 //BA.debugLineNum = 37;BA.debugLine="imvLogo.Gravity = Gravity.FILL";
_imvlogo.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 38;BA.debugLine="Activity.AddView ( imvLogo , 50%x - 50dip  , 65di";
mostCurrent._activity.AddView((android.view.View)(_imvlogo.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (65)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 40;BA.debugLine="Dim lblName As  Label";
_lblname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 42;BA.debugLine="bg.Initialize (Colors.DarkGray , 10dip)";
_bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.DarkGray,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 43;BA.debugLine="lblName.Initialize (\"lbname\")";
_lblname.Initialize(mostCurrent.activityBA,"lbname");
 //BA.debugLineNum = 44;BA.debugLine="lblName.Background = bg";
_lblname.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 45;BA.debugLine="lblName.Gravity = Gravity.CENTER";
_lblname.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 46;BA.debugLine="lblName.Text = \"Myanmar Nayon Font\"";
_lblname.setText(BA.ObjectToCharSequence("Myanmar Nayon Font"));
 //BA.debugLineNum = 47;BA.debugLine="lblName.TextSize = 13";
_lblname.setTextSize((float) (13));
 //BA.debugLineNum = 48;BA.debugLine="lblName.TextColor = Colors.White";
_lblname.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 49;BA.debugLine="Activity.AddView (lblName , 100%x / 2 - 90dip , (";
mostCurrent._activity.AddView((android.view.View)(_lblname.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90))),(int) ((_imvlogo.getHeight()+_imvlogo.getTop())+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (180)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 50;BA.debugLine="lblName.Height = su.MeasureMultilineTextHeight (l";
_lblname.setHeight((int) (mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(_lblname.getObject()),BA.ObjectToCharSequence(_lblname.getText()))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 53;BA.debugLine="Dim c As ColorDrawable";
_c = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 54;BA.debugLine="c.Initialize (Colors.White , 10dip )";
_c.Initialize(anywheresoftware.b4a.keywords.Common.Colors.White,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 55;BA.debugLine="lstOne.Initialize (\"lstOnes\")";
mostCurrent._lstone.Initialize(mostCurrent.activityBA,"lstOnes");
 //BA.debugLineNum = 56;BA.debugLine="lstOne.Background = c";
mostCurrent._lstone.setBackground((android.graphics.drawable.Drawable)(_c.getObject()));
 //BA.debugLineNum = 57;BA.debugLine="lstOne.SingleLineLayout .Label.TextSize = 12";
mostCurrent._lstone.getSingleLineLayout().Label.setTextSize((float) (12));
 //BA.debugLineNum = 58;BA.debugLine="lstOne.SingleLineLayout .Label .TextColor = Color";
mostCurrent._lstone.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 59;BA.debugLine="lstOne.SingleLineLayout .Label .Gravity = Gravity";
mostCurrent._lstone.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 60;BA.debugLine="lstOne.SingleLineLayout .ItemHeight = 40dip";
mostCurrent._lstone.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 61;BA.debugLine="lstOne.AddSingleLine2(\"App Name : Myanmar Nayon F";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("App Name : Myanmar Nayon Font"),(Object)(1));
 //BA.debugLineNum = 62;BA.debugLine="lstOne.AddSingleLine2(\"Version : 1.0\",2)";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("Version : 1.0"),(Object)(2));
 //BA.debugLineNum = 63;BA.debugLine="lstOne.AddSingleLine2 (\"Font Developer : Anonymou";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("Font Developer : Anonymous   "),(Object)(3));
 //BA.debugLineNum = 64;BA.debugLine="lstOne.AddSingleLine2 (\"Developed By : Khun Htetz";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("Developed By : Khun Htetz Naing    "),(Object)(4));
 //BA.debugLineNum = 65;BA.debugLine="lstOne.AddSingleLine2(\"Powered By : Myanmar Andro";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("Powered By : Myanmar Android App"),(Object)(5));
 //BA.debugLineNum = 66;BA.debugLine="lstOne.AddSingleLine2 (\"Website : www.MyanmarAndr";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("Website : www.MyanmarAndroidApp.com  "),(Object)(6));
 //BA.debugLineNum = 67;BA.debugLine="lstOne.AddSingleLine2 (\"Facebook : www.fb.com/MmF";
mostCurrent._lstone.AddSingleLine2(BA.ObjectToCharSequence("Facebook : www.fb.com/MmFreeAndroidApps   "),(Object)(7));
 //BA.debugLineNum = 68;BA.debugLine="Activity.AddView ( lstOne, 30dip , 170dip , 100%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lstone.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (280)));
 //BA.debugLineNum = 70;BA.debugLine="Dim lblCredit As Label";
_lblcredit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="lblCredit.Initialize (\"lblCredit\")";
_lblcredit.Initialize(mostCurrent.activityBA,"lblCredit");
 //BA.debugLineNum = 72;BA.debugLine="lblCredit.TextColor = Colors.Green";
_lblcredit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 73;BA.debugLine="lblCredit.TextSize = 13";
_lblcredit.setTextSize((float) (13));
 //BA.debugLineNum = 74;BA.debugLine="lblCredit.Gravity = Gravity.CENTER";
_lblcredit.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 75;BA.debugLine="lblCredit.Text = \"If You have any Problem You can";
_lblcredit.setText(BA.ObjectToCharSequence("If You have any Problem You can contact Me."));
 //BA.debugLineNum = 76;BA.debugLine="Activity.AddView (lblCredit, 10dip,(lstOne.Top+ls";
mostCurrent._activity.AddView((android.view.View)(_lblcredit.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) ((mostCurrent._lstone.getTop()+mostCurrent._lstone.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 77;BA.debugLine="lblCredit.Height = su.MeasureMultilineTextHeight";
_lblcredit.setHeight(mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(_lblcredit.getObject()),BA.ObjectToCharSequence(_lblcredit.getText())));
 //BA.debugLineNum = 79;BA.debugLine="tlb.Initialize(\"tlb\")";
mostCurrent._tlb.Initialize(mostCurrent.activityBA,"tlb");
 //BA.debugLineNum = 80;BA.debugLine="tlb.Text = \"Myanmar Nayon Font\"";
mostCurrent._tlb.setText(BA.ObjectToCharSequence("Myanmar Nayon Font"));
 //BA.debugLineNum = 81;BA.debugLine="tlb.Color = Colors.rgb(76, 175, 80)";
mostCurrent._tlb.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (76),(int) (175),(int) (80)));
 //BA.debugLineNum = 82;BA.debugLine="tlb.TextColor = Colors.White";
mostCurrent._tlb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 83;BA.debugLine="tlb.TextSize = 20";
mostCurrent._tlb.setTextSize((float) (20));
 //BA.debugLineNum = 84;BA.debugLine="tlb.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._tlb.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 86;BA.debugLine="tlb.Gravity = Gravity.CENTER";
mostCurrent._tlb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 87;BA.debugLine="Activity.AddView(tlb,0%x,0%y,100%x,55dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tlb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55)));
 //BA.debugLineNum = 89;BA.debugLine="sm.Initialize(Activity, Me, \"SlideMenu\",0,70%x)";
mostCurrent._sm._initialize(mostCurrent.activityBA,mostCurrent._activity,about.getObject(),"SlideMenu",(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 90;BA.debugLine="sm.AddItem(\"Samsung\",LoadBitmap(File.DirAssets,\"s";
mostCurrent._sm._additem("Samsung",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"samsung.png"),(Object)(1));
 //BA.debugLineNum = 91;BA.debugLine="sm.AddItem(\"Oppo\",LoadBitmap(File.DirAssets,\"oppo";
mostCurrent._sm._additem("Oppo",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"oppo.png"),(Object)(2));
 //BA.debugLineNum = 92;BA.debugLine="sm.AddItem(\"Vivo\",LoadBitmap(File.DirAssets,\"vivo";
mostCurrent._sm._additem("Vivo",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"vivo.png"),(Object)(3));
 //BA.debugLineNum = 93;BA.debugLine="sm.AddItem(\"Huawei\",LoadBitmap(File.DirAssets,\"hu";
mostCurrent._sm._additem("Huawei",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"huawei.jpg"),(Object)(4));
 //BA.debugLineNum = 94;BA.debugLine="sm.AddItem(\"Xiaomi\",LoadBitmap(File.DirAssets,\"xi";
mostCurrent._sm._additem("Xiaomi",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"xiaomi.png"),(Object)(5));
 //BA.debugLineNum = 95;BA.debugLine="sm.AddItem(\"Other [#Root]\",LoadBitmap(File.DirAss";
mostCurrent._sm._additem("Other [#Root]",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"other.png"),(Object)(6));
 //BA.debugLineNum = 96;BA.debugLine="sm.AddItem(\"About\",LoadBitmap(File.DirAssets,\"abo";
mostCurrent._sm._additem("About",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"about.png"),(Object)(9));
 //BA.debugLineNum = 98;BA.debugLine="mbg.Initialize(LoadBitmap(File.DirAssets,\"menu.pn";
mostCurrent._mbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu.png").getObject()));
 //BA.debugLineNum = 99;BA.debugLine="menu.Initialize(\"menu\")";
mostCurrent._menu.Initialize(mostCurrent.activityBA,"menu");
 //BA.debugLineNum = 100;BA.debugLine="menu.Background = mbg";
mostCurrent._menu.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mbg.getObject()));
 //BA.debugLineNum = 101;BA.debugLine="menu.Gravity = Gravity.CENTER";
mostCurrent._menu.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 102;BA.debugLine="Activity.AddView(menu,10dip,12.5dip,30dip,30dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._menu.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 107;BA.debugLine="Banner.Initialize2(\"Banner\",\"ca-app-pub-417334857";
mostCurrent._banner.Initialize2(mostCurrent.activityBA,"Banner","ca-app-pub-4173348573252986/3885084957",mostCurrent._banner.SIZE_SMART_BANNER);
 //BA.debugLineNum = 108;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 109;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 111;BA.debugLine="If 100%x > 100%y Then height = 32dip Else height";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 114;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 116;BA.debugLine="Activity.AddView(Banner, 0dip, 100%y - height, 10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 117;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 118;BA.debugLine="Log(Banner)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._banner));
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 164;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 14;BA.debugLine="Dim su As StringUtils";
mostCurrent._su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 15;BA.debugLine="Dim p As PhoneIntents";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 16;BA.debugLine="Dim lstOne As ListView";
mostCurrent._lstone = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim abg As BitmapDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 20;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 21;BA.debugLine="Dim sm As SlideMenu";
mostCurrent._sm = new com.htetznaing.mmnayonfont.slidemenu();
 //BA.debugLineNum = 22;BA.debugLine="Dim tlb As Label";
mostCurrent._tlb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim menu As Button";
mostCurrent._menu = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim mbg As BitmapDrawable";
mostCurrent._mbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 25;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _imv_click() throws Exception{
 //BA.debugLineNum = 135;BA.debugLine="Sub imv_Click";
 //BA.debugLineNum = 136;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.myanmaran";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.myanmarandroidapp.com/search?q=Myanmar+Metrix+Font")));
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _lblcredit_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 143;BA.debugLine="Sub lblCredit_Click";
 //BA.debugLineNum = 144;BA.debugLine="Try";
try { //BA.debugLineNum = 146;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb://";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 149;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 153;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 154;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.facebook.";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 156;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _lbname_click() throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Sub lbname_Click";
 //BA.debugLineNum = 140;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.MyanmarAn";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.MyanmarAndroidApp.com")));
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _lstones_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 168;BA.debugLine="Sub lstOnes_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 169;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)(1),(Object)(3),(Object)(4),(Object)(1),(Object)(5),(Object)(6),(Object)(7))) {
case 0: {
 //BA.debugLineNum = 171;BA.debugLine="Try";
try { //BA.debugLineNum = 173;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 175;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 176;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 180;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 181;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 183;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 1: {
 //BA.debugLineNum = 188;BA.debugLine="Try";
try { //BA.debugLineNum = 190;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 192;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 193;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e18) {
			processBA.setLastException(e18); //BA.debugLineNum = 197;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 198;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 200;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 2: {
 //BA.debugLineNum = 204;BA.debugLine="Try";
try { //BA.debugLineNum = 206;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 208;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 209;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e28) {
			processBA.setLastException(e28); //BA.debugLineNum = 213;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 214;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 216;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 3: {
 //BA.debugLineNum = 220;BA.debugLine="Try";
try { //BA.debugLineNum = 222;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 224;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 225;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e38) {
			processBA.setLastException(e38); //BA.debugLineNum = 229;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 230;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 232;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 4: {
 //BA.debugLineNum = 236;BA.debugLine="Try";
try { //BA.debugLineNum = 238;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 240;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 241;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e48) {
			processBA.setLastException(e48); //BA.debugLineNum = 245;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 246;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 248;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
case 5: {
 //BA.debugLineNum = 252;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.Myanmar";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.MyanmarAndroidApp.com")));
 break; }
case 6: {
 //BA.debugLineNum = 254;BA.debugLine="Try";
try { //BA.debugLineNum = 256;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 258;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 259;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e60) {
			processBA.setLastException(e60); //BA.debugLineNum = 263;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 264;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.faceboo";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 266;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 break; }
}
;
 //BA.debugLineNum = 269;BA.debugLine="End Sub";
return "";
}
public static String  _menu_click() throws Exception{
 //BA.debugLineNum = 299;BA.debugLine="Sub menu_Click";
 //BA.debugLineNum = 300;BA.debugLine="If sm.isVisible Then";
if (mostCurrent._sm._isvisible()) { 
 //BA.debugLineNum = 301;BA.debugLine="sm.Hide";
mostCurrent._sm._hide();
 }else {
 //BA.debugLineNum = 303;BA.debugLine="sm.Show";
mostCurrent._sm._show();
 };
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _slidemenu_click(Object _item) throws Exception{
 //BA.debugLineNum = 271;BA.debugLine="Sub SlideMenu_Click(Item As Object)";
 //BA.debugLineNum = 272;BA.debugLine="sm.Hide";
mostCurrent._sm._hide();
 //BA.debugLineNum = 297;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 122;BA.debugLine="Sub t_Tick";
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
}
