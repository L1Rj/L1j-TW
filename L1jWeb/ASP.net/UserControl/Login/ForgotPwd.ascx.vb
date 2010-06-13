Imports MySql.Data.MySqlClient
Imports System.Net.Mail
Imports System.Web.Configuration
Imports System.Net.Configuration

Partial Class UserControl_Login_ForgotPwd
    Inherits System.Web.UI.UserControl

    Private _db As New SQLMethod()

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Not IsPostBack Then
            
        End If
    End Sub

    Protected Sub SubmitButton_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles SubmitButton.Click
        Dim strSQL As String = String.Empty
        Dim cmd As New MySqlCommand
        strSQL = "SELECT question FROM web_personal_info WHERE login=@login"
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = txtAccount.Text.Trim()
        Dim _question As String = _db.GetFirstValue(cmd)

        If _question = Nothing Or _db.ErrMsg <> String.Empty Then
            pnlQandA.Visible = False
            FailureText.Text = "您並沒有設定提示問題！請洽系統管理員設定提示問題"
            If _db.ErrMsg <> String.Empty Then FailureText.Text += "<br />" & _db.ErrMsg
        Else
            pnlQandA.Visible = True
            txtAccount.Enabled = False
            SubmitButton.Enabled = False
            lblQuestion.Text = _question
        End If
    End Sub

    Protected Sub bntSendEMail_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles bntSendEMail.Click
        Dim strSQL As String = String.Empty
        Dim cmd As New MySqlCommand
        strSQL = "SELECT answer FROM web_personal_info WHERE login=@login AND question=@question"
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = txtAccount.Text.Trim()
        cmd.Parameters.Add("@question", MySqlDbType.VarChar).Value = lblQuestion.Text
        Dim _answer As String = _db.GetFirstValue(cmd)

        If _answer = txtAnswer.Text Then
            '變更使用者密碼
            Dim _newpassword As String = ChangePwd(txtAccount.Text.Trim())
            'Send E-Mail
            SendEmail(txtAccount.Text.Trim(), _newpassword)
        Else
            lblFailure.Text = "回答錯誤！"
        End If
    End Sub

    Private Function ChangePwd(ByVal Account As String) As String
        '隨機取得密碼
        Dim _newpassword As String = NewPassWord()

        '密碼編碼
        Dim _password_salt As String = ConfigurationManager.AppSettings.Get("PASSWORD_SALT")
        Dim _hash_account As String = Hash.Hash.GetHash(Account, Hash.Hash.HashType.MD5)
        Dim _hash_password As String = Hash.Hash.GetHash(_password_salt & _newpassword & _hash_account, Hash.Hash.HashType.SHA256)

        Dim strSQL As String = "UPDATE web_accounts SET password=@password WHERE login=@login "
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@password", MySqlDbType.VarChar).Value = _hash_password
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = Account
        _db.ExecSQL(cmd)

        Return _newpassword
    End Function

    Private Sub SendEmail(ByVal Account As String, ByVal NewPwd As String)
        Try
            Dim Email As New EMail()

            Dim _from As List(Of MailAddress) = GetEMailAddr(ConfigurationManager.AppSettings.Get("AdminMail"))
            If _from.Count > 0 Then
                Email.FromMail = _from.Item(0)
            Else
                Response.Write("無寄件者")
                Exit Sub
            End If

            '取得使用者email
            Dim strSQL As String = "SELECT email FROM web_personal_info WHERE login=@login"
            Dim cmd As New MySqlCommand()
            cmd.CommandText = strSQL
            cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = Account
            Dim _useremail As String = _db.GetFirstValue(cmd)

            Dim _to As List(Of MailAddress) = GetEMailAddr(_useremail)
            Email.ToMail = _to

            'Dim CC As List(Of MailAddress) = GetEMailAddr(txtCC.Text)
            'Email.cc = CC

            'Dim BCC As List(Of MailAddress) = GetEMailAddr(txtBCC.Text)
            'Email.bcc = BCC

            Email.Subject = ConfigurationManager.AppSettings.Get("WebName") & "-密碼通知"
            Email.Body = ShowBody(Account, NewPwd)
            Email.isBodyHtml = True

            Dim result As String = Email.SendMail()
            If result = "Success" Then
                Response.Write("您新的密碼已成功發送於您的信箱！")
            End If
        Catch ex As Exception
            Response.Write(ex.Message)
        End Try
    End Sub

    Private Function GetEMailAddr(ByVal MAddrs As String) As List(Of MailAddress)
        Try

            Dim RcMail As List(Of MailAddress) = Nothing
            Dim tMAddr As String = ""
            Dim tMName As String = ""
            Dim y As Integer
            Dim tMail As MailAddress

            If MAddrs <> "" Then
                '如果傳入有資料 
                RcMail = New List(Of MailAddress)
                If InStr(1, MAddrs, ";", CompareMethod.Text) = 0 Then
                    '判斷只有一組Mail 
                    If InStr(1, MAddrs, "(", CompareMethod.Text) > 0 Then
                        '有Mail_Name 
                        tMAddr = MAddrs.Split("(")(0)   'Mail_Addr 
                        tMName = Replace(MAddrs.Split("(")(1), ")", "") 'MailName 
                    Else
                        tMAddr = MAddrs 'Mail_Addr 

                    End If
                    tMail = New MailAddress(tMAddr, tMName)
                    RcMail.Add(tMail)
                Else
                    '多組Mail處理 
                    Dim sMAddrs() As String '每組Mail含名字處理 
                    sMAddrs = MAddrs.Split(";")
                    For y = 0 To sMAddrs.Length - 1
                        '判斷是否有名稱 
                        If InStr(1, sMAddrs(y), "(", CompareMethod.Text) > 0 Then
                            '有名稱處理 
                            tMAddr = sMAddrs(y).Split("(")(0)
                            tMName = Replace(sMAddrs(y).Split("(")(1), ")", "")
                        Else
                            '無名稱處理 
                            tMAddr = sMAddrs(y)
                            tMName = ""
                        End If
                        tMail = New MailAddress(tMAddr, tMName)
                        RcMail.Add(tMail)
                    Next
                End If
            End If

            Return RcMail
        Catch ex As Exception
            Throw New Exception(ex.Message)
        End Try
    End Function

    Private Function ShowBody(ByVal Account As String, ByVal NewPwd As String) As String
        Dim _webip As String = ConfigurationManager.AppSettings.Get("WebIP")
        Dim _webname As String = ConfigurationManager.AppSettings.Get("WebName")
        Dim tmpBody As New StringBuilder()
        tmpBody.Append(Account + "  您好！<br />")
        tmpBody.Append("您的密碼已重新設定，新的密碼為：<font color=blue>" & NewPwd & "</font>！<br />")
        tmpBody.Append("可至下列網址登入並修改您的密碼！<br />")
        tmpBody.Append("<a href=""http:// " & _webip & """>" & _webname & "</a>")
        Return tmpBody.ToString()
    End Function

    Private Function NewPassWord() As String
        Dim RNG As New RNG()
        Dim sb As New System.Text.StringBuilder()
        Dim chars As Char() = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".ToCharArray()
        Dim length As Integer = RNG.[Next](4, 4)
        For i As Integer = 0 To length - 1
            sb.Append(chars(RNG.[Next](chars.Length - 1)))
        Next

        Return sb.ToString()
    End Function
End Class
