Imports MySql.Data.MySqlClient

Partial Class UserControl_Login_Register
    Inherits System.Web.UI.UserControl

    Private _db As New SQLMethod()

    Protected Sub lbtnCreateUser_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles lbtnCreateUser.Click
        If Not TBValidateCode1.ValidateCode(txtCheckCode.Text) Then
            lblMsg.Visible = True
            lblMsg.ForeColor = System.Drawing.Color.Red
            lblMsg.Text = "驗證碼錯誤!"
            Exit Sub
        End If

        Try
            '檢查輸入欄位
            Dim result As String = String.Empty
            result = CheckValue()
            If result <> String.Empty Then
                lblMsg.Visible = True
                lblMsg.ForeColor = System.Drawing.Color.Red
                lblMsg.Text = result
                txtCheckCode.Text = String.Empty
                Return
            End If

            '建立帳號
            Dim _password_salt As String = ConfigurationManager.AppSettings.Get("PASSWORD_SALT")
            Dim _account As String = txtAccount.Text.Trim()
            Dim _passWord As String = txtPassword.Text.Trim()
            Dim _hash_account As String = Hash.Hash.GetHash(_account, Hash.Hash.HashType.MD5)
            Dim _hash_password As String = Hash.Hash.GetHash(_password_salt & _passWord & _hash_account, Hash.Hash.HashType.SHA256)
            Dim strSQL As New StringBuilder()
            Dim CollCmd As New List(Of MySqlCommand)
            Dim cmd As New MySqlCommand()
            strSQL.Append("INSERT INTO web_accounts ")
            strSQL.Append("(login, password, lastactive, access_level, ip, host, banned) VALUES ")
            strSQL.Append("(@login, @password, NOW(), @access_level, @ip, @host, @banned) ")
            cmd.CommandText = strSQL.ToString()
            cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = _account
            cmd.Parameters.Add("@password", MySqlDbType.VarChar).Value = _hash_password
            cmd.Parameters.Add("@access_level", MySqlDbType.Int32).Value = 0
            cmd.Parameters.Add("@ip", MySqlDbType.VarChar).Value = Page.Request.UserHostAddress()
            cmd.Parameters.Add("@host", MySqlDbType.VarChar).Value = Page.Request.UserHostName()
            cmd.Parameters.Add("@banned", MySqlDbType.Int32).Value = 0
            'cmd.Parameters.Add("@character_slot", MySqlDbType.Int32).Value = 0
            CollCmd.Add(cmd)

            strSQL = New StringBuilder()
            cmd = New MySqlCommand()
            strSQL.Append("INSERT INTO web_personal_info ")
            strSQL.Append("(login, email, question, answer) VALUES ")
            strSQL.Append("(@login, @email, @question, @answer) ")
            cmd.CommandText = strSQL.ToString()
            cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = _account
            cmd.Parameters.Add("@email", MySqlDbType.VarChar).Value = txtEmail.Text.Trim()
            cmd.Parameters.Add("@question", MySqlDbType.VarChar).Value = txtQuestion.Text.Trim()
            cmd.Parameters.Add("@answer", MySqlDbType.VarChar).Value = txtAnswer.Text.Trim()
            CollCmd.Add(cmd)

            _db.ExecSQL(CollCmd)

            '開起訊息
            If _db.ErrMsg = String.Empty Then
                lblMsg.Visible = True
                lblMsg.ForeColor = System.Drawing.Color.Blue
                lblMsg.Text = "帳號建立成功<br /><a href=""" & System.Web.Security.FormsAuthentication.DefaultUrl & """ >回首頁</a>"
            Else
                lblMsg.Visible = True
                lblMsg.ForeColor = System.Drawing.Color.Red
                lblMsg.Text = "帳號建立失敗，請聯絡系統管理員<br /><a href=""" & System.Web.Security.FormsAuthentication.DefaultUrl & """ >回首頁</a>"
            End If
        Catch ex As Exception
            lblMsg.Visible = True
            lblMsg.ForeColor = System.Drawing.Color.Red
            lblMsg.Text = ex.Message()
        End Try
    End Sub

    Protected Function CheckValue() As String
        Dim result As String = String.Empty
        Dim strSQL As StringBuilder = New StringBuilder()
        Dim cmd As New MySqlCommand()

        '檢查帳號是否跟密碼一樣
        If txtAccount.Text.Trim() = txtPassword.Text.Trim() Then
            result = "帳號不可與密碼相同"
            Return result
        End If

        '檢查是否有重複帳號
        strSQL.Append("SELECT login FROM web_accounts WHERE login=@login ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = txtAccount.Text.Trim()
        result = _db.GetFirstValue(cmd)
        If result <> String.Empty Then
            result = "帳號已被註冊，請輸入不同的帳號。"
            Return result
        End If

        '檢查是否有重複E-Mail
        strSQL = New StringBuilder()
        cmd = New MySqlCommand()
        strSQL.Append("SELECT email FROM web_accounts WHERE email=@email ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@email", MySqlDbType.VarChar).Value = txtEmail.Text.Trim()
        result = _db.GetFirstValue(cmd)
        If result <> String.Empty Then
            result = txtEmail.Text & " 已經被註冊！請重新更換。"
            Return result
        End If

        '檢查帳號、密碼長度是否正確
        Dim _account_length As Integer = CInt(ConfigurationManager.AppSettings.Get("ACCOUNT_LENGTH"))
        Dim _password_length As Integer = CInt(ConfigurationManager.AppSettings.Get("PASSWORD_LENGTH"))
        Dim _tmpAccount As String = txtAccount.Text.Trim()
        Dim _tmpPWD As String = txtPassword.Text.Trim()
        If _tmpAccount.Trim().Length < _account_length Then
            result = "帳號長度必須大於 " & _account_length & " 碼！"
            Return result
        End If

        If _tmpPWD.Trim().Length < _password_length Then
            result = "密碼長度必須大於 " & _password_length & " 碼！"
            Return result
        End If

        Return result
    End Function
End Class
