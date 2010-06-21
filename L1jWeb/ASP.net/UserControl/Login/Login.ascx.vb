Imports System.Data
Imports MySql.Data.MySqlClient
Imports System.Security.Cryptography

Partial Class UserControl_Login_Login
    Inherits System.Web.UI.UserControl

    Private _db As New SQLMethod()

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Not IsPostBack Then

        End If

        If Session("LoginUser") Is Nothing Then
            mvLogin.ActiveViewIndex = 0
        Else
            mvLogin.ActiveViewIndex = 1

            '顯示登入訊息
            Dim LoginUser As LoginUser = DirectCast(Session("LoginUser"), LoginUser)
            DisplayUserInfo(LoginUser)
        End If

        Page.Form.DefaultButton = LoginButton.UniqueID
    End Sub

    Protected Sub LoginButton_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles LoginButton.Click
        Dim _password_salt As String = ConfigurationManager.AppSettings.Get("PASSWORD_SALT")
        Dim _account As String = txtAccount.Text.Trim()
        Dim _passWord As String = txtPassword.Text.Trim()
        Dim _hash_account As String = Hash.Hash.GetHash(_account, Hash.Hash.HashType.MD5)
        Dim _hash_password As String = Hash.Hash.GetHash(_password_salt & _passWord & _hash_account, Hash.Hash.HashType.SHA256)

        Dim strSQL As New StringBuilder
        strSQL.Append("SELECT A.*, B.email ")
        strSQL.Append("FROM web_accounts AS A ")
        strSQL.Append("LEFT JOIN web_personal_info AS B ON A.login = B.login ")
        strSQL.Append("WHERE A.login=@login AND password=@password ")

        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@login", MySqlDbType.VarChar, 50).Value = _account
        cmd.Parameters.Add("@password", MySqlDbType.VarChar, 100).Value = _hash_password
        Dim user As DataTable = _db.GetDataTable(cmd, "web_accounts")

        If user IsNot Nothing Then
            If user.Rows.Count > 0 Then
                '登入成功
                lblMsg.Text = String.Empty
                Dim LoginUser As New LoginUser
                LoginUser.Account = user.Rows(0)("login").ToString()
                LoginUser.Access_Level = CInt(user.Rows(0)("access_level").ToString())
                LoginUser.Banned = user.Rows(0)("banned").ToString()
                'LoginUser.Character_Slot = user.Rows(0)("character_slot").ToString()
                LoginUser.Host = Page.Request.UserHostName
                LoginUser.IP = Page.Request.UserHostAddress
                LoginUser.Email = user.Rows(0)("email").ToString()
                Session("LoginUser") = LoginUser

                '切換登入顯示MutiView
                mvLogin.ActiveViewIndex = 1
                DisplayUserInfo(LoginUser)
                Response.Redirect(FormsAuthentication.DefaultUrl)
            Else
                lblMsg.Text = "帳號或密碼輸入錯誤！"
                '切換登出顯示MutiView
                mvLogin.ActiveViewIndex = 0
            End If
        Else
            lblMsg.Text = "帳號或密碼輸入錯誤！"
            '切換登出顯示MutiView
            mvLogin.ActiveViewIndex = 0
        End If
    End Sub

    Protected Sub lbforgotpassword_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles lbforgotpassword.Click
        Response.Redirect("~/ForgotPWD.aspx")
    End Sub

    Protected Sub btnLogout_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles btnLogout.Click
        Session.RemoveAll()
        mvLogin.ActiveViewIndex = 0
        Response.Redirect(FormsAuthentication.DefaultUrl)
    End Sub

    Private Sub DisplayUserInfo(ByVal LoginUser As LoginUser)
        lblAccount.Text = LoginUser.Account
        lblIP.Text = LoginUser.IP

        Dim _appfunc As New AppFunction
        Dim _dt As DataTable = _appfunc.GetGameAccount(LoginUser.Account)

        lbGameAccount.Text = "遊戲帳號(" & _dt.Rows.Count & ")"
    End Sub

End Class
