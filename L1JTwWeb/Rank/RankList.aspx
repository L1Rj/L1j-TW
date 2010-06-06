<%@ Page Language="VB" MasterPageFile="~/MasterPage.master" AutoEventWireup="false" CodeFile="RankList.aspx.vb" Inherits="Rank_RankList" title="未命名頁面" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <table border="0" cellpadding="0" cellspacing="5" summary="排版表格">
        <tr>
            <td style="text-align:center">前20名排行榜</td>
        </tr>
        <tr>
            <td>
                <asp:GridView ID="gvCharacters" runat="server" AutoGenerateColumns="False" 
                    BackColor="White" BorderColor="#DEDFDE" BorderStyle="None" BorderWidth="1px" 
                    CellPadding="4" ForeColor="Black" GridLines="Vertical" AllowPaging="True">
                    <RowStyle BackColor="#F7F7DE" />
                    <Columns>
                        <asp:BoundField DataField="char_name" HeaderText="名稱" />
                        <asp:BoundField DataField="level" HeaderText="等級" />
                        <asp:BoundField DataField="Type" HeaderText="職業" />
                        <asp:BoundField DataField="sex" HeaderText="性別" />
                        <asp:BoundField DataField="CurHp" HeaderText="血量" />
                        <asp:BoundField DataField="CurMp" HeaderText="魔力" />
                        <asp:BoundField DataField="ac" HeaderText="防禦" />
                        <asp:BoundField DataField="Str" HeaderText="力量" />
                        <asp:BoundField DataField="Con" HeaderText="體質" />
                        <asp:BoundField DataField="Dex" HeaderText="敏捷" />
                        <asp:BoundField DataField="Intel" HeaderText="智力" />
                        <asp:BoundField DataField="Wis" HeaderText="精神" />
                        <asp:BoundField DataField="Cha" HeaderText="魅力" />
                        <asp:BoundField DataField="Clanname" HeaderText="血盟" />
                        <asp:BoundField DataField="Lawful" HeaderText="正義值" />
                    </Columns>
                    <FooterStyle BackColor="#CCCC99" />
                    <PagerStyle BackColor="#F7F7DE" ForeColor="Black" HorizontalAlign="Right" />
                    <SelectedRowStyle BackColor="#CE5D5A" Font-Bold="True" ForeColor="White" />
                    <HeaderStyle BackColor="#6B696B" Font-Bold="True" ForeColor="White" />
                    <AlternatingRowStyle BackColor="White" />
                </asp:GridView>
            </td>
        </tr>
    </table>
</asp:Content>

