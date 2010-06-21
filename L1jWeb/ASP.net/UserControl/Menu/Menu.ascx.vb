
Partial Class UserControl_Menu_Menu
    Inherits System.Web.UI.UserControl

    Dim appMenu As New AppFunction()

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        appMenu.FindChildMenu(MainMenu, 1, False)
    End Sub
End Class
