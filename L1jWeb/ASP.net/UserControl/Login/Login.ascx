<%@ Control Language="VB" AutoEventWireup="false" CodeFile="Login.ascx.vb" Inherits="UserControl_Login_Login" %>

<table border="0" cellpadding="4" cellspacing="0" width="100%" style="border-collapse:collapse;">
    <tr>
        <td>
            <asp:MultiView ID="mvLogin" runat="server">
                <asp:View ID="ViewAnonymous" runat="server">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td align="center" colspan="2">
                                登入</td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="UserNameLabel" runat="server">帳號：</asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtAccount" runat="server" MaxLength="16" Width="120px"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="txtAccountRequired" runat="server" 
                                    ControlToValidate="txtAccount" ErrorMessage="必須提供帳號。" ToolTip="必須提供帳號。" 
                                    ValidationGroup="Login1">*</asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Label ID="PasswordLabel" runat="server">密碼：</asp:Label>
                            </td>
                            <td align="left">
                                <asp:TextBox ID="txtPassword" runat="server" TextMode="Password" MaxLength="16" Width="120px"></asp:TextBox>
                                <asp:RequiredFieldValidator ID="PasswordRequired" runat="server" 
                                    ControlToValidate="txtPassword" ErrorMessage="必須提供密碼。" ToolTip="必須提供密碼。" 
                                    ValidationGroup="Login1">*</asp:RequiredFieldValidator>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="right">
                                <asp:LinkButton ID="lbforgotpassword" runat="server">忘記密碼</asp:LinkButton>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2" style="color:Red;">
                                <asp:Literal ID="lblMsg" runat="server" EnableViewState="False"></asp:Literal>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" colspan="2">
                                <asp:Button ID="LoginButton" runat="server" BackColor="White" 
                                    BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" CommandName="Login" 
                                    Font-Names="Verdana" ForeColor="#284E98" Text="登入" 
                                    ValidationGroup="Login1" />
                                &nbsp;<asp:Button ID="RegisterButton" runat="server" BackColor="#FFFBFF"
                                    BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" 
                                    Font-Names="Verdana" ForeColor="#284E98" Text="註冊" 
                                    PostBackUrl="~/Register.aspx" />
                            </td>
                        </tr>
                    </table>
                </asp:View>
                <asp:View ID="ViewLoggedIn" runat="server">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td align="left">
                                帳號：<asp:Label ID="lblAccount" runat="server"></asp:Label></td>
                        </tr>
                        <tr>
                            <td align="left">
                                IP：<asp:Label ID="lblIP" runat="server"></asp:Label></td>
                        </tr>
                        <tr>
                            <td align="center">
                                <table border="0" cellpadding="0" cellspacing="0" width="100px">
                                    <tr>
                                        <td align="left">
                                            <asp:LinkButton ID="lbModifyPersonalData" runat="server" PostBackUrl="~/ModifyPersonalData.aspx">資料修改</asp:LinkButton>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">
                                            <asp:LinkButton ID="lbGameAccount" runat="server" PostBackUrl="~/GameAccountList.aspx"></asp:LinkButton>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <asp:Button ID="btnLogout" runat="server" BackColor="#FFFBFF"
                                    BorderColor="#507CD1" BorderStyle="Solid" BorderWidth="1px" 
                                    Font-Names="Verdana" ForeColor="#284E98" Text="登出" />
                            </td>
                        </tr>
                    </table>
                </asp:View>
            </asp:MultiView>
                
        </td>
    </tr>
</table>