Imports Microsoft.VisualBasic

Public Class BasePage
    Inherits System.Web.UI.Page

    Private Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Session("LoginUser") Is Nothing Then
            Response.Redirect(FormsAuthentication.DefaultUrl)
            Return
        End If
    End Sub

    Public ReadOnly Property GetUser() As LoginUser
        Get
            If Session("LoginUser") IsNot Nothing Then
                Return DirectCast(Session("LoginUser"), LoginUser)
            Else
                Return Nothing
            End If
        End Get
    End Property

End Class
