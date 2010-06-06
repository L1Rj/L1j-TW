Imports MySql.Data.MySqlClient

Partial Class UserControl_Login_ModifyPersonalData
    Inherits System.Web.UI.UserControl

    Private _db As SQLMethod
    Private _app As AppFunction

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Not IsPostBack Then
            Dim LoginUser As LoginUser = DirectCast(Session("LoginUser"), LoginUser)
            lblAccount.Text = LoginUser.Account
            txtEMail.Text = LoginUser.Email
        End If

        Page.Form.DefaultButton = btnModify.UniqueID
    End Sub

    Protected Sub btnModify_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles btnModify.Click
        _db = New SQLMethod
        _app = New AppFunction
        Dim strSQL As String
        Dim cmd As MySqlCommand

        If txtOldPwd.Text.Trim() <> String.Empty And txtNewPwd.Text.Trim() <> String.Empty And txtCNewPwd.Text.Trim() <> String.Empty Then
            Dim _hash_password As String = _app.GetHashPassword(lblAccount.Text, txtOldPwd.Text)
            strSQL = "SELECT password FROM web_accounts WHERE login=@login "
            cmd = New MySqlCommand()
            cmd.CommandText = strSQL
            cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = lblAccount.Text
            Dim _db_password As String = _db.GetFirstValue(cmd)

            lblMsg.Visible = True
            If _db_password <> _hash_password Then
                lblMsg.ForeColor = Drawing.Color.Red
                lblMsg.Text = "您輸入的密碼不符，請確認後重新輸入！"
                Return
            ElseIf lblAccount.Text.Trim() = txtNewPwd.Text.Trim() Then
                lblMsg.ForeColor = Drawing.Color.Red
                lblMsg.Text = "您輸入的密碼不可與帳號相同，請重新輸入！"
                Return
            Else
                Dim _new_hash_password As String = _app.GetHashPassword(lblAccount.Text, txtNewPwd.Text)
                strSQL = "UPDATE web_accounts SET password=@password WHERE login=@login "
                cmd = New MySqlCommand()
                cmd.CommandText = strSQL
                cmd.Parameters.Add("@password", MySqlDbType.VarChar).Value = _new_hash_password
                cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = lblAccount.Text
                _db.ExecSQL(cmd)

                If _db.ErrMsg = String.Empty Then
                    lblMsg.ForeColor = Drawing.Color.Blue
                    lblMsg.Text = "更新完成"
                Else
                    lblMsg.ForeColor = Drawing.Color.Red
                    lblMsg.Text = _db.ErrMsg
                    Return
                End If
            End If
        End If

        If txtEMail.Text.Trim() <> String.Empty Then
            strSQL = "UPDATE web_personal_info SET email=@email WHERE login=@login "
            cmd = New MySqlCommand()
            cmd.CommandText = strSQL
            cmd.Parameters.Add("@email", MySqlDbType.VarChar).Value = txtEMail.Text.Trim()
            cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = lblAccount.Text
            _db.ExecSQL(cmd)

            lblMsg.Visible = True
            If _db.ErrMsg = String.Empty Then
                lblMsg.ForeColor = Drawing.Color.Blue
                lblMsg.Text = "更新完成"
            Else
                lblMsg.ForeColor = Drawing.Color.Red
                lblMsg.Text = _db.ErrMsg
                Return
            End If
        End If

    End Sub
End Class
