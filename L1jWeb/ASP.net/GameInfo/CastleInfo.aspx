<%@ Page Language="VB" MasterPageFile="~/MasterPage.master" AutoEventWireup="false" CodeFile="CastleInfo.aspx.vb" Inherits="GameInfo_CastleInfo" title="未命名頁面" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <table border="0" cellpadding="0" cellspacing="5" width="100%" summary="排版表格">
        <tr>
            <td style="text-align:center; width:100%">城堡資訊</td>
        </tr>
        <tr>
            <td>
                <asp:GridView ID="gvCastleInfo" runat="server" AutoGenerateColumns="False" 
                    BackColor="White" BorderColor="#DEDFDE" BorderStyle="None" BorderWidth="1px" 
                    CellPadding="4" ForeColor="Black" Width="100%">
                    <RowStyle BackColor="#F7F7DE" />
                    <Columns>
                        <asp:BoundField DataField="name" HeaderText="城堡" />
                        <asp:BoundField DataField="war_time" HeaderText="下次攻城時間" />
                        <asp:BoundField DataField="tax_rate" HeaderText="稅率" />
                        <asp:BoundField DataField="public_money" HeaderText="稅收" />
                        <asp:BoundField DataField="leader_name" HeaderText="城主" />
                        <asp:BoundField DataField="clan_name" HeaderText="血盟名稱" />
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

