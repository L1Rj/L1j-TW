<%@ Page Language="VB" MasterPageFile="~/MasterPage.master" AutoEventWireup="false" CodeFile="GameAccountList.aspx.vb" Inherits="GameAccountList" title="未命名頁面" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">  

    <script language="javascript" type="text/javascript">
        function Clear() {
            document.getElementById("<%=txtGameAccount.ClientID %>").value = "";
            document.getElementById("<%=txtGamePassword.ClientID %>").value = "";
            document.getElementById("<%=txtCPassword.ClientID %>").value = "";
            return false
        }
    </script>

    <table border="0" cellpadding="0" cellspacing="0" width="100%" summary="排版表格">
        <tr>
            <td style="width:100%">
                <asp:DataList ID="dlGameAccount" runat="server" RepeatColumns="3" Width="100%">
                    <ItemTemplate>
                        <a href="GameAccountList.aspx?id=<%# HttpUtility.HtmlEncode(DataBinder.Eval(Container.DataItem, "accounts_login")) %>"><%#DataBinder.Eval(Container.DataItem, "accounts_login")%></a>
                    </ItemTemplate>
                </asp:DataList>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>
                <asp:Button ID="btnAddGameAccount" runat="server" BackColor="White" 
                    BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" 
                    Font-Names="Verdana" ForeColor="#284E98" Text="新增遊戲帳號" />
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>
                <asp:MultiView ID="mvGameAccount" runat="server">
                    <asp:View ID="viewAddGameAccount" runat="server">
                        <table border="0" cellpadding="3" cellspacing="0" summary="排版表格">
                            <tr>
                                <td align="right">
                                    <asp:Label ID="lblAccount" runat="server" Text="遊戲帳號："></asp:Label>
                                </td>
                                <td align="left">
                                    <asp:TextBox ID="txtGameAccount" MaxLength="16" runat="server" Width="160px" 
                                        ValidationGroup="vgRegister"></asp:TextBox>
                                    <asp:RequiredFieldValidator ID="rfvAccount" runat="server" ErrorMessage="*" 
                                        ControlToValidate="txtGameAccount" Display="Dynamic" 
                                        ValidationGroup="vgRegister" />
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Label ID="lblPassword" runat="server" Text="遊戲密碼："></asp:Label>
                                </td>
                                <td align="left">
                                    <asp:TextBox ID="txtGamePassword" MaxLength="16" runat="server" TextMode="Password" 
                                        Width="160px" ValidationGroup="vgRegister"></asp:TextBox>
                                    <asp:RequiredFieldValidator ID="rfvPassword" runat="server" ErrorMessage="*" 
                                        ControlToValidate="txtGamePassword" Display="Dynamic" 
                                        ValidationGroup="vgRegister" />
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <asp:Label ID="lblCPassword" runat="server" Text="確認密碼："></asp:Label>
                                </td>
                                <td align="left">
                                    <asp:TextBox ID="txtCPassword" MaxLength="16" runat="server" 
                                        TextMode="Password" Width="160px" ValidationGroup="vgRegister"></asp:TextBox>
                                    <asp:RequiredFieldValidator ID="rfvCPassword" runat="server" ErrorMessage="*" 
                                        ControlToValidate="txtCPassword" Display="Dynamic" 
                                        ValidationGroup="vgRegister" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <asp:LinkButton ID="lbAddGameAccount" runat="server" ValidationGroup="vgRegister">建立帳號</asp:LinkButton>
                                    &nbsp;&nbsp;
                                    <asp:LinkButton ID="lbCancel" runat="server" onclientclick="return Clear()">清除</asp:LinkButton>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <asp:CompareValidator ID="cvPassword" runat="server" 
                                        ErrorMessage="密碼和確認密碼必須相符。<br />" ControlToCompare="txtGamePassword" 
                                        ControlToValidate="txtCPassword" Display="Dynamic" 
                                        ValidationGroup="vgRegister"></asp:CompareValidator>
                                </td>
                            </tr>
                        </table>
                    </asp:View>
                    <asp:View ID="viewGameAccount" runat="server">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" summary="排版表格">
                            <tr>
                                <td>
                                    <asp:GridView ID="gvGameAccount" runat="server" AutoGenerateColumns="False" 
                                        CellPadding="4" ForeColor="#333333" GridLines="None">
                                        <RowStyle BackColor="#EFF3FB" />
                                        <Columns>
                                            <asp:BoundField DataField="login" HeaderText="遊戲帳號" ItemStyle-Width="150px" />
                                            <asp:BoundField DataField="lastactive" HeaderText="最後登入日期" ItemStyle-Width="200px" />
                                            <asp:BoundField DataField="IP" HeaderText="IP" ItemStyle-Width="150px" />
                                        </Columns>
                                        <FooterStyle BackColor="#507CD1" Font-Bold="True" ForeColor="White" />
                                        <PagerStyle BackColor="#2461BF" ForeColor="White" HorizontalAlign="Center" />
                                        <SelectedRowStyle BackColor="#D1DDF1" Font-Bold="True" ForeColor="#333333" />
                                        <HeaderStyle BackColor="#507CD1" Font-Bold="True" ForeColor="White" />
                                        <EditRowStyle BackColor="#2461BF" />
                                        <AlternatingRowStyle BackColor="White" />
                                    </asp:GridView>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>
                                    <asp:DetailsView ID="dvCharacters" runat="server" Width="524px" 
                                        BackColor="White" BorderColor="#DEDFDE" BorderStyle="None" BorderWidth="1px" 
                                        CellPadding="4" ForeColor="Black" 
                                        AutoGenerateRows="False">
                                        <FooterStyle BackColor="#CCCC99" />
                                        <RowStyle BackColor="#F7F7DE" />
                                        <PagerStyle BackColor="#F7F7DE" ForeColor="Black" HorizontalAlign="Right" />
                                        <Fields>
                                            <asp:BoundField DataField="char_name" HeaderText="角色名稱" />
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
                                        </Fields>
                                        <HeaderStyle BackColor="#6B696B" Font-Bold="True" ForeColor="White" />
                                        <EditRowStyle BackColor="#CE5D5A" Font-Bold="True" ForeColor="White" />
                                        <AlternatingRowStyle BackColor="White" />
                                    </asp:DetailsView>
                                </td>
                            </tr>
                        </table>
                    </asp:View>
                </asp:MultiView>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblMsg" runat="server"></asp:Label>
            </td>
        </tr>
    </table>
</asp:Content>

