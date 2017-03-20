Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: false
#End Region
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim wb As WebView
	Dim wv As WebViewSettings
	Dim tlb,lb As Label
	Dim Banner As AdView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	wb.Initialize("wb")
	Activity.AddView(wb,0%x,55dip,100%x,100%y)
	wb.LoadUrl("http://www.htetznaing.com/2017/02/Myanmar-Font-Styles.html")
	wv.setDisplayZoomControls(wb , False)
	
	tlb.Initialize("tlb")
	tlb.Text = "Tutorial"
	tlb.Color = Colors.rgb(76, 175, 80)
	tlb.TextColor = Colors.White
	tlb.TextSize = 25
	tlb.Typeface = Typeface.DEFAULT_BOLD
	
	tlb.Gravity = Gravity.CENTER
	Activity.AddView(tlb,0%x,0%y,100%x,55dip)
	
	Banner.Initialize2("Banner","ca-app-pub-4173348573252986/3885084957",Banner.SIZE_SMART_BANNER)
	Dim height As Int
	If GetDeviceLayoutValues.ApproximateScreenSize < 6 Then
		'phones
		If 100%x > 100%y Then height = 32dip Else height = 50dip
	Else
		'tablets
		height = 90dip
	End If
	Activity.AddView(Banner, 0dip, 100%y - height, 100%x, height)
	Banner.LoadAd
	Log(Banner)
	
	lb.Initialize("lb")
	lb.SetBackgroundImage(LoadBitmap(File.DirAssets,"open.png"))
	Activity.AddView(lb,100%x - 40dip,12.5dip,30dip,30dip)
End Sub

Sub lb_Click
	Dim p As PhoneIntents
	StartActivity(p.OpenBrowser("http://www.htetznaing.com/2017/02/Myanmar-Font-Styles.html"))
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub