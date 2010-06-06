
Partial Class UserControl_Server_ServerStatus
    Inherits System.Web.UI.UserControl

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        Dim _appfunc As New AppFunction
        Dim L1JTWName As String = ConfigurationManager.AppSettings("L1JTWName")
        Dim L1JTWIP As String = ConfigurationManager.AppSettings("L1JTWIP")
        Dim L1JTWPort As String = ConfigurationManager.AppSettings("L1JTWPort")

        If _appfunc.IsServerConnectable(L1JTWIP, L1JTWPort, 3) Then
            imgStatus.ImageUrl = "~/Images/ServerStatus/start.gif"
        Else
            imgStatus.ImageUrl = "~/Images/ServerStatus/stop.gif"
        End If

        lblL1JTWName.Text = L1JTWName
    End Sub
End Class
