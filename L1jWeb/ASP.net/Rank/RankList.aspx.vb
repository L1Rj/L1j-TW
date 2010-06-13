Imports System.Text
Imports System.Data
Imports MySql.Data.MySqlClient

Partial Class Rank_RankList
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        RankList()
    End Sub

    Private Sub RankList()
        Dim _db As New SQLMethod
        Dim strSQL As New StringBuilder()
        Dim cmd As New MySqlCommand()
        strSQL.Append("SELECT char_name, level, Type, sex, CurHp, CurMp, ac, ")
        strSQL.Append("       Str, Con, Dex, Intel, Wis, Cha, Clanname, Lawful ")
        strSQL.Append("FROM characters ")
        strSQL.Append("ORDER BY level DESC, Exp DESC ")
        strSQL.Append("LIMIT 0,20 ")
        cmd.CommandText = strSQL.ToString()
        Dim _dt As DataTable = _db.GetDataTable(cmd, "characters")
        gvCharacters.DataSource = _dt
        gvCharacters.DataBind()
    End Sub

    Protected Sub gvCharacters_RowDataBound(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewRowEventArgs) Handles gvCharacters.RowDataBound
        If e.Row.RowIndex <> -1 Then
            Dim tmpType As String = e.Row.Cells(2).Text
            Dim tmpSex As String = e.Row.Cells(3).Text

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

            e.Row.Cells(2).Text = tmpType
            e.Row.Cells(3).Text = tmpSex
        End If
    End Sub

    Protected Sub gvCharacters_PageIndexChanging(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewPageEventArgs) Handles gvCharacters.PageIndexChanging
        gvCharacters.PageIndex = e.NewPageIndex
        gvCharacters.DataBind()
    End Sub
End Class
