
Partial Class UserControl_Menu_Menu
    Inherits System.Web.UI.UserControl

    Dim appMenu As New AppFunction()

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Session("LoginUser") IsNot Nothing Then
            Dim tmpLoginUser As LoginUser = DirectCast(Session("LoginUser"), LoginUser)
            If tmpLoginUser.Access_Level >= 200 Then
                appMenu.FindChildMenu(MainMenu, 1, True)
            Else
                appMenu.FindChildMenu(MainMenu, 1, False)
            End If
        Else
            appMenu.FindChildMenu(MainMenu, 1, False)
        End If
    End Sub
End Class
