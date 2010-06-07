Imports System.Data
Imports MySql.Data.MySqlClient

Partial Class GameInfo_CastleInfo
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        CastleList()
    End Sub

    Private Sub CastleList()
        Dim _db As New SQLMethod
        Dim strSQL As New StringBuilder()
        Dim cmd As New MySqlCommand()
        strSQL.Append("SELECT a.name, a.war_time, a.tax_rate, a.public_money, ")
        strSQL.Append("       b.leader_name, b.clan_name ")
        strSQL.Append("FROM castle a ")
        strSQL.Append("LEFT JOIN clan_data b ON a.castle_id LIKE CONCAT('%',b.hascastle,'%') ")
        strSQL.Append("ORDER BY castle_id ASC ")
        cmd.CommandText = strSQL.ToString()
        Dim _dt As DataTable = _db.GetDataTable(cmd, "characters")
        gvCastleInfo.DataSource = _dt
        gvCastleInfo.DataBind()
    End Sub

    Protected Sub gvCastleInfo_RowDataBound(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewRowEventArgs) Handles gvCastleInfo.RowDataBound
        If e.Row.Cells.Count > 1 Then
            If e.Row.Cells(4).Text.Trim() = "&nbsp;" Then
                e.Row.Cells(4).Text = "尚無城主"
            End If

            If e.Row.Cells(5).Text.Trim() = "&nbsp;" Then
                e.Row.Cells(5).Text = "尚無血盟"
            End If
        End If
    End Sub
End Class
