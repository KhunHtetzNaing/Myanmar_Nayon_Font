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
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim lb As Label
	Dim mm As Typeface : mm = mm.LoadFromAssets("Nayon.ttf")
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	lb.Initialize("lb")
	lb.Color = Colors.White
	lb.Gravity = Gravity.CENTER
	lb.Typeface = mm
	lb.TextSize = 25
	lb.TextColor = Colors.Magenta
	Activity.AddView(lb,0%x,0%y,100%x,100%y)
	lb.Visible = True
	lb.Text = "File Manager တစ္ခုခုထဲသို႔သြားၿပီး Vivo Myanmar Font ဖိုဒါထဲကိုဝင္ပါ။ Nayon.txj (သို့) Nayon.itz ကိုဖြင့္ၿပီး Apply လုပ္ေပးလိုက္ပါ သို႔မဟုတ္ Change Font2 နဲ႔ထပ္သြင္းပါ။"
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub
