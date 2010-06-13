Imports Microsoft.VisualBasic
Imports MySql.Data.MySqlClient
Imports System.Data

Public Class GameAccount

    Private _db As SQLMethod

    Private _account As String
    Private _access_level As Integer
    Private _ip As String
    Private _host As String
    Private _banned As Integer
    Private _character_slot As Integer

    Private strErrMsg As String

    Public Sub New()

    End Sub

    Public Property Account() As String
        Get
            Return _account
        End Get
        Set(ByVal value As String)
            _account = value
        End Set
    End Property

    Public Property Access_Level() As Integer
        Get
            Return _access_level
        End Get
        Set(ByVal value As Integer)
            _access_level = value
        End Set
    End Property

    Public Property IP() As String
        Get
            Return _ip
        End Get
        Set(ByVal value As String)
            _ip = value
        End Set
    End Property

    Public Property Host() As String
        Get
            Return _host
        End Get
        Set(ByVal value As String)
            _host = value
        End Set
    End Property

    Public Property Banned() As Integer
        Get
            Return _banned
        End Get
        Set(ByVal value As Integer)
            _banned = value
        End Set
    End Property

    Public Property Character_Slot() As Integer
        Get
            Return _character_slot
        End Get
        Set(ByVal value As Integer)
            _character_slot = value
        End Set
    End Property

    Public Property ErrMsg() As String
        Get
            Return strErrMsg
        End Get
        Set(ByVal value As String)
            strErrMsg = value
        End Set
    End Property

    Public Function CreateGameAccount(ByVal WebAccount As String, ByVal GameAccount As String, ByVal GamePassword As String, ByVal ip As String, ByVal host As String) As GameAccount
        strErrMsg = String.Empty
        _db = New SQLMethod()
        Dim tmpGameAccount As New GameAccount
        Try
            Dim result As String = CheckAccountInfo(WebAccount, GameAccount, GamePassword)
            If result = String.Empty Then
                Dim _password_salt As String = ConfigurationManager.AppSettings.Get("PASSWORD_SALT")
                Dim _account As String = GameAccount
                Dim _passWord As String = GamePassword
                Dim _hash_account As String = Hash.Hash.GetHash(_account, Hash.Hash.HashType.MD5)
                Dim _hash_password As String = Hash.Hash.GetHash(_password_salt & _passWord & _hash_account, Hash.Hash.HashType.SHA256)

                '新增Game_Account
                Dim strSQL As New StringBuilder()
                Dim CollCmd As New List(Of MySqlCommand)
                Dim cmd As New MySqlCommand()
                strSQL.Append("INSERT INTO accounts ")
                strSQL.Append("(login, password, lastactive, access_level, ip, host, banned, character_slot) VALUES ")
                strSQL.Append("(@login, @password, NOW(), @access_level, @ip, @host, @banned, @character_slot) ")
                cmd.CommandText = strSQL.ToString()
                cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = _account
                cmd.Parameters.Add("@password", MySqlDbType.VarChar).Value = _hash_password
                cmd.Parameters.Add("@access_level", MySqlDbType.Int32).Value = 0
                cmd.Parameters.Add("@ip", MySqlDbType.VarChar).Value = ip
                cmd.Parameters.Add("@host", MySqlDbType.VarChar).Value = host
                cmd.Parameters.Add("@banned", MySqlDbType.Int32).Value = 0
                cmd.Parameters.Add("@character_slot", MySqlDbType.Int32).Value = 0
                CollCmd.Add(cmd)

                '新增Web_Account & Game_Account 對照表
                strSQL = New StringBuilder()
                cmd = New MySqlCommand()
                strSQL.Append("INSERT INTO web_accounts_relation ")
                strSQL.Append("(web_accounts_login, accounts_login) VALUES ")
                strSQL.Append("(@web_accounts_login, @accounts_login) ")
                cmd.CommandText = strSQL.ToString()
                cmd.Parameters.Add("@web_accounts_login", MySqlDbType.VarChar).Value = WebAccount
                cmd.Parameters.Add("@accounts_login", MySqlDbType.VarChar).Value = _account
                CollCmd.Add(cmd)

                _db.ExecSQL(CollCmd)

                tmpGameAccount.Account = GameAccount
                tmpGameAccount.Access_Level = 0
                tmpGameAccount.IP = ip
                tmpGameAccount.Host = host
                tmpGameAccount.Banned = 0
                tmpGameAccount.Character_Slot = 0
            Else
                strErrMsg = result
            End If
        Catch ex As Exception
            strErrMsg = ex.Message
        End Try

        Return tmpGameAccount
    End Function

    Public Function GetGameAccountInfo(ByVal GameAccount As String) As GameAccount
        Dim tmpGameAccount As New GameAccount
        _db = New SQLMethod
        Dim strSQL As String = "SELECT * FROM accounts WHERE login=@login "
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = GameAccount
        Dim tmpDT As DataTable = _db.GetDataTable(cmd, "login")

        tmpGameAccount.Account = tmpDT.Rows(0)("Account").ToString()
        tmpGameAccount.Access_Level = CInt(tmpDT.Rows(0)("Access_Level"))
        tmpGameAccount.IP = tmpDT.Rows(0)("IP").ToString()
        tmpGameAccount.Host = tmpDT.Rows(0)("Host").ToString()
        tmpGameAccount.Banned = CInt(tmpDT.Rows(0)("Banned"))
        tmpGameAccount.Character_Slot = CInt(tmpDT.Rows(0)("Character_Slot"))

        Return tmpGameAccount
    End Function

    Public Function GetGameAccountDT(ByVal GameAccount As String) As DataTable
        _db = New SQLMethod
        Dim strSQL As String = "SELECT * FROM accounts WHERE login=@login "
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = GameAccount
        Dim tmpDT As DataTable = _db.GetDataTable(cmd, "login")

        Return tmpDT
    End Function

    Protected Function CheckAccountInfo(ByVal WebAccount As String, ByVal GameAccount As String, ByVal GamePassword As String) As String
        Dim result As String = String.Empty
        Dim strSQL As StringBuilder = New StringBuilder()
        Dim cmd As New MySqlCommand()

        '檢查帳號是否跟密碼一樣
        If GameAccount = GamePassword Then
            result = "帳號不可與密碼相同"
            Return result
        End If

        '檢查是否有重複帳號
        strSQL.Append("SELECT login FROM accounts WHERE login=@login ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@login", MySqlDbType.VarChar).Value = GameAccount
        result = _db.GetFirstValue(cmd)
        If result <> String.Empty Then
            result = "帳號已被註冊，請輸入不同的帳號。"
            Return result
        End If

        '檢查帳號、密碼長度是否正確
        Dim _account_length As Integer = CInt(ConfigurationManager.AppSettings.Get("ACCOUNT_LENGTH"))
        Dim _password_length As Integer = CInt(ConfigurationManager.AppSettings.Get("PASSWORD_LENGTH"))
        Dim _tmpAccount As String = GameAccount
        Dim _tmpPWD As String = GamePassword
        If _tmpAccount.Trim().Length < _account_length Then
            result = "帳號長度必須 >= " & _account_length & " 碼！"
            Return result
        End If

        If _tmpPWD.Trim().Length < _password_length Then
            result = "密碼長度必須 >= " & _password_length & " 碼！"
            Return result
        End If

        '檢查申請帳號是否已超過3組
        Dim _appfunc As New AppFunction
        Dim _dt As DataTable = _appfunc.GetGameAccount(WebAccount)
        If _dt.Rows.Count >= 3 Then
            result = "您的遊戲帳號已達3組無法再新增！"
            Return result
        End If

        Return result
    End Function

End Class
