Imports System.Net.Sockets
Imports System.Net

Partial Class MasterPage
    Inherits System.Web.UI.MasterPage

    Dim _appfunc As AppFunction

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Session("LoginUser") IsNot Nothing Then
            Dim _loginuser As LoginUser = DirectCast(Session("LoginUser"), LoginUser)
            GetControlPanel(_loginuser)
        End If
    End Sub

    Private Sub GetControlPanel(ByVal _loginuser As LoginUser)
        If _loginuser.Access_Level >= 200 Then
            _appfunc = New AppFunction
            pnlControlPage.Visible = True

            '*****設定頁面上方工具列*****
            Dim myTR1 As New TableRow()
            '---回首頁設定---
            Dim myTC1 As New TableCell()
            '依權限加入功能選單
            _appfunc.Authority_Menu(_loginuser, myTR1)

            tbFunction.Rows.Add(myTR1)
        End If
    End Sub

End Class

