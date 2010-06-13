<%@ Page Language="VB" MasterPageFile="~/MasterPage.master" AutoEventWireup="false" CodeFile="ItemSearch.aspx.vb" Inherits="GameInfo_ItemSearch" title="未命名頁面" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <div style="text-align:left">
        <table border="0" cellpadding="0" cellspacing="5" summary="排版表格" width="100%">
            <tr>
                <td style="width:100%">
                    關鍵字：<asp:TextBox ID="txtItemName" runat="server" MaxLength="20" Columns="20"></asp:TextBox></td>
            </tr>
            <tr>
                <td>
                    <asp:RadioButtonList ID="SearchKind" runat="server" 
                        RepeatDirection="Horizontal">
                        <asp:ListItem Value="item" Selected="True">搜尋物品</asp:ListItem>
                        <asp:ListItem Value="npc">搜尋怪物</asp:ListItem>
                    </asp:RadioButtonList>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <asp:Button ID="btnSearch" runat="server" Text="送出" />
                </td>
            </tr>
            <tr>
                <td align="center">
                    <asp:MultiView ID="mvSearchList" runat="server">
                        <asp:View ID="viewItemList" runat="server">
                            <asp:GridView ID="gvItemList" runat="server" AutoGenerateColumns="False" 
                                Width="100%" CellPadding="4" ForeColor="#333333" EmptyDataText="無此物品" 
                                AllowPaging="True">
                                <RowStyle BackColor="#EFF3FB" />
                                <Columns>
                                    <asp:TemplateField HeaderText="物品名稱">
                                        <ItemTemplate>
                                            <asp:HyperLink ID="hlItemID" runat="server" NavigateUrl='<%# String.Format("ItemSearch.aspx?ItemID={0}&ItemName={1}", Eval("item_id"), Eval("name")) %>'
                                                Text='<%# Eval("name", "{0}") %>' Target="_self" ForeColor="Blue"></asp:HyperLink>                                                            
                                        </ItemTemplate>
                                    </asp:TemplateField>
                                </Columns>
                                <FooterStyle BackColor="#507CD1" Font-Bold="True" ForeColor="White" />
                                <PagerStyle BackColor="#2461BF" ForeColor="White" HorizontalAlign="Center" />
                                <SelectedRowStyle BackColor="#D1DDF1" Font-Bold="True" ForeColor="#333333" />
                                <HeaderStyle BackColor="#507CD1" Font-Bold="True" ForeColor="White" />
                                <EditRowStyle BackColor="#2461BF" />
                                <AlternatingRowStyle BackColor="White" />
                            </asp:GridView>
                        </asp:View>
                        <asp:View ID="viewNpcList" runat="server">
                            <asp:GridView ID="gvNpcList" runat="server" AutoGenerateColumns="False" 
                                Width="100%" CellPadding="4" EmptyDataText="無此怪物" ForeColor="#333333" 
                                AllowPaging="True">
                                <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                <Columns>
                                    <asp:TemplateField HeaderText="NPC名稱">
                                        <ItemTemplate>
                                            <asp:HyperLink ID="hlNpcID" runat="server" NavigateUrl='<%# String.Format("ItemSearch.aspx?NpcID={0}&NpcName={1}", Eval("npcid"), Eval("name")) %>'
                                                Text='<%# Eval("name", "{0}") %>' Target="_self" ForeColor="Blue"></asp:HyperLink>                                                            
                                        </ItemTemplate>
                                    </asp:TemplateField>
                                </Columns>
                                <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                <HeaderStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                <EditRowStyle BackColor="#999999" />
                                <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                            </asp:GridView>
                        </asp:View>
                        <asp:View ID="viewItemInfo" runat="server">
                            <table border="0" cellpadding="0" cellspacing="5" width="100%" summary="排版表格">
                                <tr>
                                    <td valign="top" style="width:50%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <tr>
                                                <td>NPC圖片(缺)</td>
                                            </tr>
                                            <tr>
                                                <td><asp:Label ID="lblNpcName" runat="server"></asp:Label></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td style="width:50%">
                                        <asp:GridView ID="gvItemInfo" runat="server" Width="100%" 
                                            AutoGenerateColumns="False" BackColor="White" BorderColor="#336666" 
                                            BorderStyle="Double" BorderWidth="3px" CellPadding="4" 
                                            GridLines="Horizontal" EmptyDataText="無掉落物品">
                                            <RowStyle BackColor="White" ForeColor="#333333" />
                                            <FooterStyle BackColor="White" ForeColor="#333333" />
                                            <PagerStyle BackColor="#336666" ForeColor="White" HorizontalAlign="Center" />
                                            <SelectedRowStyle BackColor="#339966" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#336666" Font-Bold="True" ForeColor="White" />
                                            <Columns>
                                                <asp:TemplateField HeaderText="物品名稱">
                                                    <ItemTemplate>
                                                        <asp:HyperLink ID="hlItemID" runat="server" NavigateUrl='<%# String.Format("ItemSearch.aspx?ItemID={0}&ItemName={1}", Eval("item_id"), Eval("name")) %>'
                                                            Text='<%# Eval("name", "{0}") %>' Target="_self" ForeColor="Blue"></asp:HyperLink>                                                            
                                                    </ItemTemplate>
                                                </asp:TemplateField>
                                                <asp:BoundField DataField="chance" HeaderText="掉落機率" />
                                            </Columns>
                                        </asp:GridView>
                                    </td>
                                </tr>
                            </table>
                        </asp:View>
                        <asp:View ID="viewNpcInfo" runat="server">
                            <table border="0" cellpadding="0" cellspacing="5" width="100%" summary="排版表格">
                                <tr>
                                    <td valign="top" style="width:50%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <tr>
                                                <td>物品圖片(缺)</td>
                                            </tr>
                                            <tr>
                                                <td><asp:Label ID="lblItemName" runat="server"></asp:Label></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td style="width:50%">
                                        <asp:GridView ID="gvNpcInfo" runat="server" Width="100%" 
                                            AutoGenerateColumns="False" BackColor="White" BorderColor="#336666" 
                                            BorderStyle="Double" BorderWidth="3px" CellPadding="4" 
                                            GridLines="Horizontal" EmptyDataText="無怪物掉落此物品">
                                            <RowStyle BackColor="White" ForeColor="#333333" />
                                            <FooterStyle BackColor="White" ForeColor="#333333" />
                                            <PagerStyle BackColor="#336666" ForeColor="White" HorizontalAlign="Center" />
                                            <SelectedRowStyle BackColor="#339966" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#336666" Font-Bold="True" ForeColor="White" />
                                            <Columns>
                                                <asp:TemplateField HeaderText="NPC名稱">
                                                    <ItemTemplate>
                                                        <asp:HyperLink ID="hlNpcID" runat="server" NavigateUrl='<%# String.Format("ItemSearch.aspx?NpcID={0}&NpcName={1}", Eval("npcid"), Eval("name")) %>'
                                                            Text='<%# Eval("name", "{0}") %>' Target="_self" ForeColor="Blue"></asp:HyperLink>                                                            
                                                    </ItemTemplate>
                                                </asp:TemplateField>
                                                <asp:BoundField DataField="chance" HeaderText="掉落機率" />
                                            </Columns>
                                        </asp:GridView>
                                    </td>
                                </tr>
                            </table>
                        </asp:View>
                    </asp:MultiView>
                </td>
            </tr>
        </table>
    </div>
</asp:Content>

