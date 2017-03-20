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
End Sub

Sub Globals
	Dim ph As Phone
	Dim b1,b2,b3,b4,b5,b6 As Label
	Dim Banner As AdView
	Dim Interstitial As InterstitialAd

	Dim sm As SlideMenu
	Dim tlb As Label
	Dim menu,share As Button
	Dim sbg,mbg As BitmapDrawable
	Dim copy As BClipboard
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.Color = Colors.White
	ph.SetScreenOrientation(1)

	b1.Initialize("b1")
	Dim b1bg As ColorDrawable
	b1bg.Initialize(Colors.Black,10)
	b1.Text = "Samsung"
	b1.Background = b1bg
	b1.Gravity = Gravity.CENTER
	b1.Textcolor = Colors.White
	b1.TextSize = 17
	Activity.AddView(b1,20%x,55dip+10%y,60%x,50dip)

	b2.Initialize("b2")
	b2.Background = b1bg
	b2.Text = "Oppo"
	b2.Gravity = Gravity.CENTER
	b2.Textcolor = Colors.White
	b2.TextSize = 17
	Activity.AddView(b2,20%x,(b1.Top+b1.Height)+2%y,60%x,50dip)

	b3.Initialize("b3")
	b3.Text = "Vivo"
	b3.Background = b1bg
	b3.Gravity = Gravity.CENTER
	b3.Textcolor = Colors.White
	b3.TextSize = 17
	Activity.AddView(b3,20%x,(b2.Top+b2.Height)+2%y,60%x,50dip)

	b4.Initialize("b4")
	b4.Text = "Huawei"
	b4.Background = b1bg
	b4.Gravity = Gravity.CENTER
	b4.Textcolor = Colors.White
	b4.TextSize = 17
	Activity.AddView(b4,20%x,(b3.Top+b3.Height)+2%y,60%x,50dip)

	b5.Initialize("b5")
	b5.Text = "Xiaomi"
	b5.Background = b1bg
	b5.Gravity = Gravity.CENTER
	b5.Textcolor = Colors.White
	b5.TextSize = 17
	Activity.AddView(b5,20%x,(b4.Top+b4.Height)+2%y,60%x,50dip)

	b6.Initialize("b6")
	b6.Text = "Other [#Root]"
	b6.Background = b1bg
	b6.Gravity = Gravity.CENTER
	b6.Textcolor = Colors.White
	b6.TextSize = 17
	Activity.AddView(b6,20%x,(b5.Top+b5.Height)+2%y,60%x,50dip)
	
	'NEWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
	tlb.Initialize("tlb")
	tlb.Color = Colors.rgb(76, 175, 80)
	
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
End Sub

Sub b1_Click
	StartActivity(Samsung)
End Sub

Sub b2_Click
	StartActivity(Oppo)
End Sub

Sub b3_Click
	StartActivity(Vivo)
End Sub

Sub b4_Click
	StartActivity(Huawei)
End Sub

Sub b5_Click
	StartActivity(Xiaomi)
End Sub

Sub b6_Click
	StartActivity(Other)
End Sub

Sub Activity_Resume

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

End Sub


Sub SlideMenu_Click(Item As Object)
	sm.Hide
	Select Item
		Case 1 :
			StartActivity(Samsung)
		Case 2 :
			StartActivity(Oppo)
		Case 3 :
			StartActivity(Vivo)
		Case 4 :
			StartActivity(Huawei)
		Case 5 :
			StartActivity(Xiaomi)
		Case 6 :
			StartActivity(Other)
		Case 7 :
			Dim ShareIt As Intent
			copy.clrText
			copy.setText("#Myanmar_Nayon_Font App! Beautiful Myanmar Zawgyi Font Style!	You can Use in Samung, Oppo,Vivo, Huawei (EMUI) and Xiaomi (MIUI) without Root Access!!!! Download Free at : http://www.htetznaing.com/search?q=Myanmar+Nayon+Font")
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