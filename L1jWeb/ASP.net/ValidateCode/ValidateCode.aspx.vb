Imports System
Imports System.IO
Imports System.Drawing
Imports System.Drawing.Imaging
Imports System.Text
Imports System.Collections
Imports System.Web
Imports System.Web.UI
Imports System.Web.UI.WebControls

Partial Class ValidateCode
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        Dim sCode As String = String.Empty

        '清除該頁輸出緩存，設置該頁無緩存
        Response.Buffer = True
        Response.ExpiresAbsolute = System.DateTime.Now.AddMilliseconds(0)
        Response.Expires = 0
        Response.CacheControl = "no-cache"
        Response.AppendHeader("Pragma", "No-Cache")
        '將驗證碼圖片寫入記憶體流，並將其以 "image/Png" 格式輸出
        Dim oStream As New MemoryStream()
        Try
            CreateValidateCodeImage(oStream, sCode, 5, 125, 40, 18)
            Me.Session("_ValidateCode") = sCode
            Response.ClearContent()
            Response.ContentType = "image/Png"
            Response.BinaryWrite(oStream.ToArray())
        Finally
            '釋放資源
            oStream.Dispose()
        End Try
    End Sub

    ''' <summary>
    ''' 產生圖形驗證碼。
    ''' </summary>
    ''' <param name="MemoryStream">記憶體資料流。</param>
    ''' <param name="Code">傳出驗證碼。</param>
    ''' <param name="CodeLength">驗證碼字元數。</param> 
    Public Sub CreateValidateCodeImage(ByRef MemoryStream As MemoryStream, _
        ByRef Code As String, ByVal CodeLength As Integer, _
        ByVal Width As Integer, ByVal Height As Integer, ByVal FontSize As Integer)
        Dim oBmp As Bitmap

        oBmp = CreateValidateCodeImage(Code, CodeLength, Width, Height, FontSize)
        Try
            oBmp.Save(MemoryStream, ImageFormat.Png)
        Finally
            oBmp.Dispose()
        End Try
    End Sub

    ''' <summary>
    ''' 產生圖形驗證碼。
    ''' </summary>
    ''' <param name="Code">傳出驗證碼。</param>
    ''' <param name="CodeLength">驗證碼字元數。</param> 
    Public Function CreateValidateCodeImage(ByRef Code As String, ByVal CodeLength As Integer, _
        ByVal Width As Integer, ByVal Height As Integer, ByVal FontSize As Integer) As Bitmap
        Dim sCode As String = String.Empty
        '顏色列表，用於驗證碼、噪線、噪點
        Dim oColors As Color() = { _
            Drawing.Color.Black, Drawing.Color.Red, Drawing.Color.Blue, Drawing.Color.Green, _
            Drawing.Color.Orange, Drawing.Color.Brown, Drawing.Color.Brown, Drawing.Color.DarkBlue}
        '字體列表，用於驗證碼
        Dim oFontNames As String() = {"Times New Roman", "MS Mincho", "Book Antiqua", _
                                      "Gungsuh", "PMingLiU", "Impact"}
        '驗證碼的字元集，去掉了一些容易混淆的字元
        Dim oCharacter As Char() = {"2"c, "3"c, "4"c, "5"c, "6"c, "8"c, _
                                    "9"c, "A"c, "B"c, "C"c, "D"c, "E"c, _
                                    "F"c, "G"c, "H"c, "J"c, "K"c, "L"c, _
                                    "M"c, "N"c, "P"c, "R"c, "S"c, "T"c, _
                                    "W"c, "X"c, "Y"c}
        Dim oRnd As New Random()
        Dim oBmp As Bitmap
        Dim oGraphics As Graphics
        Dim N1 As Integer
        Dim oPoint1 As Drawing.Point
        Dim oPoint2 As Drawing.Point
        Dim sFontName As String
        Dim oFont As Font
        Dim oColor As Color

        '生成驗證碼字串
        For N1 = 0 To CodeLength - 1
            sCode += oCharacter(oRnd.Next(oCharacter.Length))
        Next

        oBmp = New Bitmap(Width, Height)
        oGraphics = Graphics.FromImage(oBmp)
        oGraphics.Clear(Drawing.Color.White)
        Try
            For N1 = 0 To 4
                '畫噪線
                oPoint1.X = oRnd.Next(Width)
                oPoint1.Y = oRnd.Next(Height)
                oPoint2.X = oRnd.Next(Width)
                oPoint2.Y = oRnd.Next(Height)
                oColor = oColors(oRnd.Next(oColors.Length))
                oGraphics.DrawLine(New Pen(oColor), oPoint1, oPoint2)
            Next

            For N1 = 0 To sCode.Length - 1
                '畫驗證碼字串
                sFontName = oFontNames(oRnd.Next(oFontNames.Length))
                oFont = New Font(sFontName, FontSize, FontStyle.Italic)
                oColor = oColors(oRnd.Next(oColors.Length))
                oGraphics.DrawString(sCode(N1).ToString(), oFont, New SolidBrush(oColor), CSng(N1) * FontSize + 10, CSng(8))
            Next

            For i As Integer = 0 To 30
                '畫噪點
                Dim x As Integer = oRnd.Next(oBmp.Width)
                Dim y As Integer = oRnd.Next(oBmp.Height)
                Dim clr As Color = oColors(oRnd.Next(oColors.Length))
                oBmp.SetPixel(x, y, clr)
            Next

            Code = sCode
            Return oBmp
        Finally
            oGraphics.Dispose()
        End Try
    End Function
End Class
