Imports System.Data
Imports Microsoft.VisualBasic
Imports MySql.Data.MySqlClient
Imports System.Net.Sockets
Imports System.Net
Imports System.IO

Public Class AppFunction

    Private _db As SQLMethod

    Public Function GetHashPassword(ByVal Account As String, ByVal Password As String) As String
        Dim _password_salt As String = ConfigurationManager.AppSettings.Get("PASSWORD_SALT")
        Dim _account As String = Account
        Dim _passWord As String = Password
        Dim _hash_account As String = Hash.Hash.GetHash(_account, Hash.Hash.HashType.MD5)
        Dim _hash_password As String = Hash.Hash.GetHash(_password_salt & _passWord & _hash_account, Hash.Hash.HashType.SHA256)

        Return _hash_password
    End Function

    Public Function GetGameAccount(ByVal Account As String) As DataTable
        Dim _dt As DataTable
        _db = New SQLMethod
        Dim strSQL As String = "SELECT * FROM web_accounts_relation WHERE web_accounts_login=@web_accounts_login"
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@web_accounts_login", MySqlDbType.VarChar).Value = Account
        _dt = _db.GetDataTable(cmd, "web_accounts_relation")

        Return _dt
    End Function

    Public Function IsServerConnectable(ByVal host As String, ByVal port As Integer, ByVal timeOut As Double) As Boolean
        Dim tcp As New TcpClient()
        Dim t As DateTime = DateTime.Now

        Try
            Dim ar As IAsyncResult = tcp.BeginConnect(host, port, Nothing, Nothing)
            While Not ar.IsCompleted
                If DateTime.Now > t.AddSeconds(timeOut) Then
                    Throw New Exception("Connection timeout!")
                End If
                System.Threading.Thread.Sleep(100)
            End While

            tcp.EndConnect(ar)
            tcp.Close()

            Return True

        Catch
            Return False
        End Try
    End Function

    Private Sub PingWebPage(ByVal url As String, ByVal timeout As Integer)
        Dim req As HttpWebRequest = DirectCast(WebRequest.Create(url), HttpWebRequest)
        req.Method = "GET"
        req.Credentials = CredentialCache.DefaultCredentials
        req.ContentType = "text/xml"
        req.Timeout = timeout
        Dim rsp As WebResponse = req.GetResponse()
        Dim sr As New StreamReader(rsp.GetResponseStream(), Encoding.UTF8)
        Try
            sr.ReadLine()
        Finally
            sr.Close()
            rsp.Close()
        End Try
    End Sub

    Protected Function GetMenuData() As DataTable
        Dim _dt As DataTable
        _db = New SQLMethod
        Dim strSQL As String = "SELECT * FROM web_sitemap"
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        _dt = _db.GetDataTable(cmd, "web_sitemap")
        Return _dt
    End Function

    Public Function FindChildMenu(ByRef MainMenu As Menu, ByVal Pid As Integer, ByVal OpenAdminMenu As Boolean) As String
        Dim strMsg As String = String.Empty
        Dim Dt As New DataTable()
        Dim rows() As DataRow
        Dim filterExpr As String
        If OpenAdminMenu Then
            filterExpr = "[Parent] = " & Pid
        Else
            filterExpr = "[Parent] = " & Pid & " AND [ID] < 90000"
        End If

        Try
            MainMenu.Items.Clear()
            Dt = GetMenuData()
            rows = Dt.Select(filterExpr)
            For Each row In rows
                strMsg = BindMenu(MainMenu, Dt, row("ID"), OpenAdminMenu)
                If strMsg <> "0" Then Return strMsg
            Next
        Catch ex As Exception
            Return ex.Message()
        End Try

        Return "0"
    End Function

    Public Function BindMenu(ByRef MainMenu As Menu, ByVal PId As Integer, ByVal OpenAdminMenu As Boolean) As String
        Dim strMsg As String = String.Empty
        Dim Dt As New DataTable()

        Try
            MainMenu.Items.Clear()
            Dt = GetMenuData()
            strMsg = BindMenu(MainMenu, Dt, PId, OpenAdminMenu)
            If strMsg <> "0" Then Return strMsg
        Catch ex As Exception
            Return ex.Message()
        End Try

        Return "0"
    End Function

    Protected Function BindMenu(ByRef MainMenu As Menu, ByVal Dt As DataTable, ByVal PId As Integer, ByVal OpenAdminMenu As Boolean) As String
        Dim strMsg As String = String.Empty
        Try
            strMsg = AddRootMenu(MainMenu, Dt, PId, OpenAdminMenu)
            If strMsg <> "0" Then Return strMsg
            Dim rootCount As Integer = MainMenu.Items.Count - 1
            AddMenus(MainMenu.Items(rootCount), Dt, PId, OpenAdminMenu)
        Catch ex As Exception
            Return ex.Message()
        End Try

        Return "0"
    End Function

    Protected Function AddMenus(ByRef rootMenu As MenuItem, ByVal Dt As DataTable, ByVal PId As Integer, ByVal OpenAdminMenu As Boolean) As String
        Dim strMsg As String = String.Empty
        Dim rows() As DataRow
        Dim filterExpr As String
        If OpenAdminMenu Then
            filterExpr = "[Parent] = " & PId
        Else
            filterExpr = "[Parent] = " & PId & " AND [ID] < 90000"
        End If

        Try
            rows = Dt.Select(filterExpr)
            If rows.GetUpperBound(0) >= 0 Then
                Dim row As DataRow
                For Each row In rows
                    Dim NewMenu As New MenuItem()
                    Dim tmpId As Integer = row("ID")
                    Dim tmpsText As String = row("Title")
                    Dim tmpsValue As String = row("ID")

                    If Not row("Url") Is DBNull.Value Then
                        Dim tmpsUrl As String = row("Url")
                        NewMenu.NavigateUrl = tmpsUrl
                    End If

                    NewMenu.Text = tmpsText
                    NewMenu.Value = tmpsValue
                    rootMenu.ChildItems.Add(NewMenu)
                    strMsg = AddMenus(NewMenu, Dt, tmpId, OpenAdminMenu)
                    If strMsg <> "0" Then Return strMsg
                Next
            End If
        Catch ex As Exception
            Return ex.Message()
        End Try

        Return "0"
    End Function

    Protected Function AddRootMenu(ByRef pMenu As Menu, ByVal Dt As DataTable, ByVal PId As Integer, ByVal OpenAdminMenu As Boolean) As String
        Dim rows() As DataRow
        Dim filterExpr As String
        If OpenAdminMenu Then
            filterExpr = "[ID] = " & PId
        Else
            filterExpr = "[ID] = " & PId & " AND [ID] < 90000"
        End If

        Try
            rows = Dt.Select(filterExpr)
            For Each row In rows
                Dim RootMenu As New MenuItem()
                Dim tmpId As String = row("ID")
                Dim tmpsText As String = row("Title")
                Dim tmpsValue As String = row("ID")

                If Not row("Url") Is DBNull.Value Then
                    Dim tmpsUrl As String = row("Url")
                    RootMenu.NavigateUrl = tmpsUrl
                End If

                RootMenu.Text = tmpsText
                RootMenu.Value = tmpsValue
                pMenu.Items.Add(RootMenu)
            Next
        Catch ex As Exception
            Return ex.Message()
        End Try

        Return "0"
    End Function

End Class
