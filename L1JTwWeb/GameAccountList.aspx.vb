Imports System.Data

Partial Class GameAccountList
    Inherits BasePage

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        If Not IsPostBack Then
            dvCharacters.AllowPaging = True
            Page.Form.DefaultButton = lbAddGameAccount.UniqueID
        End If

        DisplayGameAccount(GetUser.Account)

        Dim _get_gameaccount As String = HttpUtility.HtmlDecode(Request.QueryString("id"))

        '顯示已註冊帳號資訊
        If _get_gameaccount <> String.Empty Then
            DisplayUserInfo(_get_gameaccount)
            DisplayCharacters(_get_gameaccount)
            mvGameAccount.ActiveViewIndex = 1
        End If
    End Sub

    Protected Sub btnAddGameAccount_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles btnAddGameAccount.Click
        '檢查帳號是否已到達3組
        Dim _appfunc As New AppFunction
        Dim _dt As DataTable = _appfunc.GetGameAccount(GetUser.Account)
        If _dt.Rows.Count >= 3 Then
            lblMsg.ForeColor = Drawing.Color.Red
            lblMsg.Text = "您的遊戲帳號已達3組無法再新增！"
        Else
            mvGameAccount.ActiveViewIndex = 0
        End If
    End Sub

    Protected Sub lbAddGameAccount_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles lbAddGameAccount.Click
        Dim _gameaccount As New GameAccount
        _gameaccount.CreateGameAccount(GetUser.Account, txtGameAccount.Text.Trim(), txtGamePassword.Text.Trim(), Page.Request.UserHostAddress, Page.Request.UserHostName)

        If _gameaccount.ErrMsg = String.Empty Then
            lblMsg.ForeColor = Drawing.Color.Blue
            lblMsg.Text = "遊戲帳號申請成功！"

            '更新頁面
            DisplayGameAccount(GetUser.Account)

            Dim _appfunc As New AppFunction
            Dim _dt As DataTable = _appfunc.GetGameAccount(GetUser.Account)
            DirectCast(DirectCast(Master.FindControl("Login1"), UserControl).FindControl("lbGameAccount"), LinkButton).Text = "遊戲帳號(" & _dt.Rows.Count & ")"
        Else
            lblMsg.ForeColor = Drawing.Color.Red
            lblMsg.Text = _gameaccount.ErrMsg
        End If
    End Sub

    Private Sub DisplayGameAccount(ByVal WebAccount As String)
        Dim _appfunc As New AppFunction
        Dim _dt As DataTable = _appfunc.GetGameAccount(WebAccount)
        dlGameAccount.DataSource = _dt
        dlGameAccount.DataBind()
    End Sub

    Private Sub DisplayUserInfo(ByVal GameAccount As String)
        Dim _gameaccount As New GameAccount
        Dim _gameaccountDT As DataTable = _gameaccount.GetGameAccountDT(GameAccount)
        gvGameAccount.DataSource = _gameaccountDT
        gvGameAccount.DataBind()
    End Sub

    Private Sub DisplayCharacters(ByVal GameAccount As String)
        Dim _list_characters As List(Of Characters)
        Dim _characters As New Characters
        _list_characters = _characters.GetCharacterInfo(GameAccount)
        dvCharacters.DataSource = _list_characters
        dvCharacters.DataBind()
    End Sub
    
    Protected Sub dvCharacters_PageIndexChanging(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.DetailsViewPageEventArgs) Handles dvCharacters.PageIndexChanging
        dvCharacters.PageIndex = e.NewPageIndex
        dvCharacters.DataBind()
    End Sub

    Protected Sub dvCharacters_DataBound(ByVal sender As Object, ByVal e As System.EventArgs) Handles dvCharacters.DataBound
        If dvCharacters.Rows.Count > 1 Then
            Dim tmpType As String = dvCharacters.Rows(2).Cells(1).Text
            Dim tmpSex As String = dvCharacters.Rows(3).Cells(1).Text

            Select Case tmpType
                Case "0"
                    tmpType = "王族"
                Case "1"
                    tmpType = "騎士"
                Case "2"
                    tmpType = "妖精"
                Case "3"
                    tmpType = "法師"
                Case "4"
                    tmpType = "黑暗妖精"
                Case "5"
                    tmpType = "龍騎士"
                Case "6"
                    tmpType = "幻術士"
            End Select

            Select Case tmpSex
                Case "0"
                    tmpSex = "男"
                Case "1"
                    tmpSex = "女"
            End Select

            dvCharacters.Rows(2).Cells(1).Text = tmpType
            dvCharacters.Rows(3).Cells(1).Text = tmpSex
        End If
    End Sub
End Class
