Imports System.Data
Imports MySql.Data.MySqlClient

Partial Class GameInfo_ItemSearch
    Inherits System.Web.UI.Page

    Private _db As SQLMethod
    Private strSQL As StringBuilder
    Private cmd As MySqlCommand

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        Dim _itemid As String = HttpUtility.UrlDecode(Request.QueryString("itemid"))
        Dim _itemname As String = HttpUtility.UrlDecode(Request.QueryString("itemname"))
        Dim _npcid As String = HttpUtility.UrlDecode(Request.QueryString("npcid"))
        Dim _npcname As String = HttpUtility.UrlDecode(Request.QueryString("npcname"))

        If _itemid <> String.Empty And _itemname <> String.Empty Then
            NpcInfoBind(_itemid)
            lblItemName.Text = _itemname
        ElseIf _npcid <> String.Empty And _npcname <> String.Empty Then
            ItemInfoBind(_npcid)
            lblNpcName.Text = _npcname
        End If
    End Sub

    Protected Sub btnSearch_Click(ByVal sender As Object, ByVal e As System.EventArgs) Handles btnSearch.Click
        Dim _searchkind As String = SearchKind.SelectedValue

        Select Case _searchkind
            Case "item"
                ItemListBind(txtItemName.Text)
            Case "npc"
                NpcListBind(txtItemName.Text)
        End Select
    End Sub

    Protected Sub gvItemList_PageIndexChanging(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewPageEventArgs) Handles gvItemList.PageIndexChanging
        ItemListBind(txtItemName.Text)
        gvItemList.PageIndex = e.NewPageIndex
        gvItemList.DataBind()
    End Sub

    Protected Sub gvNpcList_PageIndexChanging(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewPageEventArgs) Handles gvNpcList.PageIndexChanging
        NpcListBind(txtItemName.Text)
        gvNpcList.PageIndex = e.NewPageIndex
        gvNpcList.DataBind()
    End Sub

    Protected Sub gvItemInfo_RowDataBound(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewRowEventArgs) Handles gvItemInfo.RowDataBound
        If e.Row.Cells.Count > 1 Then
            Dim _chance As String = e.Row.Cells(1).Text

            If IsNumeric(_chance) Then
                e.Row.Cells(1).Text = Chance(_chance)
            End If
        End If
    End Sub

    Protected Sub gvNpcInfo_RowDataBound(ByVal sender As Object, ByVal e As System.Web.UI.WebControls.GridViewRowEventArgs) Handles gvNpcInfo.RowDataBound
        If e.Row.Cells.Count > 1 Then
            Dim _chance As String = e.Row.Cells(1).Text

            If IsNumeric(_chance) Then
                e.Row.Cells(1).Text = Chance(_chance)
            End If
        End If
    End Sub

    Private Sub ItemListBind(ByVal ItemName As String)
        Dim _dt As DataTable
        _dt = SearchItemID(txtItemName.Text)
        gvItemList.DataSource = _dt
        gvItemList.DataBind()
        mvSearchList.ActiveViewIndex = 0
    End Sub

    Private Sub NpcListBind(ByVal NpcName As String)
        Dim _dt As DataTable
        _dt = SearchNpcID(txtItemName.Text)
        gvNpcList.DataSource = _dt
        gvNpcList.DataBind()
        mvSearchList.ActiveViewIndex = 1
    End Sub

    Private Sub ItemInfoBind(ByVal NpcID As String)
        Dim _dt As DataTable
        _dt = SearchItemName(NpcID)
        gvItemInfo.DataSource = _dt
        gvItemInfo.DataBind()
        mvSearchList.ActiveViewIndex = 2
    End Sub

    Private Sub NpcInfoBind(ByVal ItemID As String)
        Dim _dt As DataTable
        _dt = SearchNpcName(ItemID)
        gvNpcInfo.DataSource = _dt
        gvNpcInfo.DataBind()
        mvSearchList.ActiveViewIndex = 3
    End Sub

    Private Function SearchItemID(ByVal ItemName As String) As DataTable
        Dim _dt As DataTable
        _db = New SQLMethod
        strSQL = New StringBuilder()
        cmd = New MySqlCommand()
        strSQL.Append("SELECT item_id , name FROM weapon WHERE name LIKE @ItemName ")
        strSQL.Append("UNION ALL ")
        strSQL.Append("SELECT item_id , name FROM armor WHERE name LIKE @ItemName ")
        strSQL.Append("UNION ALL ")
        strSQL.Append("SELECT item_id , name FROM etcitem WHERE name LIKE @ItemName ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@ItemName", MySqlDbType.VarChar).Value = "%" & ItemName & "%"
        _dt = _db.GetDataTable(cmd, "etcitem")
        Return _dt
    End Function

    Private Function SearchItemName(ByVal MobID As String) As DataTable
        Dim _dt As DataTable
        _db = New SQLMethod
        strSQL = New StringBuilder()
        cmd = New MySqlCommand()
        strSQL.Append("SELECT w.item_id , w.name , d.chance FROM droplist d ")
        strSQL.Append("INNER JOIN weapon w ON w.item_id = d.itemId ")
        strSQL.Append("WHERE d.mobId = @mobId ")
        strSQL.Append("UNION ALL ")
        strSQL.Append("SELECT a.item_id , a.name , d.chance FROM droplist d ")
        strSQL.Append("INNER JOIN armor a ON a.item_id = d.itemId ")
        strSQL.Append("WHERE d.mobId = @mobId ")
        strSQL.Append("UNION ALL ")
        strSQL.Append("SELECT e.item_id , e.name , d.chance FROM droplist d ")
        strSQL.Append("INNER JOIN etcitem e ON e.item_id = d.itemId ")
        strSQL.Append("WHERE d.mobId = @mobId ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@mobId", MySqlDbType.VarChar).Value = MobID
        _dt = _db.GetDataTable(cmd, "droplist")
        Return _dt
    End Function

    Private Function SearchNpcID(ByVal NpcName As String) As DataTable
        Dim _dt As DataTable
        _db = New SQLMethod
        strSQL = New StringBuilder()
        cmd = New MySqlCommand()
        strSQL.Append("SELECT npcid, name FROM npc WHERE name LIKE @NpcName ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@NpcName", MySqlDbType.VarChar).Value = "%" & NpcName & "%"
        _dt = _db.GetDataTable(cmd, "npc")
        Return _dt
    End Function

    Private Function SearchNpcName(ByVal ItemID As String) As DataTable
        Dim _dt As DataTable
        _db = New SQLMethod
        strSQL = New StringBuilder()
        cmd = New MySqlCommand()
        strSQL.Append("SELECT n.npcid , n.name , d.chance FROM npc n ")
        strSQL.Append("INNER JOIN droplist d ON n.npcid = d.mobId ")
        strSQL.Append("WHERE d.itemId = @itemId ")
        cmd.CommandText = strSQL.ToString()
        cmd.Parameters.Add("@itemId", MySqlDbType.VarChar).Value = ItemID
        _dt = _db.GetDataTable(cmd, "npc")
        Return _dt
    End Function

    Private Function Chance(ByVal _chance As String) As String
        Dim tmpchance As String = CStr(Math.Round(CSng(_chance) / 10000, 2)) & "%"
        Return tmpchance
    End Function
End Class
