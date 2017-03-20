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

public class samsung extends Activity implements B4AActivity{
	public static samsung mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mmnayonfont", "com.htetznaing.mmnayonfont.samsung");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (samsung).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mmnayonfont", "com.htetznaing.mmnayonfont.samsung");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mmnayonfont.samsung", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (samsung) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (samsung) Resume **");
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
		return samsung.class;
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
        BA.LogInfo("** Activity (samsung) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (samsung) Resume **");
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
public anywheresoftware.b4a.phone.Phone _ph = null;
public anywheresoftware.b4a.objects.LabelWrapper _b1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _b2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _b3 = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _interstitial = null;
public com.htetznaing.mmnayonfont.slidemenu _sm = null;
public anywheresoftware.b4a.objects.LabelWrapper _tlb = null;
public anywheresoftware.b4a.objects.ButtonWrapper _menu = null;
public anywheresoftware.b4a.objects.ButtonWrapper _share = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _sbg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _mbg = null;
public b4a.util.BClipboard _copy = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _mm = null;
public com.htetznaing.mmnayonfont.main _main = null;
public com.htetznaing.mmnayonfont.main2 _main2 = null;
public com.htetznaing.mmnayonfont.oppo _oppo = null;
public com.htetznaing.mmnayonfont.huawei _huawei = null;
public com.htetznaing.mmnayonfont.vivo _vivo = null;
public com.htetznaing.mmnayonfont.tutorial _tutorial = null;
public com.htetznaing.mmnayonfont.about _about = null;
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
anywheresoftware.b4a.objects.drawable.ColorDrawable _b1bg = null;
int _height = 0;
 //BA.debugLineNum = 26;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 28;BA.debugLine="lb.Initialize(\"lb\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"lb");
 //BA.debugLineNum = 29;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 30;BA.debugLine="lb.Text = \"	Install ကိုနွိပ္ျပီး Myanmar Nayon(iF";
mostCurrent._lb.setText(BA.ObjectToCharSequence("	Install ကိုနွိပ္ျပီး Myanmar Nayon(iFont) ကို Install လုပ္ေပးလိုက္ပါ။  Install လုပ္လို့ၿပီးရင္ Done ကိုသာနွိပ္ေပးပါ။ ျပီးရင္ေအာက္ပါ Change Font ကိုနွိပ္ျပီး Myanmar Nayon(iFont) ကိုေရြးေပးလိုက္ပါ။ နဂိုမူလေဖာင့္ကိုျပန္ထားခ်င္ရင္ Change Font ကိုနွိပ္ျပီး Default ကိုျပန္ေရြးထားနိုင္ပါတယ္။"));
 //BA.debugLineNum = 31;BA.debugLine="Activity.AddView(lb,2%x,55dip+1%y,90%x,35%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55))+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (35),mostCurrent.activityBA));
 //BA.debugLineNum = 32;BA.debugLine="lb.TextColor = Colors.Black";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 33;BA.debugLine="lb.Typeface = mm";
mostCurrent._lb.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 35;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 36;BA.debugLine="ph.SetScreenOrientation(1)";
mostCurrent._ph.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 38;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 39;BA.debugLine="Dim b1bg As ColorDrawable";
_b1bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 40;BA.debugLine="b1bg.Initialize(Colors.Black,10)";
_b1bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Black,(int) (10));
 //BA.debugLineNum = 41;BA.debugLine="b1.Text = \"Install\"";
mostCurrent._b1.setText(BA.ObjectToCharSequence("Install"));
 //BA.debugLineNum = 42;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 43;BA.debugLine="b1.Gravity = Gravity.CENTER";
mostCurrent._b1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 44;BA.debugLine="b1.Textcolor = Colors.White";
mostCurrent._b1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 45;BA.debugLine="b1.TextSize = 17";
mostCurrent._b1.setTextSize((float) (17));
 //BA.debugLineNum = 46;BA.debugLine="Activity.AddView(b1,20%x,(lb.Height+lb.Top)+1%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._lb.getHeight()+mostCurrent._lb.getTop())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 48;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 49;BA.debugLine="b2.Background = b1bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 50;BA.debugLine="b2.Text = \"Change Font\"";
mostCurrent._b2.setText(BA.ObjectToCharSequence("Change Font"));
 //BA.debugLineNum = 51;BA.debugLine="b2.Gravity = Gravity.CENTER";
mostCurrent._b2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 52;BA.debugLine="b2.Textcolor = Colors.White";
mostCurrent._b2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 53;BA.debugLine="b2.TextSize = 17";
mostCurrent._b2.setTextSize((float) (17));
 //BA.debugLineNum = 54;BA.debugLine="Activity.AddView(b2,20%x,(b1.Top+b1.Height)+2%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b1.getTop()+mostCurrent._b1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 56;BA.debugLine="b3.Initialize(\"b3\")";
mostCurrent._b3.Initialize(mostCurrent.activityBA,"b3");
 //BA.debugLineNum = 57;BA.debugLine="b3.Text = \"Tutorial\"";
mostCurrent._b3.setText(BA.ObjectToCharSequence("Tutorial"));
 //BA.debugLineNum = 58;BA.debugLine="b3.Background = b1bg";
mostCurrent._b3.setBackground((android.graphics.drawable.Drawable)(_b1bg.getObject()));
 //BA.debugLineNum = 59;BA.debugLine="b3.Gravity = Gravity.CENTER";
mostCurrent._b3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 60;BA.debugLine="b3.Textcolor = Colors.White";
mostCurrent._b3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 61;BA.debugLine="b3.TextSize = 17";
mostCurrent._b3.setTextSize((float) (17));
 //BA.debugLineNum = 62;BA.debugLine="Activity.AddView(b3,20%x,(b2.Top+b2.Height)+2%y,6";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._b2.getTop()+mostCurrent._b2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 65;BA.debugLine="tlb.Initialize(\"tlb\")";
mostCurrent._tlb.Initialize(mostCurrent.activityBA,"tlb");
 //BA.debugLineNum = 66;BA.debugLine="tlb.Text = \"Samsung\"";
mostCurrent._tlb.setText(BA.ObjectToCharSequence("Samsung"));
 //BA.debugLineNum = 67;BA.debugLine="tlb.Color = Colors.rgb(76, 175, 80)";
mostCurrent._tlb.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (76),(int) (175),(int) (80)));
 //BA.debugLineNum = 68;BA.debugLine="tlb.TextColor = Colors.White";
mostCurrent._tlb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 69;BA.debugLine="tlb.TextSize = 25";
mostCurrent._tlb.setTextSize((float) (25));
 //BA.debugLineNum = 70;BA.debugLine="tlb.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._tlb.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 72;BA.debugLine="tlb.Gravity = Gravity.CENTER";
mostCurrent._tlb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 73;BA.debugLine="Activity.AddView(tlb,0%x,0%y,100%x,55dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tlb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55)));
 //BA.debugLineNum = 75;BA.debugLine="sm.Initialize(Activity, Me, \"SlideMenu\",0,70%x)";
mostCurrent._sm._initialize(mostCurrent.activityBA,mostCurrent._activity,samsung.getObject(),"SlideMenu",(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 76;BA.debugLine="sm.AddItem(\"Samsung\",LoadBitmap(File.DirAssets,\"s";
mostCurrent._sm._additem("Samsung",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"samsung.png"),(Object)(1));
 //BA.debugLineNum = 77;BA.debugLine="sm.AddItem(\"Oppo\",LoadBitmap(File.DirAssets,\"oppo";
mostCurrent._sm._additem("Oppo",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"oppo.png"),(Object)(2));
 //BA.debugLineNum = 78;BA.debugLine="sm.AddItem(\"Vivo\",LoadBitmap(File.DirAssets,\"vivo";
mostCurrent._sm._additem("Vivo",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"vivo.png"),(Object)(3));
 //BA.debugLineNum = 79;BA.debugLine="sm.AddItem(\"Huawei\",LoadBitmap(File.DirAssets,\"hu";
mostCurrent._sm._additem("Huawei",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"huawei.jpg"),(Object)(4));
 //BA.debugLineNum = 80;BA.debugLine="sm.AddItem(\"Xiaomi\",LoadBitmap(File.DirAssets,\"xi";
mostCurrent._sm._additem("Xiaomi",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"xiaomi.png"),(Object)(5));
 //BA.debugLineNum = 81;BA.debugLine="sm.AddItem(\"Other [#Root]\",LoadBitmap(File.DirAss";
mostCurrent._sm._additem("Other [#Root]",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"other.png"),(Object)(6));
 //BA.debugLineNum = 82;BA.debugLine="sm.AddItem(\"Share App\",LoadBitmap(File.DirAssets,";
mostCurrent._sm._additem("Share App",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png"),(Object)(7));
 //BA.debugLineNum = 83;BA.debugLine="sm.AddItem(\"More App\",LoadBitmap(File.DirAssets,\"";
mostCurrent._sm._additem("More App",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"moreapp.png"),(Object)(8));
 //BA.debugLineNum = 84;BA.debugLine="sm.AddItem(\"About\",LoadBitmap(File.DirAssets,\"abo";
mostCurrent._sm._additem("About",anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"about.png"),(Object)(9));
 //BA.debugLineNum = 86;BA.debugLine="mbg.Initialize(LoadBitmap(File.DirAssets,\"menu.pn";
mostCurrent._mbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu.png").getObject()));
 //BA.debugLineNum = 87;BA.debugLine="menu.Initialize(\"menu\")";
mostCurrent._menu.Initialize(mostCurrent.activityBA,"menu");
 //BA.debugLineNum = 88;BA.debugLine="menu.Background = mbg";
mostCurrent._menu.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mbg.getObject()));
 //BA.debugLineNum = 89;BA.debugLine="menu.Gravity = Gravity.CENTER";
mostCurrent._menu.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 90;BA.debugLine="Activity.AddView(menu,10dip,12.5dip,30dip,30dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._menu.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 92;BA.debugLine="sbg.Initialize(LoadBitmap(File.DirAssets,\"share.p";
mostCurrent._sbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()));
 //BA.debugLineNum = 93;BA.debugLine="share.Initialize(\"share\")";
mostCurrent._share.Initialize(mostCurrent.activityBA,"share");
 //BA.debugLineNum = 94;BA.debugLine="share.Background = sbg";
mostCurrent._share.setBackground((android.graphics.drawable.Drawable)(mostCurrent._sbg.getObject()));
 //BA.debugLineNum = 95;BA.debugLine="share.Gravity = Gravity.CENTER";
mostCurrent._share.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 96;BA.debugLine="Activity.AddView(share,100%x - 40dip,12.5dip,30di";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._share.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (12.5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 98;BA.debugLine="Banner.Initialize2(\"Banner\",\"ca-app-pub-417334857";
mostCurrent._banner.Initialize2(mostCurrent.activityBA,"Banner","ca-app-pub-4173348573252986/3885084957",mostCurrent._banner.SIZE_SMART_BANNER);
 //BA.debugLineNum = 99;BA.debugLine="Dim height As Int";
_height = 0;
 //BA.debugLineNum = 100;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 102;BA.debugLine="If 100%x > 100%y Then height = 32dip Else height";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 105;BA.debugLine="height = 90dip";
_height = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddView(Banner, 0dip, 100%y - height, 10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_height),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_height);
 //BA.debugLineNum = 108;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 109;BA.debugLine="Log(Banner)";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._banner));
 //BA.debugLineNum = 111;BA.debugLine="Interstitial.Initialize(\"Interstitial\",\"ca-app-pu";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/5361818159");
 //BA.debugLineNum = 112;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 114;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 115;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="ad2.Initialize(\"ad2\",60000)";
_ad2.Initialize(processBA,"ad2",(long) (60000));
 //BA.debugLineNum = 117;BA.debugLine="ad2.Enabled = True";
_ad2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 184;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 157;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 158;BA.debugLine="If Interstitial.Ready Then Interstitial.Show";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();};
 //BA.debugLineNum = 159;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _ad2_tick() throws Exception{
 //BA.debugLineNum = 180;BA.debugLine="Sub ad2_Tick";
 //BA.debugLineNum = 181;BA.debugLine="If Interstitial.Ready Then Interstitial.Show";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();};
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 120;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 121;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="File.Copy(File.DirAssets,\"Nayon.apk\",File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Nayon.apk",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Nayon.apk");
 //BA.debugLineNum = 123;BA.debugLine="Msgbox(\"1. Install Fone App\" & CRLF & \"2. After I";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("1. Install Fone App"+anywheresoftware.b4a.keywords.Common.CRLF+"2. After Installed click done!"+anywheresoftware.b4a.keywords.Common.CRLF+"3. Click Change Font and Choose your Font!"),BA.ObjectToCharSequence("Attention!"),mostCurrent.activityBA);
 //BA.debugLineNum = 124;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 125;BA.debugLine="i.Initialize(i.ACTION_VIEW,\"file:///\"&File.DirRoo";
_i.Initialize(_i.ACTION_VIEW,"file:///"+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Nayon.apk");
 //BA.debugLineNum = 126;BA.debugLine="i.SetType(\"application/vnd.android.package-archiv";
_i.SetType("application/vnd.android.package-archive");
 //BA.debugLineNum = 127;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
anywheresoftware.b4a.objects.IntentWrapper _abc = null;
 //BA.debugLineNum = 130;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 131;BA.debugLine="If File.Exists(File.DirRootExternal,\"Nayon.apk\")";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Nayon.apk")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 132;BA.debugLine="File.Delete(File.DirRootExternal,\"Nayon.apk\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Nayon.apk");
 };
 //BA.debugLineNum = 134;BA.debugLine="Try";
try { //BA.debugLineNum = 135;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 136;BA.debugLine="i.Initialize(\"\", \"\")";
_i.Initialize("","");
 //BA.debugLineNum = 137;BA.debugLine="i.SetComponent(\"com.android.settings/.flipfont.F";
_i.SetComponent("com.android.settings/.flipfont.FontListActivity");
 //BA.debugLineNum = 138;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 } 
       catch (Exception e10) {
			processBA.setLastException(e10); //BA.debugLineNum = 140;BA.debugLine="Dim abc As Intent";
_abc = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 141;BA.debugLine="abc.Initialize(\"android.settings.DISPLAY_SETTING";
_abc.Initialize("android.settings.DISPLAY_SETTINGS","");
 //BA.debugLineNum = 142;BA.debugLine="StartActivity(abc)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_abc.getObject()));
 };
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _b3_click() throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub b3_Click";
 //BA.debugLineNum = 147;BA.debugLine="If File.Exists(File.DirRootExternal,\"Nayon.apk\")";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Nayon.apk")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 148;BA.debugLine="File.Delete(File.DirRootExternal,\"Nayon.apk\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Nayon.apk");
 };
 //BA.debugLineNum = 150;BA.debugLine="StartActivity(Tutorial)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._tutorial.getObject()));
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 11;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 12;BA.debugLine="Dim b1,b2,b3 As Label";
mostCurrent._b1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._b3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim Interstitial As InterstitialAd";
mostCurrent._interstitial = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim sm As SlideMenu";
mostCurrent._sm = new com.htetznaing.mmnayonfont.slidemenu();
 //BA.debugLineNum = 18;BA.debugLine="Dim tlb As Label";
mostCurrent._tlb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim menu,share As Button";
mostCurrent._menu = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._share = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim sbg,mbg As BitmapDrawable";
mostCurrent._sbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._mbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 21;BA.debugLine="Dim copy As BClipboard";
mostCurrent._copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 22;BA.debugLine="Dim lb As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim mm As Typeface : mm = mm.LoadFromAssets(\"Nayo";
mostCurrent._mm = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim mm As Typeface : mm = mm.LoadFromAssets(\"Nayo";
mostCurrent._mm.setObject((android.graphics.Typeface)(mostCurrent._mm.LoadFromAssets("Nayon.ttf")));
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_adclosed() throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub Interstitial_AdClosed";
 //BA.debugLineNum = 164;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_adopened() throws Exception{
 //BA.debugLineNum = 176;BA.debugLine="Sub Interstitial_adopened";
 //BA.debugLineNum = 177;BA.debugLine="Log(\"Opened\")";
anywheresoftware.b4a.keywords.Common.Log("Opened");
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub Interstitial_FailedToReceiveAd (ErrorCode As S";
 //BA.debugLineNum = 172;BA.debugLine="Log(\"not Received - \" &\"Error Code: \"&ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("not Received - "+"Error Code: "+_errorcode);
 //BA.debugLineNum = 173;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 174;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_receivead() throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub Interstitial_ReceiveAd";
 //BA.debugLineNum = 168;BA.debugLine="Log(\"Received\")";
anywheresoftware.b4a.keywords.Common.Log("Received");
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _menu_click() throws Exception{
 //BA.debugLineNum = 222;BA.debugLine="Sub menu_Click";
 //BA.debugLineNum = 223;BA.debugLine="If sm.isVisible Then sm.Hide Else sm.Show";
if (mostCurrent._sm._isvisible()) { 
mostCurrent._sm._hide();}
else {
mostCurrent._sm._show();};
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim ad1,ad2 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
_ad2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
 //BA.debugLineNum = 226;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 227;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 228;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 229;BA.debugLine="copy.setText(\"#Myanmar_Nayon_Font App! Beautiful";
mostCurrent._copy.setText(mostCurrent.activityBA,"#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://www.htetznaing.com/search?q=Myanmar+Nayon+Font");
 //BA.debugLineNum = 230;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 231;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 232;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 233;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 234;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 235;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 236;BA.debugLine="End Sub";
return "";
}
public static String  _slidemenu_click(Object _item) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
 //BA.debugLineNum = 189;BA.debugLine="Sub SlideMenu_Click(Item As Object)";
 //BA.debugLineNum = 190;BA.debugLine="sm.Hide";
mostCurrent._sm._hide();
 //BA.debugLineNum = 191;BA.debugLine="Select Item";
switch (BA.switchObjectToInt(_item,(Object)(1),(Object)(2),(Object)(3),(Object)(4),(Object)(5),(Object)(6),(Object)(7),(Object)(8),(Object)(9))) {
case 0: {
 //BA.debugLineNum = 193;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,samsung.getObject());
 break; }
case 1: {
 //BA.debugLineNum = 195;BA.debugLine="StartActivity(Oppo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._oppo.getObject()));
 break; }
case 2: {
 //BA.debugLineNum = 197;BA.debugLine="StartActivity(Vivo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._vivo.getObject()));
 break; }
case 3: {
 //BA.debugLineNum = 199;BA.debugLine="StartActivity(Huawei)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._huawei.getObject()));
 break; }
case 4: {
 //BA.debugLineNum = 201;BA.debugLine="StartActivity(Xiaomi)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._xiaomi.getObject()));
 break; }
case 5: {
 //BA.debugLineNum = 203;BA.debugLine="StartActivity(Other)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._other.getObject()));
 break; }
case 6: {
 //BA.debugLineNum = 205;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 206;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 207;BA.debugLine="copy.setText(\"#Myanmar_Nayon_Font App! Beautifu";
mostCurrent._copy.setText(mostCurrent.activityBA,"#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://bit.ly/2mqSEWy");
 //BA.debugLineNum = 208;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 209;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 210;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",c";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 211;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 212;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 213;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 break; }
case 7: {
 //BA.debugLineNum = 215;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 216;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetzna";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_p.OpenBrowser("http://www.htetznaing.com")));
 break; }
case 8: {
 //BA.debugLineNum = 218;BA.debugLine="StartActivity(About)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._about.getObject()));
 break; }
}
;
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
return "";
}
}
