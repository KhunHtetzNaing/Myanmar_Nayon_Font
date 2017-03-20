Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	Dim ad1,ad2 As Timer
	Dim ti,tr As Timer
	Dim ist,rst As Timer
End Sub

Sub Globals
	Dim ph As Phone
	Dim OS As String
	Dim b1,b2,b3 As Label
	Dim Banner As AdView
	Dim Interstitial As InterstitialAd
	Dim ml As MLfiles

	Dim sm As SlideMenu
	Dim tlb As Label
	Dim menu,share As Button
	Dim sbg,mbg As BitmapDrawable
	Dim copy As BClipboard
	Dim lb,lw As Label
	Dim mm As Typeface : mm = mm.LoadFromAssets("Nayon.ttf")
	Dim ml As MLfiles
	Dim rooot As String
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	Select ph.SdkVersion
		Case 2 : OS = "1.1"
		Case 3 : OS = "1.5"
		Case 4 : OS = "1.6"
		Case 5 : OS = "2.0"
		Case 6 : OS = "2.0.1"
		Case 7 : OS = "2.1"
		Case 8 : OS = "2.2"
		Case 9 : OS = "2.3 - 2.3.2"
		Case 10 : OS = "	2.3.3 - 2.3.7" ' 2.3.3 or 2.3.4
		Case 11 : OS = "3.0"
		Case 12 : OS = "3.1"
		Case 13 : OS = "3.2"
		Case 14 : OS = "	4.0.1 - 4.0.2"
		Case 15 : OS = "4.0.3 - 4.0.4"
		Case 16 : OS = "	4.1.x"
		Case 17 : OS = "	4.2.x"
		Case 18 : OS = 	"4.3.x"
		Case 19 : OS = "	4.4 - 4.4.4"
		Case 21: OS = "5.0"
		Case 22: OS = "5.1"
		Case 23: OS = "6.0"
		Case 24 : OS = "	7.0"
		Case 25 : OS = "	7.1"
		Case Else : OS = "?"
	End Select
	
	ml.GetRoot
	If ml.HaveRoot Then
		rooot = "Rooted"
	Else
		rooot = "Unroot"
	End If
	
	lw.Initialize("")
	lw.Text = "Warning :  This is beta version!" & CRLF & " Use at your own risk :)"
	lw.TextColor = Colors.Red
	lw.Gravity = Gravity.CENTER
	Activity.AddView(lw,1%x,55dip,100%x,10%y)
	
	lb.Initialize("lb")
	lb.Gravity = Gravity.CENTER
	lb.Text = "Brand Name : " & ph.Manufacturer & CRLF & "Device Name : " & ph.Model & CRLF & "Android Version : " & OS & CRLF & "Root Status : " & rooot
	Activity.AddView(lb,0%x,(lw.Height+lw.Top)+1%y,100%x,30%y)
	lb.TextColor = Colors.Black
	lb.Typeface = mm
	
	Activity.Color = Colors.White
	ph.SetScreenOrientation(1)
	
	b1.Initialize("b1")
	Dim b1bg As ColorDrawable
	b1bg.Initialize(Colors.Black,10)
	b1.Text = "Install"
	b1.Background = b1bg
	b1.Gravity = Gravity.CENTER
	b1.Textcolor = Colors.White
	b1.TextSize = 17
	Activity.AddView(b1,20%x,(lb.Height+lb.Top)+1%y,60%x,50dip)
	
	b2.Initialize("b2")
	b2.Background = b1bg
	b2.Text = "Restore"
	b2.Gravity = Gravity.CENTER
	b2.Textcolor = Colors.White
	b2.TextSize = 17
	Activity.AddView(b2,20%x,(b1.Top+b1.Height)+2%y,60%x,50dip)
	
	b3.Initialize("b3")
	b3.Text = "Tutorial"
	b3.Background = b1bg
	b3.Gravity = Gravity.CENTER
	b3.Textcolor = Colors.White
	b3.TextSize = 17
	Activity.AddView(b3,20%x,(b2.Top+b2.Height)+2%y,60%x,50dip)
	
	'NEWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
	tlb.Initialize("tlb")
	tlb.Text = "Other [#Root]"
	tlb.Color = Colors.rgb(76, 175, 80)
	tlb.TextColor = Colors.White
	tlb.TextSize = 25
	tlb.Typeface = Typeface.DEFAULT_BOLD
	
	tlb.Gravity = Gravity.CENTER
	Activity.AddView(tlb,0%x,0%y,100%x,55dip)
	
	sm.Initialize(Activity, Me, "SlideMenu",0,70%x)
	sm.AddItem("Samsung",LoadBitmap(File.DirAssets,"samsung.png"),1)
	sm.AddItem("Oppo",LoadBitmap(File.DirAssets,"oppo.png"),2)
	sm.AddItem("Vivo",LoadBitmap(File.DirAssets,"vivo.png"),3)
	sm.AddItem("Huawei",LoadBitmap(File.DirAssets,"huawei.jpg"),4)
	sm.AddItem("Xiaomi",LoadBitmap(File.DirAssets,"xiaomi.png"),5)
	sm.AddItem("Other [#Root]",LoadBitmap(File.DirAssets,"other.png"),6)
	sm.AddItem("Share App",LoadBitmap(File.DirAssets,"share.png"),7)
	sm.AddItem("More App",LoadBitmap(File.DirAssets,"moreapp.png"),8)
	sm.AddItem("About",LoadBitmap(File.DirAssets,"about.png"),9)
	
	mbg.Initialize(LoadBitmap(File.DirAssets,"menu.png"))
	menu.Initialize("menu")
	menu.Background = mbg
	menu.Gravity = Gravity.CENTER
	Activity.AddView(menu,10dip,12.5dip,30dip,30dip)
	
	sbg.Initialize(LoadBitmap(File.DirAssets,"share.png"))
	share.Initialize("share")
	share.Background = sbg
	share.Gravity = Gravity.CENTER
	Activity.AddView(share,100%x - 40dip,12.5dip,30dip,30dip)
	
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
	
	Interstitial.Initialize("Interstitial","ca-app-pub-4173348573252986/5361818159")
	Interstitial.LoadAd
	
	ad1.Initialize("ad1",100)
	ad1.Enabled = False
	ad2.Initialize("ad2",60000)
	ad2.Enabled = True
	
	ti.Initialize("ti",100)
	ti.Enabled = False
	tr.Initialize("tr",100)
	tr.Enabled = False
	
	ist.Initialize("ist",5000)
	ist.Enabled = False
	rst.Initialize("rst",5000)
	rst.Enabled = False
End Sub

Sub b1_Click
	ad1.Enabled = True
	ml.GetRoot
	If ml.HaveRoot Then
		File.Copy(File.DirAssets,"Nayon.ttf",File.DirRootExternal,"MyanmarHeart.ttf")
		ProgressDialogShow("Installing...")
		ti.Enabled = True
	Else
		Msgbox("Your device not have Root Access!","Attention!")
	End If
End Sub

Sub ti_Tick
	ml.RootCmd("mount -o rw,remount /system","",Null,Null,False)
	If ml.Exists("/system/Ht3tzN4ing.ttf") = False Then
		ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/Ht3tzN4ing.ttf","",Null,Null,False)
		'Padauk
		If ml.Exists("/system/fonts/Padauk.ttf") = True Then
			ml.mv("/system/fonts/Padauk.ttf","/system/fonts/Padauk.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Padauk.ttf",644)
		End If
		
		'Padauk-book
		If ml.Exists("/system/fonts/Padauk-book.ttf") = True Then
			ml.mv("/system/fonts/Padauk-book.ttf","/system/fonts/Padauk-book.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-book.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Padauk-book.ttf",644)
		End If
		
		'Padauk-bookbold
		If ml.Exists("/system/fonts/Padauk-bookbold.ttf") = True Then
			ml.mv("/system/fonts/Padauk-bookbold.ttf","/system/fonts/Padauk-bookbold.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-bookbold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Padauk-bookbold.ttf",644)
		End If
		
		'NotoSansMyanmar-Bold
		If ml.Exists("/system/fonts/NotoSansMyanmar-Bold.ttf") = True Then
			ml.mv("/system/fonts/NotoSansMyanmar-Bold.ttf","/system/fonts/NotoSansMyanmar-Bold.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmar-Bold.ttf",644)
		End If
		
		'NotoSansMyanmar-Regular
		If ml.Exists("/system/fonts/NotoSansMyanmar-Regular.ttf") = True Then
			ml.mv("/system/fonts/NotoSansMyanmar-Regular.ttf","/system/fonts/NotoSansMyanmar-Regular.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmar-Regular.ttf",644)
		End If
		
		'NotoSansMyanmarUI-Bold
		If ml.Exists("/system/fonts/NotoSansMyanmarUI-Bold.ttf") = True Then
			ml.mv("/system/fonts/NotoSansMyanmarUI-Bold.ttf","/system/fonts/NotoSansMyanmarUI-Bold.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarUI-Bold.ttf",644)
		End If
		
		'NotoSansMyanmarUI-Regular
		If ml.Exists("/system/fonts/NotoSansMyanmarUI-Regular.ttf") = True Then
			ml.mv("/system/fonts/NotoSansMyanmarUI-Regular.ttf","/system/fonts/NotoSansMyanmarUI-Regular.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarUI-Regular.ttf",644)
			
		End If
		
		'NotoSansMyanmarZawgyi-Bold
		If ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf") = True Then
			ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf","/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf",644)
		End If
		
		'NotoSansMyanmarZawgyi-Regular
		If ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf") = True Then
			ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf","/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf",644)
		End If
		
		'SamsungMyanmar
		If ml.Exists("/system/fonts/SamsungMyanmar.ttf") = True Then
			ml.mv("/system/fonts/SamsungMyanmar.ttf","/system/fonts/SamsungMyanmar.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/SamsungMyanmar.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/SamsungMyanmar.ttf",644)
		End If
		
		'ZawgyiOne
		If ml.Exists("/system/fonts/ZawgyiOne.ttf") = True Then
			ml.mv("/system/fonts/ZawgyiOne.ttf","/system/fonts/ZawgyiOne.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/ZawgyiOne.ttf",644)
		End If
	
		'ZawgyiOne2008
		If ml.Exists("/system/fonts/ZawgyiOne2008.ttf") = True Then
			ml.mv("/system/fonts/ZawgyiOne2008.ttf","/system/fonts/ZawgyiOne2008.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne2008.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/ZawgyiOne2008.ttf",644)
		End If
		
		'mmsd
		If ml.Exists("/system/fonts/mmsd.ttf") = True Then
			ml.mv("/system/fonts/mmsd.ttf","/system/fonts/mmsd.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/mmsd.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/mmsd.ttf",644)
		End If
	
		'Roboto
		If ml.Exists("/system/fonts/Roboto-Regular.ttf") = True Then
			ml.mv("/system/fonts/Roboto-Regular.ttf","/system/fonts/Roboto-Regular.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Roboto-Regular.ttf",644)
		End If
			
		If ml.Exists("/system/fonts/Roboto-Light.ttf") = True Then
			ml.mv("/system/fonts/Roboto-Light.ttf","/system/fonts/Roboto-Light.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Light.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Roboto-Light.ttf",644)
		End If
			
		If ml.Exists("/system/fonts/Roboto-Bold.ttf") = True Then
			ml.mv("/system/fonts/Roboto-Bold.ttf","/system/fonts/Roboto-Bold.ttf.bak")
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Roboto-Bold.ttf",644)
		End If
		'INSTALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
		ist.Enabled = True
		ti.Enabled = False
		
	Else
		'JGKLDJEIGUEOPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPJ
		'Padauk
		If ml.Exists("/system/fonts/Padauk.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Padauk.ttf",644)
		End If
		
		'Padauk-book
		If ml.Exists("/system/fonts/Padauk-book.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-book.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Padauk-book.ttf",644)
		End If
		
		'Padauk-bookbold
		If ml.Exists("/system/fonts/Padauk-bookbold.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Padauk-bookbold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Padauk-bookbold.ttf",644)
		End If
		
		'NotoSansMyanmar-Bold
		If ml.Exists("/system/fonts/NotoSansMyanmar-Bold.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmar-Bold.ttf",644)
		End If
		
		'NotoSansMyanmar-Regular
		If ml.Exists("/system/fonts/NotoSansMyanmar-Regular.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmar-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmar-Regular.ttf",644)
		End If
		
		'NotoSansMyanmarUI-Bold
		If ml.Exists("/system/fonts/NotoSansMyanmarUI-Bold.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarUI-Bold.ttf",644)
		End If
		
		'NotoSansMyanmarUI-Regular
		If ml.Exists("/system/fonts/NotoSansMyanmarUI-Regular.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarUI-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarUI-Regular.ttf",644)
			
		End If
		
		'NotoSansMyanmarZawgyi-Bold
		If ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf",644)
		End If
		
		'NotoSansMyanmarZawgyi-Regular
		If ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/NotoSansMyanmarZawgyi-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf",644)
		End If
		
		'SamsungMyanmar
		If ml.Exists("/system/fonts/SamsungMyanmar.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/SamsungMyanmar.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/SamsungMyanmar.ttf",644)
		End If
		
		'ZawgyiOne
		If ml.Exists("/system/fonts/ZawgyiOne.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/ZawgyiOne.ttf",644)
		End If
	
		'ZawgyiOne2008
		If ml.Exists("/system/fonts/ZawgyiOne2008.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/ZawgyiOne2008.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/ZawgyiOne2008.ttf",644)
		End If
		
		'mmsd
		If ml.Exists("/system/fonts/mmsd.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/mmsd.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/mmsd.ttf",644)
		End If
	
		'Roboto
		If ml.Exists("/system/fonts/Roboto-Regular.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Regular.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Roboto-Regular.ttf",644)
		End If
			
		If ml.Exists("/system/fonts/Roboto-Light.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Light.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Roboto-Light.ttf",644)
		End If
			
		If ml.Exists("/system/fonts/Roboto-Bold.ttf") = True Then
			ml.RootCmd("cp -r /sdcard/MyanmarHeart.ttf /system/fonts/Roboto-Bold.ttf","",Null,Null,False)
			ml.chmod("/system/fonts/Roboto-Bold.ttf",644)
		End If
		'INSTALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
		ist.Enabled = True
		ti.Enabled = False
	End If
End Sub
		
Sub ist_Tick
	ProgressDialogHide
	Msgbox("Congratulations! Myanmar Installed in your device" & CRLF& "Now, your device will be reboot!","Completed")
	ml.GetRoot
	If ml.HaveRoot Then
		ml.RootCmd("reboot","",Null,Null,False)
	End If
	ist.Enabled = False
End Sub

Sub b2_Click
	ml.GetRoot
	If ml.HaveRoot Then
		ProgressDialogShow("Please Wait...")
		tr.Enabled = True
	Else
		Msgbox("Your device not have Root Access!","Attention!")
	End If
End Sub

Sub tr_Tick
	ml.RootCmd("mount -o rw,remount /system","",Null,Null,False)
	'INSTALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
	'Padauk
	If ml.Exists("/system/fonts/Padauk.ttf.bak") = True Then
		ml.rm("/system/fonts/Padauk.ttf")
		ml.mv("/system/fonts/Padauk.ttf.bak","/system/fonts/Padauk.ttf")
		ml.chmod("/system/fonts/Padauk.ttf",644)
	End If
		
	'Padauk-book
	If ml.Exists("/system/fonts/Padauk-book.ttf.bak") = True Then
		ml.rm("/system/fonts/Padauk-book.ttf")
		ml.mv("/system/fonts/Padauk-book.ttf.bak","/system/fonts/Padauk-book.ttf")
		ml.chmod("/system/fonts/Padauk-book.ttf",644)
	End If
		
	'Padauk-bookbold
	If ml.Exists("/system/fonts/Padauk-bookbold.ttf.bak") = True Then
		ml.rm("/system/fonts/Padauk-bookbold.ttf")
		ml.mv("/system/fonts/Padauk-bookbold.ttf.bak","/system/fonts/Padauk-bookbold.ttf")
		ml.chmod("/system/fonts/Padauk-bookbold.ttf",644)
	End If
		
	'NotoSansMyanmar-Bold
	If ml.Exists("/system/fonts/NotoSansMyanmar-Bold.ttf.bak") = True Then
		ml.rm("/system/fonts/NotoSansMyanmar-Bold.ttf")
		ml.mv("/system/fonts/NotoSansMyanmar-Bold.ttf.bak","/system/fonts/NotoSansMyanmar-Bold.ttf")
		ml.chmod("/system/fonts/NotoSansMyanmar-Bold.ttf",644)
	End If
		
	'NotoSansMyanmar-Regular
	If ml.Exists("/system/fonts/NotoSansMyanmar-Regular.ttf.bak") = True Then
		ml.rm("/system/fonts/NotoSansMyanmar-Regular.ttf")
		ml.mv("/system/fonts/NotoSansMyanmar-Regular.ttf.bak","/system/fonts/NotoSansMyanmar-Regular.ttf")
		ml.chmod("/system/fonts/NotoSansMyanmar-Regular.ttf",644)
	End If
		
	'NotoSansMyanmarUI-Bold
	If ml.Exists("/system/fonts/NotoSansMyanmarUI-Bold.ttf.bak") = True Then
		ml.rm("/system/fonts/NotoSansMyanmarUI-Bold.ttf")
		ml.mv("/system/fonts/NotoSansMyanmarUI-Bold.ttf.bak","/system/fonts/NotoSansMyanmarUI-Bold.ttf")
		ml.chmod("/system/fonts/NotoSansMyanmarUI-Bold.ttf",644)
	End If
		
	'NotoSansMyanmarUI-Regular
	If ml.Exists("/system/fonts/NotoSansMyanmarUI-Regular.ttf.bak") = True Then
		ml.rm("/system/fonts/NotoSansMyanmarUI-Regular.ttf")
		ml.mv("/system/fonts/NotoSansMyanmarUI-Regular.ttf.bak","/system/fonts/NotoSansMyanmarUI-Regular.ttf")
		ml.chmod("/system/fonts/NotoSansMyanmarUI-Regular.ttf",644)
	End If
		
	'NotoSansMyanmarZawgyi-Bold
	If ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf.bak") = True Then
		ml.rm("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf")
		ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf.bak","/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf")
		ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Bold.ttf",644)
	End If
		
	'NotoSansMyanmarZawgyi-Regular
	If ml.Exists("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf.bak") = True Then
		ml.rm("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf")
		ml.mv("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf.bak","/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf")
		ml.chmod("/system/fonts/NotoSansMyanmarZawgyi-Regular.ttf",644)
	End If
		
	'SamsungMyanmar
	If ml.Exists("/system/fonts/SamsungMyanmar.ttf.bak") = True Then
		ml.rm("/system/fonts/SamsungMyanmar.ttf")
		ml.mv("/system/fonts/SamsungMyanmar.ttf.bak","/system/fonts/SamsungMyanmar.ttf")
		ml.chmod("/system/fonts/SamsungMyanmar.ttf",644)
	End If
		
	'ZawgyiOne
	If ml.Exists("/system/fonts/ZawgyiOne.ttf.bak") = True Then
		ml.rm("/system/fonts/ZawgyiOne.ttf")
		ml.mv("/system/fonts/ZawgyiOne.ttf.bak","/system/fonts/ZawgyiOne.ttf")
		ml.chmod("/system/fonts/ZawgyiOne.ttf",644)
	End If
	
	'ZawgyiOne2008
	If ml.Exists("/system/fonts/ZawgyiOne2008.ttf.bak") = True Then
		ml.rm("/system/fonts/ZawgyiOne2008.ttf")
		ml.mv("/system/fonts/ZawgyiOne2008.ttf.bak","/system/fonts/ZawgyiOne2008.ttf")
		ml.chmod("/system/fonts/ZawgyiOne2008.ttf",644)
	End If
		
	'mmsd
	If ml.Exists("/system/fonts/mmsd.ttf.bak") = True Then
		ml.rm("/system/fonts/mmsd.ttf")
		ml.mv("/system/fonts/mmsd.ttf.bak","/system/fonts/mmsd.ttf")
		ml.chmod("/system/fonts/mmsd.ttf",644)
	End If
	
	'Roboto
	If ml.Exists("/system/fonts/Roboto-Bold.ttf.bak") = True Then
		ml.rm("/system/fonts/Roboto-Bold.ttf")
		ml.mv("/system/fonts/Roboto-Bold.ttf.bak","/system/fonts/Roboto-Bold.ttf")
		ml.chmod("/system/fonts/Roboto-Bold.ttf",644)
	End If
		
	If ml.Exists("/system/fonts/Roboto-Light.ttf.bak") = True Then
		ml.rm("/system/fonts/Roboto-Light.ttf")
		ml.mv("/system/fonts/Roboto-Light.ttf.bak","/system/fonts/Roboto-Light.ttf")
		ml.chmod("/system/fonts/Roboto-Light.ttf",644)
	End If
		
	If ml.Exists("/system/fonts/Roboto-Regular.ttf.bak") = True Then
		ml.rm("/system/fonts/Roboto-Regular.ttf")
		ml.mv("/system/fonts/Roboto-Regular.ttf.bak","/system/fonts/Roboto-Regular.ttf")
		ml.chmod("/system/fonts/Roboto-Regular.ttf",644)
	End If
	
	If ml.Exists("/system/Ht3tzN4ing.ttf") = True Then
		ml.rm("/system/Ht3tzN4ing.ttf")
	End If
	'INSTALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL
	rst.Enabled = True
	tr.Enabled = False
End Sub

Sub rst_Tick
	ml.GetRoot
	If ml.HaveRoot Then
		ProgressDialogHide
		Msgbox("Congratulations! Original Restored" & CRLF& "Now, your device will be reboot!","Completed")
		ml.RootCmd("reboot","",Null,Null,False)
	Else
		Msgbox("Your device not have Root Access!","Attention!")
	End If
	rst.Enabled = False
End Sub

Sub b3_Click
	StartActivity(Tutorial)
End Sub

Sub Activity_Resume
	Dim in As Intent
	Dim pm As PackageManager
	in = pm.GetApplicationIntent("com.xinmei365.fonu")
	If in.IsInitialized Then
		Dim ml As MLfiles
		ml.rmrf(File.DirRootExternal & "/.MyanmarHeartFont")
	End If
End Sub

Sub ad1_Tick
	If Interstitial.Ready Then Interstitial.Show
	ad1.Enabled = False
End Sub


Sub Interstitial_AdClosed
	Interstitial.LoadAd
End Sub

Sub Interstitial_ReceiveAd
	Log("Received")
End Sub

Sub Interstitial_FailedToReceiveAd (ErrorCode As String)
	Log("not Received - " &"Error Code: "&ErrorCode)
	Interstitial.LoadAd
End Sub

Sub Interstitial_adopened
	Log("Opened")
End Sub

Sub ad2_Tick
	If Interstitial.Ready Then Interstitial.Show
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Dim in As Intent
	Dim pm As PackageManager
	in = pm.GetApplicationIntent("com.xinmei365.fonu")
	If in.IsInitialized Then
		Dim ml As MLfiles
		ml.rmrf(File.DirRootExternal & "/.MyanmarHeartFont")
	End If
End Sub


Sub SlideMenu_Click(Item As Object)
	sm.Hide
	Select Item
		Case 1 :
			StartActivity(Me)
		Case 2 :
			StartActivity(Oppo)
		Case 3 :
			StartActivity(Vivo)
		Case 4 :
			StartActivity(Huawei)
		Case 5 :
			StartActivity(Xiaomi)
		Case 6 :
			StartActivity(Me)
		Case 7 :
			Dim ShareIt As Intent
			copy.clrText
			copy.setText("#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://bit.ly/2mqSEWy")
			ShareIt.Initialize (ShareIt.ACTION_SEND,"")
			ShareIt.SetType ("text/plain")
			ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
			ShareIt.PutExtra ("android.intent.extra.SUBJECT","Get Free!!")
			ShareIt.WrapAsIntentChooser("Share App Via...")
			StartActivity (ShareIt)
		Case 8 :
			Dim p As PhoneIntents
			StartActivity(p.OpenBrowser("http://www.htetznaing.com"))
		Case 9 :
			StartActivity(About)
	End Select
End Sub

Sub menu_Click
	If sm.isVisible Then sm.Hide Else sm.Show
End Sub

Sub share_Click
	Dim ShareIt As Intent
	copy.clrText
	copy.setText("#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://www.htetznaing.com/search?q=Myanmar+Nayon+Font")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","Get Free!!")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub